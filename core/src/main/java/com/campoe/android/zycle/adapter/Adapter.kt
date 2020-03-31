package com.campoe.android.zycle.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.ZycleDsl
import com.campoe.android.zycle.`typealias`.*
import com.campoe.android.zycle.extension.cast
import com.campoe.android.zycle.mapper.Mapper
import com.campoe.android.zycle.mapper.TypeErasedMapper
import com.campoe.android.zycle.mapper.mapper
import com.campoe.android.zycle.observablelist.ObservableList
import com.campoe.android.zycle.viewholder.ViewHolder

@ZycleDsl
open class Adapter<E : Any, VH : ViewHolder<E>>(internal val items: MutableList<E> = mutableListOf()) :
    RecyclerView.Adapter<VH>(), IAdapter<E, VH> {

    constructor(items: Array<out E>) : this(items.toMutableList())

    @Suppress("LeakingThis")
    private var callback: ObservableList.ObservableListCallback<E, VH> =
        ObservableList.ObservableListCallback(this)

    private val map: MutableMap<Class<*>, TypeErasedMapper> = mutableMapOf()

    private var layoutProvider: LayoutProvider<E>? = null
    private var stableIdProvider: StableIdProvider<E>? = null
        set(value) {
            setHasStableIds(true)
            field = value
        }

    private var onAttachListener: OnAttachListener? = null
    private var onDetachListener: OnDetachListener? = null

    private var onBindListener: OnBindListener<VH>? = null
    private var onRecycleListener: OnRecycleListener<VH>? = null

    private var onClickListener: OnItemClickListener<VH>? = null
    private var onLongClickListener: OnItemLongClickListener<VH>? = null
    private var onTouchListener: OnItemTouchListener<VH>? = null

    private var layoutInflater: LayoutInflater? = null
    protected var recyclerView: RecyclerView? = null

    override fun attach(view: RecyclerView) =
        apply { view.adapter = this }

    override fun detach() =
        apply { recyclerView?.adapter = null }

    internal fun onAttach(f: OnAttachListener): IAdapter<E, VH> =
        apply { onAttachListener = f }

    internal fun onDetach(f: OnDetachListener): IAdapter<E, VH> =
        apply { onDetachListener = f }

    fun <T : E> map(clazz: Class<T>, mapper: Mapper<T, ViewHolder<T>>) =
        apply { map[clazz] = mapper }

    inline fun <reified T : E> map(mapper: Mapper<T, ViewHolder<T>>) =
        map(T::class.java, mapper)

    override fun <T : E> map(
        clazz: Class<T>,
        @LayoutRes
        layoutRes: Int,
        block: MapperBlock<T>?
    ): Adapter<E, VH> =
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
        noinline block: MapperBlock<T>
    ) =
        map(T::class.java, layoutRes, block)

    override fun layout(f: LayoutProvider<E>) =
        apply { layoutProvider = f }

    override fun stableId(f: StableIdProvider<E>): Adapter<E, VH> =
        apply { stableIdProvider = f }

    override fun onBind(f: OnBindListener<VH>): Adapter<E, VH> =
        apply { onBindListener = f }

    override fun onRecycle(f: OnRecycleListener<VH>): Adapter<E, VH> =
        apply { onRecycleListener = f }

    override fun onClick(f: OnItemClickListener<VH>): Adapter<E, VH> =
        apply { onClickListener = f }

    override fun onLongClick(f: OnItemLongClickListener<VH>): Adapter<E, VH> =
        apply { onLongClickListener = f }

    override fun onTouch(f: OnItemTouchListener<VH>): Adapter<E, VH> =
        apply { onTouchListener = f }

    @Suppress("UNCHECKED_CAST")
    protected open fun instantiateViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ): VH {
        val itemView = layoutInflater.inflate(viewType, parent, false)
        return ViewHolder<E>(itemView) as VH
    }

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.context)
        return instantiateViewHolder(parent, viewType, layoutInflater!!)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @Suppress("UNCHECKED_CAST")
    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = get(position)
        holder.bind(
            item, // TODO: set listeners in create
            onClick = { this@Adapter.doOnClick(this as VH) },
            onLongClick = { this@Adapter.doOnLongClick(this as VH) },
            onTouch = { e -> this@Adapter.doOnTouch(this as VH, e) }
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

    final override fun onAttachedToRecyclerView(view: RecyclerView) {
        if (recyclerView == null) {
            if (items is ObservableList) items.addCallback(callback)
            recyclerView = view
            layoutInflater = LayoutInflater.from(view.context)
            onAttachListener?.invoke(view)
        }
    }

    final override fun onDetachedFromRecyclerView(view: RecyclerView) {
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
            ?: map[holder.item!!.javaClass]?.cast<Mapper<*, VH>>()?.internalOnBindListener?.invoke(
                holder
            )
            ?: throw RuntimeException("Could not resolve an item layout.") // TODO: error message
    }

    private fun doOnRecycle(holder: VH) {
        onRecycleListener?.invoke(holder)
            ?: map[holder.item!!.javaClass]?.cast<Mapper<*, VH>>()?.onRecycleListener?.invoke(holder)
            ?: throw RuntimeException("Could not resolve an item layout.")
    }

    private fun getMapper(position: Int): Mapper<E, VH>? {
        return map[get(position).javaClass]?.cast()
    }

    protected fun get(position: Int): E {
        return items[position]
    }

    private fun doOnClick(holder: VH) {
        onClickListener?.invoke(holder)
        map[holder.item!!.javaClass]?.cast<Mapper<*, VH>>()?.onClickListener?.invoke(holder)
    }

    private fun doOnLongClick(holder: VH): Boolean {
        return onLongClickListener?.invoke(holder) ?: false ||
                map[holder.item!!.javaClass]?.cast<Mapper<*, VH>>()?.onLongClickListener?.invoke(
                    holder
                ) ?: false
    }

    private fun doOnTouch(holder: VH, e: MotionEvent): Boolean {
        return onTouchListener?.invoke(holder, e) ?: false ||
                map[holder.item!!.javaClass]?.cast<Mapper<*, VH>>()?.onTouchListener?.invoke(
                    holder,
                    e
                ) ?: false
    }

}

internal fun <E : Any> adapterOf(): Adapter<E, ViewHolder<E>> = Adapter()
internal fun <E : Any> adapterOf(vararg elements: E): Adapter<E, ViewHolder<E>> = Adapter(elements)

internal fun <E : Any> Array<out E>.toAdapter(): Adapter<E, ViewHolder<E>> = Adapter(this)
internal fun <E : Any> MutableList<E>.toAdapter(): Adapter<E, ViewHolder<E>> = Adapter(this)