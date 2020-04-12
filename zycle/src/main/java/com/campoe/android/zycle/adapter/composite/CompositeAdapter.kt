package com.campoe.android.zycle.adapter.composite

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.`typealias`.AdapterBlock
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.adapter.EmptyAdapter
import com.campoe.android.zycle.adapter.observer.AdapterDataObserver
import com.campoe.android.zycle.adapter.util.AdapterLut
import java.lang.ref.WeakReference
import java.util.*

class CompositeAdapter(vararg adapters: Adapter) : Adapter() {

    private val entries: Array<out OffsetAdapterEntry> =
        Array(adapters.size) { i ->
            OffsetAdapterEntry(
                adapters[i]
            )
        }

    private val viewTypes: WeakHashMap<Int, WeakReference<Adapter>> = WeakHashMap()
    private var compositeItemCount: Int = 0
    private val adapterLut: AdapterLut =
        AdapterLut()
    override val hasStableIds: Boolean = adapters.size == 1 && adapters[0].hasStableIds

    constructor(adapters: Collection<Adapter>) : this(*adapters.toTypedArray())

    private fun lookupEntry(position: Int): OffsetAdapterEntry =
        entries[adapterLut.getAdapterIndex(position)]

    internal fun childPosition(parentPosition: Int): Int {
        return lookupEntry(parentPosition).childPosition(parentPosition)
    }

    internal fun innerPosition(parentPosition: Int): Int {
        return lookupEntry(parentPosition).innerPosition(parentPosition)
    }

    override fun flatten(): Adapter {
        val entries: MutableList<OffsetAdapterEntry> = mutableListOf()
        this.entries.flatMapTo(entries) {
            it.adapter.flatten()
                .let { flat ->
                    if (flat is CompositeAdapter) flat.entries.asIterable() else listOf(
                        OffsetAdapterEntry(
                            flat
                        )
                    )
                }
        }
        return when {
            entries.isEmpty() -> EmptyAdapter
            entries.size == 1 -> entries[0].adapter
            else -> CompositeAdapter(
                entries.map { it.adapter })
        }
    }

    override fun getItemId(position: Int): Long {
        val entry = lookupEntry(position)
        return entry.adapter.getItemId(entry.childPosition(position))
    }

    override fun getItemCount(): Int {
        return if (hasObservers()) {
            compositeItemCount
        } else entries.sumBy { it.adapter.getItemCount() }
    }

    override fun getItemViewType(position: Int): Int {
        val entry = lookupEntry(position)
        val adapter = entry.adapter
        val viewType = adapter.getItemViewType(entry.childPosition(position))
        if (!viewTypes.containsKey(viewType)) {
            viewTypes[viewType] = WeakReference(adapter)
        }
        return viewType
    }

    override fun onFirstObserverRegistered() {
        compositeItemCount = adapterLut.rebuild(entries)
        if (compositeItemCount > 0) {
            notifyItemRangeInserted(0, compositeItemCount)
        }
        entries.forEach {
            it.register()
        }
    }

    override fun onLastObserverUnregistered() {
        entries.forEach {
            it.unregister()
        }
        compositeItemCount = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val adapter = viewTypes[viewType]?.get() ?: throw RuntimeException()
        return adapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val entry = lookupEntry(position)
        entry.adapter.onBindViewHolder(holder, entry.childPosition(position))
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        val position = holder.layoutPosition
        if (position == RecyclerView.NO_POSITION) return
        lookupEntry(position).adapter.onViewRecycled(holder)
    }

    override fun getLayoutRes(viewType: Int): Int {
        val adapter = viewTypes[viewType]?.get() ?: throw RuntimeException()
        return adapter.getLayoutRes(viewType)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        val position = holder.layoutPosition
        if (position == RecyclerView.NO_POSITION) return false
        return lookupEntry(position).adapter.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        val position = holder.layoutPosition
        if (position == RecyclerView.NO_POSITION) return
        lookupEntry(position).adapter.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        val position = holder.layoutPosition
        if (position == RecyclerView.NO_POSITION) return
        lookupEntry(position).adapter.onViewDetachedFromWindow(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        entries.forEach { it.adapter.onAttachedToRecyclerView(recyclerView) }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        entries.forEach { it.adapter.onDetachedFromRecyclerView(recyclerView) }
        super.onDetachedFromRecyclerView(recyclerView)
    }

    internal inner class OffsetAdapterEntry(@JvmField internal val adapter: Adapter) {

        private val forwardingObserver: AdapterDataObserver =
            object : AdapterDataObserver() {
                override fun onChanged() {
                    compositeItemCount = adapterLut.rebuild(entries)
                    notifyItemRangeChanged(offset, adapter.getItemCount())
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    compositeItemCount = adapterLut.rebuild(entries)
                    notifyItemRangeInserted(
                        positionStart + offset,
                        itemCount
                    )
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    compositeItemCount = adapterLut.rebuild(entries)
                    notifyItemRangeRemoved(positionStart + offset, itemCount)
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    notifyItemRangeChanged(positionStart + offset, itemCount)
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                    notifyItemRangeChanged(positionStart + offset, itemCount, payload)
                }

                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    notifyItemRangeMoved(fromPosition + offset, toPosition + offset, itemCount)
                }
            }

        internal var offset: Int = 0

        internal fun childPosition(parentPosition: Int): Int = parentPosition - offset

        internal fun innerPosition(parentPosition: Int): Int {
            val childPosition = childPosition(parentPosition)
            return if (adapter is CompositeAdapter) adapter.childPosition(childPosition)
            else childPosition
        }

        internal fun register() {
            adapter.registerAdapterDataObserver(forwardingObserver)
        }

        internal fun unregister() {
            adapter.unregisterAdapterDataObserver(forwardingObserver)
        }

    }

    class Builder : com.campoe.android.zycle.util.Builder<Adapter> {

        private val adapters: MutableList<Adapter> = mutableListOf()
        private var postBuild: AdapterBlock? = null

        fun prepend(adapter: Adapter) =
            apply { adapters.add(0, adapter) }

        fun append(adapter: Adapter) =
            apply { adapters.add(adapter) }

        fun prepend(vararg adapters: Adapter) =
            apply { this.adapters.addAll(0, adapters.toList()) }

        fun append(vararg adapters: Adapter) =
            apply { this.adapters.addAll(adapters.toList()) }

        fun flatten() =
            apply {
                val adapters = adapters.toList()
                this.adapters.clear()
                adapters.flatMapTo(this.adapters) {
                    it.flatten()
                        .let { flat ->
                            if (flat is CompositeAdapter) flat.entries.map { e -> e.adapter } else listOf(
                                flat
                            )
                        }
                }
            }

        fun postBuild(block: AdapterBlock) = apply { postBuild = block }

        override fun build(): Adapter {
            val adapter = when {
                adapters.isEmpty() -> return EmptyAdapter
                adapters.size == 1 -> adapters[0]
                else -> CompositeAdapter(
                    adapters
                )
            }
            return postBuild?.invoke(adapter) ?: adapter
        }

    }

}