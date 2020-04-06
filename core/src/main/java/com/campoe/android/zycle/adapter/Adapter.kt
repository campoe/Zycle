package com.campoe.android.zycle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.ZycleDsl
import com.campoe.android.zycle.`typealias`.*
import com.campoe.android.zycle.eventhook.EventHook
import com.campoe.android.zycle.eventhook.Hookable
import com.campoe.android.zycle.eventhook.attachEvents
import com.campoe.android.zycle.extension.AdapterExtension
import com.campoe.android.zycle.extension.Extendable
import com.campoe.android.zycle.ktx.cast
import com.campoe.android.zycle.layout.Item
import com.campoe.android.zycle.layout.RecyclerItem
import com.campoe.android.zycle.observablelist.ObservableList
import com.campoe.android.zycle.viewholder.ViewHolder

abstract class Adapter<E : Any> internal constructor(
    internal val items: MutableList<E> = mutableListOf(),
    private val extensionPoints: MutableList<AdapterExtension> = mutableListOf()
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), IAdapter<E> {

    constructor(items: MutableList<E>) : this(items, mutableListOf())
    constructor(items: Array<out E>) : this(items.toMutableList())

    @Suppress("LeakingThis")
    protected var callback: ObservableList.ObservableListCallback<E> =
        ObservableList.ObservableListCallback(this)

    protected var layoutInflater: LayoutInflater? = null
    protected var recyclerView: RecyclerView? = null

    override fun attach(view: RecyclerView) =
        apply { view.adapter = this }

    override fun detach() =
        apply { recyclerView?.adapter = null }

    override fun getItemCount(): Int {
        return items.size
    }

    internal fun extendWith(onAttach: OnAttachListener, onDetach: OnDetachListener) {
        extensionPoints.add(object : AdapterExtension() {
            override fun onAttach(recyclerView: RecyclerView) = onAttach(recyclerView)
            override fun onDetach(recyclerView: RecyclerView) = onDetach(recyclerView)
        })
    }

    final override fun onAttachedToRecyclerView(view: RecyclerView) {
        if (recyclerView == null) {
            if (items is ObservableList) items.addCallback(callback)
            recyclerView = view
            layoutInflater = LayoutInflater.from(view.context)
            extensionPoints.forEach { it.onAttach(view) }
        }
    }

    final override fun onDetachedFromRecyclerView(view: RecyclerView) {
        if (recyclerView != null) {
            if (items is ObservableList) items.removeCallback(callback)
            extensionPoints.forEach { it.onDetach(view) }
            recyclerView = null
            layoutInflater = null
        }
    }

    protected fun getItem(position: Int): E? {
        if (position < 0 || position >= items.size) return null
        return items[position]
    }

    @ZycleDsl
    class Builder<E : Any> internal constructor() : IAdapter.IBuilder<E>,
        Hookable<E, RecyclerView.ViewHolder>, Extendable {

        private var viewTypeProvider: ViewTypeProvider<E>? = null
        private var layoutProvider: LayoutProvider? = null
        private var stableIdProvider: StableIdProvider<E>? = null

        private var onCreateListener: OnCreateListener<RecyclerView.ViewHolder>? = null
        private var onBindListener: OnBindListener<E, RecyclerView.ViewHolder>? = null
        private var onRecycleListener: OnRecycleListener<RecyclerView.ViewHolder>? = null

        private val map: MutableMap<Class<*>, RecyclerItem<E>> = mutableMapOf()

        private val extensionPoints: MutableList<AdapterExtension> = mutableListOf()
        override val eventHooks: MutableList<EventHook<E, RecyclerView.ViewHolder>> =
            mutableListOf()

        override fun extendWith(extensionPoint: AdapterExtension) =
            apply { extensionPoints.add(extensionPoint) }

        @Suppress("UNCHECKED_CAST")
        fun <T : E> map(clazz: Class<T>, item: RecyclerItem<T>) =
            apply { map[clazz] = item as RecyclerItem<E> }

        inline fun <reified T : E> map(item: RecyclerItem<T>) =
            map(T::class.java, item)

        @Suppress("UNCHECKED_CAST")
        override fun <T : E> map(
            clazz: Class<T>,
            @LayoutRes
            layoutRes: Int,
            viewType: Int,
            block: ItemBuilderBlock<T>?
        ): Builder<E> =
            apply {
                map[clazz] = Item.Builder<T>(layoutRes, viewType)
                    .apply { block?.invoke(this) }.build() as RecyclerItem<E>
            }

        @Suppress("UNCHECKED_CAST")
        override fun <T : E> map(
            clazz: Class<T>,
            @LayoutRes
            layoutRes: Int,
            block: ItemBuilderBlock<T>?
        ): Builder<E> =
            apply {
                map[clazz] =
                    Item.Builder<T>(
                            layoutRes,
                            clazz.name.hashCode()
                        )
                        .apply { block?.invoke(this) }.build() as RecyclerItem<E>
            }

        inline fun <reified T : E> map(
            @LayoutRes layoutRes: Int
        ) =
            map(T::class.java, layoutRes)

        inline fun <reified T : E> map(
            @LayoutRes layoutRes: Int,
            viewType: Int
        ) =
            map(T::class.java, layoutRes, viewType)

        inline fun <reified T : E> map(
            @LayoutRes layoutRes: Int,
            noinline block: ItemBuilderBlock<T>
        ) =
            map(T::class.java, layoutRes, block)

        inline fun <reified T : E> map(
            @LayoutRes layoutRes: Int,
            viewType: Int,
            noinline block: ItemBuilderBlock<T>
        ) =
            map(T::class.java, layoutRes, viewType, block)

        override fun viewType(f: ViewTypeProvider<E>): Builder<E> =
            apply { viewTypeProvider = f }

        override fun layout(f: LayoutProvider): Builder<E> =
            apply { layoutProvider = f }

        override fun stableId(f: StableIdProvider<E>): Builder<E> =
            apply { stableIdProvider = f }

        override fun onCreate(f: OnCreateListener<RecyclerView.ViewHolder>): Builder<E> =
            apply { onCreateListener = f }

        override fun onBind(f: OnBindListener<E, RecyclerView.ViewHolder>): Builder<E> =
            apply { onBindListener = f }

        override fun onRecycle(f: OnRecycleListener<RecyclerView.ViewHolder>): Builder<E> =
            apply { onRecycleListener = f }

        override fun build(): Adapter<E> =
            build(mutableListOf())

        fun build(items: Array<out E>): Adapter<E> = build(items.toMutableList())

        fun build(items: MutableList<E>): Adapter<E> {
            return object : Adapter<E>(items, extensionPoints) {
                init {
                    setHasStableIds(hasStableIds() || map.any {
                        it.value.hasStableIds()
                    })
                }

                private val viewTypes: SparseArrayCompat<Class<*>> = SparseArrayCompat(1)

                override fun getStableId(item: E, position: Int): Long {
                    return stableIdProvider?.invoke(item, position)
                        ?: map[item.javaClass]?.getStableId(item, position)
                        ?: throw RuntimeException()
                }

                override fun getLayoutRes(viewType: Int): Int {
                    return layoutProvider?.invoke(viewType)
                        ?: map[viewTypes[viewType]]?.layoutRes
                        ?: throw RuntimeException()
                }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder {
                    if (layoutInflater == null) layoutInflater =
                        LayoutInflater.from(parent.context) // TODO: create in onAttach
                    val clazz = viewTypes[viewType]
                    val item = map[clazz]
                    val layoutRes = layoutProvider?.invoke(viewType)
                        ?: item?.layoutRes ?: throw RuntimeException()
                    val itemView = layoutInflater!!.inflate(layoutRes, parent, false)
                    val holder = ViewHolder(itemView)
                    onCreateListener?.invoke(holder)
                        ?: item?.onCreate(holder)
                    holder.cast<ViewHolder>()?.also {
                        it.onCreateViewHolder(recyclerView!!)
                    }
                    return holder
                }

                override fun onBindViewHolder(
                    holder: RecyclerView.ViewHolder,
                    position: Int
                ) {
                    val item = getItem(position) ?: throw RuntimeException()
                    attachEvents(holder, item, holder.adapterPosition)
                    val mapper = map[viewTypes[holder.itemViewType]]
                    mapper?.attachEvents(holder, item, holder.adapterPosition)
                    onBindListener?.invoke(holder, item)
                        ?: mapper?.onBind(
                            holder,
                            item
                        )
                    holder.cast<ViewHolder>()?.also {
                        it.onBindViewHolder()
                    }
                }

                override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
                    if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                        onRecycleListener?.invoke(holder)
                            ?: map[viewTypes[holder.itemViewType]]?.onRecycle(holder)
                        holder.cast<ViewHolder>()?.also {
                            it.onViewRecycled(recyclerView!!)
                        }
                    }
                }

                override fun getItemViewType(position: Int): Int {
                    val item = getItem(position) ?: throw RuntimeException()
                    val clazz = item.javaClass
                    val viewType = viewTypeProvider?.invoke(item, position)
                        ?: map[clazz]?.viewType ?: super.getItemViewType(position)
                    if (!viewTypes.containsKey(viewType)) {
                        viewTypes.put(viewType, clazz)
                    }
                    return viewType
                }

                override fun getItemId(position: Int): Long {
                    val item = getItem(position) ?: return super.getItemId(position)
                    return stableIdProvider?.invoke(item, position)
                        ?: map[item.javaClass]?.getStableId(item, position)
                        ?: super.getItemId(position)
                }
            }
        }

    }

}