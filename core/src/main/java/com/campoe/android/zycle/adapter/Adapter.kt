package com.campoe.android.zycle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.ZycleDsl
import com.campoe.android.zycle.`typealias`.*
import com.campoe.android.zycle.eventhook.EventHook
import com.campoe.android.zycle.layout.Layout
import com.campoe.android.zycle.mapper.Mapper
import com.campoe.android.zycle.mapper.mapper
import com.campoe.android.zycle.observablelist.ObservableList
import com.campoe.android.zycle.viewholder.ViewHolder

@ZycleDsl
open class Adapter<E : Any, VH : ViewHolder>(internal val items: MutableList<E> = mutableListOf()) :
    RecyclerView.Adapter<VH>(), IAdapter<E, VH> {

    constructor(items: Array<out E>) : this(items.toMutableList())

    @Suppress("LeakingThis")
    private var callback: ObservableList.ObservableListCallback<E, VH> =
        ObservableList.ObservableListCallback(this)

//    private var headers: MutableList<Layout<VH>> = mutableListOf() // FIXME: item positions heavily depend on this
    private val layout: Layout<VH> = ItemLayout()
    private var footers: MutableList<Layout<VH>> = mutableListOf()

    private var onAttachListener: OnAttachListener? = null
    private var onDetachListener: OnDetachListener? = null

    private var layoutInflater: LayoutInflater? = null
    private var recyclerView: RecyclerView? = null

    private val map: MutableMap<Class<*>, Mapper<E, VH>> = mutableMapOf()

    private var layoutProvider: LayoutProvider<E>? = null
    private var stableIdProvider: StableIdProvider<E>? = null

    private var onBindListener: OnBindListener<E, VH>? = null
    private var onRecycleListener: OnRecycleListener<E, VH>? = null

    private var eventHooks: MutableList<EventHook<E, VH>> = mutableListOf()

    override fun attach(view: RecyclerView) =
        apply { view.adapter = this }

    override fun detach() =
        apply { recyclerView?.adapter = null }

//    fun header(header: Layout<VH>) =
//        apply { headers.add(header) }

    fun footer(footer: Layout<VH>) =
        apply { footers.add(footer) }

    internal fun onAttach(f: OnAttachListener) =
        apply { onAttachListener = f }

    internal fun onDetach(f: OnDetachListener) =
        apply { onDetachListener = f }

    @Suppress("UNCHECKED_CAST")
    fun <T : E> map(clazz: Class<T>, mapper: Mapper<T, VH>) =
        apply { map[clazz] = mapper as Mapper<E, VH> }

    inline fun <reified T : E> map(mapper: Mapper<T, VH>) =
        map(T::class.java, mapper)

    @Suppress("UNCHECKED_CAST")
    override fun <T : E> map(
        clazz: Class<T>,
        @LayoutRes
        layoutRes: Int,
        block: MapperBlock<T, VH>?
    ): Adapter<E, VH> =
        apply {
            map[clazz] = mapper<T, VH>(layoutRes)
                .apply { block?.invoke(this) } as Mapper<E, VH>
        }

    inline fun <reified T : E> map(
        @LayoutRes layoutRes: Int
    ) =
        map(T::class.java, layoutRes)

    inline fun <reified T : E> map(
        @LayoutRes layoutRes: Int,
        noinline block: MapperBlock<T, VH>
    ) =
        map(T::class.java, layoutRes, block)

    override fun layout(f: LayoutProvider<E>): Adapter<E, VH> =
        apply { layoutProvider = f }

    override fun stableId(f: StableIdProvider<E>): Adapter<E, VH> =
        apply { stableIdProvider = f }

    override fun onBind(f: OnBindListener<E, VH>): Adapter<E, VH> =
        apply { onBindListener = f }

    override fun onRecycle(f: OnRecycleListener<E, VH>): Adapter<E, VH> =
        apply { onRecycleListener = f }

    override fun onEvent(eventHook: EventHook<E, VH>): Adapter<E, VH> =
        apply { eventHooks.add(eventHook) }

    @Suppress("UNCHECKED_CAST")
    protected open fun instantiateViewHolder(
        parent: ViewGroup,
        position: Int,
        layoutInflater: LayoutInflater
    ): VH {
        val (layout, itemPosition) = getLayoutAndItemPosition(position)
        val itemView = layoutInflater.inflate(layout.getLayoutRes(itemPosition), parent, false)
        return ViewHolder(itemView, layout as Layout<ViewHolder>) as VH
    }

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.context)
        val holder = instantiateViewHolder(parent, viewType, layoutInflater!!)
        holder.onCreateViewHolder(recyclerView!!)
        return holder
    }

    override fun getItemCount(): Int {
        return /*headers.sumBy {
            it.getItemCount()
        } + */layout.getItemCount() + footers.sumBy { it.getItemCount() }
    }

    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBindViewHolder()
    }

    override fun onViewRecycled(holder: VH) {
        if (holder.adapterPosition != RecyclerView.NO_POSITION) {
            holder.onViewRecycled(recyclerView!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        val (layout, itemPosition) = getLayoutAndItemPosition(position)
        return layout.getItemId(itemPosition)
    }

    final override fun onAttachedToRecyclerView(view: RecyclerView) {
        if (recyclerView == null) {
            if (items is ObservableList) items.addCallback(callback)
            recyclerView = view
            layoutInflater = LayoutInflater.from(view.context)
            onAttachListener?.invoke(view)
//            headers.forEach { it.onAttach(view) }
            footers.forEach { it.onAttach(view) }
        }
    }

    final override fun onDetachedFromRecyclerView(view: RecyclerView) {
        if (recyclerView != null) {
            if (items is ObservableList) items.removeCallback(callback)
            onDetachListener?.invoke(view)
//            headers.forEach { it.onDetach(view) }
            footers.forEach { it.onDetach(view) }
            recyclerView = null
            layoutInflater = null
        }
    }

    private fun getItem(position: Int): E? {
        val itemPosition = position/* - headers.sumBy { it.getItemCount() }*/
        if (itemPosition < 0 || itemPosition >= items.size) return null
        return items[itemPosition]
    }

    private fun getItemAndItemPosition(position: Int): Pair<E?, Int> {
        val itemPosition = position/* - headers.sumBy { it.getItemCount() }*/
        if (itemPosition < 0 || itemPosition >= items.size) return null to itemPosition
        return items[itemPosition] to itemPosition
    }

    private fun getItemPosition(position: Int): Int {
        if (itemCount == 0) {
            return 0
        }

        var count = position
//        headers.forEach {
//            if (count == 0) return count
//            val itemCount = it.getItemCount()
//            if (count < itemCount) return count
//            count -= itemCount
//        }
        if (count < items.size) {
            return count
        }
        count -= items.size
        footers.forEach {
            if (count == 0) return count
            val itemCount = it.getItemCount()
            if (count < itemCount) return count
            count -= itemCount
        }
        return RecyclerView.NO_POSITION
    }

    private fun getLayout(position: Int): Layout<VH> {
        if (position < 0) throw RuntimeException()
        var count = position
//        headers.forEach {
//            val layout = it
//            if (count == 0) return layout
//            val itemCount = layout.getItemCount()
//            if (count < itemCount) return layout
//            count -= itemCount
//        }
        if (count < items.size) {
            return layout
        }
        count -= items.size
        footers.forEach {
            val layout = it
            if (count == 0) return layout
            val itemCount = layout.getItemCount()
            if (count < itemCount) return layout
            count -= itemCount
        }
        throw RuntimeException()
    }

    private fun getLayoutAndItemPosition(position: Int): Pair<Layout<VH>, Int> {
        if (position < 0) throw RuntimeException()
        var count = position
    //        headers.forEach {
    //            val layout = it
    //            if (count == 0) return layout to count
    //            val itemCount = layout.getItemCount()
    //            if (count < itemCount) return layout to count
    //            count -= itemCount
    //        }
        if (count < items.size) {
            return layout to count
        }
        count -= items.size
        footers.forEach {
            val layout = it
            if (count == 0) return layout to count
            val itemCount = layout.getItemCount()
            if (count < itemCount) return layout to count
            count -= itemCount
        }
        throw RuntimeException()
    }

    internal fun anyHasStableIds(): Boolean {
        return stableIdProvider != null || map.any {
            it.value.stableIdProvider != null
        }
    }

    internal inner class ItemLayout : Layout<VH>() {

        private fun getItem(position: Int): E {
            return items[position]
        }

        internal fun <VH : ViewHolder> getItem(holder: VH): E =
            this@Adapter.getItem(holder.adapterPosition)!!

        internal fun <VH : ViewHolder> getItemPosition(holder: VH): Int =
            this@Adapter.getItemPosition(holder.adapterPosition)

        override fun getLayoutRes(position: Int): Int {
            val item = getItem(position)
            return layoutProvider?.invoke(item, position)
                ?: map[item.javaClass]?.layoutRes
                ?: throw RuntimeException("Could not resolve an item layout.")
        }

        override fun getItemId(position: Int): Long {
            val item = getItem(position)
            return stableIdProvider?.invoke(item, position)
                ?: map[item.javaClass]?.stableIdProvider?.invoke(
                    item,
                    position
                )
                ?: super.getItemId(position)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBind(holder: VH) {
            val (item, itemPosition) = this@Adapter.getItemAndItemPosition(holder.adapterPosition)
            val mapper = map[item!!.javaClass]
            mapper?.eventHooks?.forEach {
                it.attach(
                    holder, item, itemPosition
                )
            }
            eventHooks.forEach { it.attach(holder, item, itemPosition) }
            onBindListener?.invoke(holder, item, itemPosition)
                ?: mapper?.onBindListener?.invoke(
                    holder, item, itemPosition
                )
        }

        override fun onRecycle(holder: VH) {
            val (item, itemPosition) = this@Adapter.getItemAndItemPosition(holder.adapterPosition)
            onRecycleListener?.invoke(holder, item!!, itemPosition)
                ?: map[item!!.javaClass]?.onRecycleListener?.invoke(
                    holder, item, itemPosition
                )
        }

    }

}

internal fun <E : Any> adapterOf(): Adapter<E, ViewHolder> = Adapter()
internal fun <E : Any> adapterOf(vararg elements: E): Adapter<E, ViewHolder> = Adapter(elements)

internal fun <E : Any> Array<out E>.toAdapter(): Adapter<E, ViewHolder> = Adapter(this)
internal fun <E : Any> MutableList<E>.toAdapter(): Adapter<E, ViewHolder> = Adapter(this)