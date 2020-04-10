package com.campoe.android.zycle.adapter

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.`typealias`.AdapterBlock
import com.campoe.android.zycle.`typealias`.AdapterBuilderBlock
import com.campoe.android.zycle.`typealias`.BinderBuilderBlock
import com.campoe.android.zycle.`typealias`.MapperBuilderBlock
import com.campoe.android.zycle.adapter.observer.AdapterDataObserver
import com.campoe.android.zycle.adapter.transformer.Transformer
import com.campoe.android.zycle.binder.RecyclerBinder
import com.campoe.android.zycle.condition.Condition
import com.campoe.android.zycle.mapper.Mapper
import com.campoe.android.zycle.util.Builder

internal interface IAdapter {

    val hasStableIds: Boolean

    fun attach(recyclerView: RecyclerView): Adapter

    @LayoutRes
    fun getLayoutRes(viewType: Int): Int
    fun getItemCount(): Int
    fun getItemId(position: Int): Long
    fun getItemViewType(position: Int): Int
    fun isEnabled(position: Int): Boolean

    fun registerAdapterDataObserver(observer: AdapterDataObserver)
    fun unregisterAdapterDataObserver(observer: AdapterDataObserver)
    fun hasObservers(): Boolean
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>)
    fun onViewRecycled(holder: RecyclerView.ViewHolder)
    fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean
    fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder)
    fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder)
    fun onAttachedToRecyclerView(recyclerView: RecyclerView)
    fun onDetachedFromRecyclerView(recyclerView: RecyclerView)
    fun notifyDataSetChanged()
    fun notifyItemChanged(position: Int)
    fun notifyItemChanged(position: Int, payload: Any?)
    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int)
    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?)
    fun notifyItemInserted(position: Int)
    fun notifyItemMoved(fromPosition: Int, toPosition: Int)
    fun notifyItemRangeInserted(positionStart: Int, itemCount: Int)
    fun notifyItemRemoved(position: Int)
    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int)

    fun compose(transformer: Transformer): Adapter
    fun flatten(): Adapter
    fun prepend(adapter: Adapter): Adapter
    fun prepend(vararg adapters: Adapter): Adapter
    fun append(adapter: Adapter): Adapter
    fun append(vararg adapters: Adapter): Adapter
    fun showIf(condition: Condition): Adapter
    fun showIfElse(condition: Condition, @LayoutRes layoutRes: Int): Adapter

    interface IBuilder : Builder<Adapter> {

        fun flatten(): Adapter.Builder
        fun prepend(adapter: Adapter): Adapter.Builder
        fun prepend(vararg adapters: Adapter): Adapter.Builder
        fun append(adapter: Adapter): Adapter.Builder
        fun append(vararg adapters: Adapter): Adapter.Builder
        fun postBuild(block: AdapterBlock): Adapter.Builder

        fun viewsOf(@LayoutRes vararg layouts: Int): Adapter.Builder

        fun adapterOf(block: AdapterBuilderBlock): Adapter.Builder

        fun <E : Any> adapterOf(items: List<E>, mapper: Mapper<E>): Adapter.Builder

        fun <E : Any> adapterOf(items: List<E>, block: MapperBuilderBlock<E>): Adapter.Builder

        fun <E : Any> adapterOf(items: List<E>, binder: RecyclerBinder<E>): Adapter.Builder

        fun <E : Any> adapterOf(
            items: List<E>,
            @LayoutRes
            layoutRes: Int,
            block: BinderBuilderBlock<E>
        ): Adapter.Builder

        fun <E : Any> adapterOf(
            items: List<E>,
            @LayoutRes
            layoutRes: Int,
            @IdRes
            viewType: Int,
            block: BinderBuilderBlock<E>
        ): Adapter.Builder

    }

}