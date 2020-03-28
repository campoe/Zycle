package com.campoe.android.zycle.adapter

import android.view.*
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.ZycleDsl
import com.campoe.android.zycle.mapper.Mapper
import com.campoe.android.zycle.mapper.TypeErasedMapper
import com.campoe.android.zycle.mapper.mapper
import com.campoe.android.zycle.observablelist.ObservableList
import com.campoe.android.zycle.util.cast
import com.campoe.android.zycle.viewholder.IViewHolder
import com.campoe.android.zycle.viewholder.ViewHolder

@ZycleDsl
open class Adapter<E : Any, VH : ViewHolder<E>>(internal val items: MutableList<E> = mutableListOf()) :
    RecyclerView.Adapter<VH>(), IAdapter<E, VH> {

    constructor(items: Array<out E>) : this(items.toMutableList())

    @Suppress("LeakingThis")
    protected var callback: ObservableList.ObservableListCallback<E, VH> =
        ObservableList.ObservableListCallback(this)

    private val map: MutableMap<Class<*>, TypeErasedMapper> = mutableMapOf()

    private var layoutProvider: ((E, Int) -> Int)? = null
    private var stableIdProvider: ((E, Int) -> Long)? = null
        set(value) {
            setHasStableIds(true)
            field = value
        }

    private var onAttachListener: ((RecyclerView) -> Unit)? = null
    private var onDetachListener: ((RecyclerView) -> Unit)? = null

    private var onBindListener: (VH.() -> Unit)? = null
    private var onRecycleListener: (VH.() -> Unit)? = null

    private var layoutInflater: LayoutInflater? = null
    private var recyclerView: RecyclerView? = null
    private val listeners: MutableList<IViewHolder.ViewHolderListener<VH>> = mutableListOf()

    override fun attach(view: RecyclerView) {
        view.adapter = this
    }

    override fun detach() {
        recyclerView?.adapter = null
    }

    internal fun onAttach(f: (RecyclerView) -> Unit): IAdapter<E, VH> =
        apply { onAttachListener = f }

    internal fun onDetach(f: (RecyclerView) -> Unit): IAdapter<E, VH> =
        apply { onDetachListener = f }

    override fun <T : E> map(
        clazz: Class<T>,
        layoutRes: Int,
        block: (Mapper<T, ViewHolder<T>>.() -> Unit)?
    ): IAdapter<E, VH> =
        apply {
            map[clazz] = mapper<T>(layoutRes)
                .apply { block?.invoke(this) }
        }

    inline fun <reified T : E> map(
        @LayoutRes layoutRes: Int
    ) =
        map(T::class.java, layoutRes)

    inline fun <reified T : E> map(
        @LayoutRes layoutRes: Int,
        noinline block: (Mapper<T, ViewHolder<T>>.() -> Unit)
    ) =
        map(T::class.java, layoutRes, block)

    override fun layout(f: (item: E, position: Int) -> Int) =
        apply { layoutProvider = f }

    override fun stableId(f: (item: E, position: Int) -> Long): IAdapter<E, VH> =
        apply { stableIdProvider = f }

    override fun onBind(f: VH.() -> Unit): IAdapter<E, VH> =
        apply { onBindListener = f }

    override fun onRecycle(f: VH.() -> Unit): IAdapter<E, VH> =
        apply { onRecycleListener = f }

    override fun listen(listener: IViewHolder.ViewHolderListener<VH>) =
        apply { listeners.add(listener) }

    inline fun onItemClick(crossinline f: (VH) -> Unit) =
        apply {
            listen(
                object : IViewHolder.OnItemClickListener<VH> {
                    override fun onItemClick(holder: VH) = f(holder)
                }
            )
        }

    inline fun onItemLongClick(crossinline f: (VH) -> Boolean) =
        apply {
            listen(
                object : IViewHolder.OnItemLongClickListener<VH> {
                    override fun onItemLongClick(holder: VH): Boolean = f(holder)
                }
            )
        }

    inline fun onItemTouch(crossinline f: (VH, MotionEvent) -> Boolean) =
        apply {
            listen(
                object : IViewHolder.OnItemTouchListener<VH> {
                    override fun onItemTouch(holder: VH, e: MotionEvent): Boolean =
                        f(holder, e)
                }
            )
        }

    inline fun onItemDrag(crossinline f: (VH, DragEvent) -> Boolean) =
        apply {
            listen(
                object : IViewHolder.OnItemDragListener<VH> {
                    override fun onItemDrag(
                        holder: VH,
                        e: DragEvent
                    ): Boolean =
                        f(holder, e)
                }
            )
        }

    inline fun onItemHover(crossinline f: (VH, MotionEvent) -> Boolean) =
        apply {
            listen(
                object : IViewHolder.OnItemHoverListener<VH> {
                    override fun onItemHover(
                        holder: VH,
                        e: MotionEvent
                    ): Boolean =
                        f(holder, e)
                }
            )
        }

    @Suppress("UNCHECKED_CAST")
    private fun instantiateViewHolder(
        parent: ViewGroup,
        viewType: Int,
        itemView: View
    ): VH {
        return ViewHolder<E>(itemView) as VH
    }

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater!!.inflate(viewType, parent, false)
        return instantiateViewHolder(parent, viewType, view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @Suppress("UNCHECKED_CAST")
    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = get(position)
        val listeners = listeners.toMutableList()
        getMapper(position)?.also { mapper ->
            listeners.apply {
                addAll(mapper.listeners)
                val tmp = listeners.distinctBy { l -> l.javaClass }
                listeners.clear()
                listeners.addAll(tmp)
            }
        }
        holder.bind(
            item,
            listeners.toList() as List<IViewHolder.ViewHolderListener<IViewHolder<E>>> // TODO: set listeners in create
        )
        doOnBind(holder)
    }

    override fun onViewRecycled(holder: VH) {
        val position = holder.adapterPosition
        if (position != RecyclerView.NO_POSITION && position < items.size) {
            doOnRecycle(holder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return layoutProvider?.invoke(get(position), position)
            ?: getMapper(position)?.layoutRes
            ?: throw RuntimeException("Could not resolve an item layout.")
    }

    override fun getItemId(position: Int): Long {
        val item = get(position)
        return stableIdProvider?.invoke(item, position)
            ?: getMapper(position)?.stableIdProvider?.invoke(item, position)
            ?: super.getItemId(position)
    }

    override fun onAttachedToRecyclerView(view: RecyclerView) {
        if (recyclerView == null) {
            if (items is ObservableList) items.addCallback(callback)
            recyclerView = view
            layoutInflater = LayoutInflater.from(view.context)
            onAttachListener?.invoke(view)
        }
    }

    override fun onDetachedFromRecyclerView(view: RecyclerView) {
        if (recyclerView != null) {
            if (items is ObservableList) items.removeCallback(callback)
            onDetachListener?.invoke(view)
            recyclerView = null
            layoutInflater = null
        }
    }

    internal fun anyHasStableIds(): Boolean {
        return map.any {
            val mapper = it.value
            if (mapper is Mapper<*, *>) {
                mapper.stableIdProvider != null
            }
            false
        }
    }

    private fun doOnBind(holder: VH) {
        onBindListener?.invoke(holder)
            ?: map[holder.item!!.javaClass]?.cast<Mapper<*, VH>>()?.onBindListener?.invoke(holder)
            ?: throw RuntimeException("Could not resolve an item layout.")
    }

    private fun doOnRecycle(holder: VH) {
        onRecycleListener?.invoke(holder)
            ?: map[holder.item!!.javaClass]?.cast<Mapper<*, VH>>()?.onRecycleListener?.invoke(holder)
            ?: throw RuntimeException("Could not resolve an item layout.")
    }

    private fun getMapper(position: Int): Mapper<E, VH>? {
        return map[get(position).javaClass]?.cast()
    }

    private fun get(position: Int): E {
        return items[position]
    }

}

fun <E : Any> adapterOf(): Adapter<E, ViewHolder<E>> = Adapter()
fun <E : Any> adapterOf(vararg elements: E): Adapter<E, ViewHolder<E>> = Adapter(elements)

fun <E : Any> Array<out E>.toAdapter(): Adapter<E, ViewHolder<E>> = Adapter(this)
fun <E : Any> MutableList<E>.toAdapter(): Adapter<E, ViewHolder<E>> = Adapter(this)