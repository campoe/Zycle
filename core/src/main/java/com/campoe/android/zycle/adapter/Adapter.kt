package com.campoe.android.zycle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.ZycleDsl
import com.campoe.android.zycle.`typealias`.AdapterBlock
import com.campoe.android.zycle.`typealias`.AdapterBuilderBlock
import com.campoe.android.zycle.`typealias`.BinderBuilderBlock
import com.campoe.android.zycle.`typealias`.MapperBuilderBlock
import com.campoe.android.zycle.adapter.composite.CompositeAdapter
import com.campoe.android.zycle.adapter.conditional.ConditionalAdapter
import com.campoe.android.zycle.adapter.conditional.ConditionalElseAdapter
import com.campoe.android.zycle.adapter.observer.AdapterDataObservable
import com.campoe.android.zycle.adapter.observer.AdapterDataObserver
import com.campoe.android.zycle.adapter.transformer.Transformer
import com.campoe.android.zycle.binder.Binder
import com.campoe.android.zycle.binder.RecyclerBinder
import com.campoe.android.zycle.condition.Condition
import com.campoe.android.zycle.mapper.Mapper

abstract class Adapter : IAdapter {

    private val observable: AdapterDataObservable =
        AdapterDataObservable()

    protected var layoutInflater: LayoutInflater? = null
        private set

    override val hasStableIds: Boolean = false

    abstract override fun getItemCount(): Int
    abstract override fun getItemViewType(position: Int): Int

    final override fun attach(recyclerView: RecyclerView) =
        apply { recyclerView.adapter = RecyclerAdapter(this) }

    override fun isEnabled(position: Int): Boolean = true
    override fun flatten(): Adapter = this

    override fun compose(transformer: Transformer): Adapter =
        transformer.transform(this)

    protected open fun onFirstObserverRegistered() = Unit
    protected open fun onLastObserverUnregistered() = Unit

    abstract override fun getLayoutRes(viewType: Int): Int

    override fun getItemId(position: Int): Long = RecyclerView.NO_ID

    abstract override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder

    abstract override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        onBindViewHolder(holder, position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) = Unit

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean = false

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) = Unit

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) = Unit

    final override fun hasObservers(): Boolean = observable.hasObservers()

    final override fun notifyDataSetChanged() = observable.notifyChanged()

    final override fun notifyItemChanged(position: Int) =
        observable.notifyItemRangeChanged(position, 1)

    final override fun notifyItemChanged(position: Int, payload: Any?) =
        observable.notifyItemRangeChanged(position, 1, payload)

    final override fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) =
        observable.notifyItemRangeChanged(positionStart, itemCount)

    final override fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) =
        observable.notifyItemRangeChanged(positionStart, itemCount, payload)

    final override fun notifyItemInserted(position: Int) =
        observable.notifyItemRangeInserted(position, 1)

    final override fun notifyItemMoved(fromPosition: Int, toPosition: Int) =
        observable.notifyItemRangeMoved(fromPosition, toPosition, 1)

    final override fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) =
        observable.notifyItemRangeInserted(positionStart, itemCount)

    final override fun notifyItemRemoved(position: Int) =
        observable.notifyItemRangeRemoved(position, 1)

    final override fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) =
        observable.notifyItemRangeRemoved(positionStart, itemCount)

    @CallSuper
    override fun registerAdapterDataObserver(observer: AdapterDataObserver) {
        val hasObservers = hasObservers()
        observable.registerObserver(observer)
        if (!hasObservers) {
            onFirstObserverRegistered()
        }
    }

    @CallSuper
    override fun unregisterAdapterDataObserver(observer: AdapterDataObserver) {
        observable.unregisterObserver(observer)
        if (!hasObservers()) {
            onLastObserverUnregistered()
        }
    }

    fun notifyItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        for (i in itemCount - 1 downTo 0) notifyItemMoved(
            fromPosition + i,
            toPosition + i
        )
    }

    final override fun prepend(adapter: Adapter): Adapter {
        return CompositeAdapter(
            adapter,
            this
        )
    }

    final override fun append(adapter: Adapter): Adapter {
        return CompositeAdapter(
            this,
            adapter
        )
    }

    final override fun prepend(vararg adapters: Adapter): Adapter {
        return if (adapters.isEmpty()) this
        else CompositeAdapter(
            *adapters,
            this
        )
    }

    final override fun append(vararg adapters: Adapter): Adapter {
        return if (adapters.isEmpty()) this
        else CompositeAdapter(
            this,
            *adapters
        )
    }

    final override fun showIf(condition: Condition): Adapter =
        ConditionalAdapter(this, condition)

    final override fun showIfElse(condition: Condition, layoutRes: Int): Adapter =
        ConditionalElseAdapter(this, condition, layoutRes)

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        layoutInflater = LayoutInflater.from(recyclerView.context)
    }

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        layoutInflater = null
    }

    @ZycleDsl
    class Builder private constructor(private var compositeBuilder: CompositeAdapter.Builder) :
        IAdapter.IBuilder {

        constructor() : this(CompositeAdapter.Builder())

        override fun prepend(adapter: Adapter) =
            apply { compositeBuilder.prepend(adapter) }

        override fun prepend(vararg adapters: Adapter) =
            apply { compositeBuilder.prepend(*adapters) }

        override fun append(adapter: Adapter) =
            apply { compositeBuilder.append(adapter) }

        override fun append(vararg adapters: Adapter) =
            apply { compositeBuilder.append(*adapters) }

        override fun flatten() =
            apply { compositeBuilder.flatten() }

        override fun postBuild(block: AdapterBlock): Builder =
            apply { compositeBuilder.postBuild(block) }

        override fun viewsOf(@LayoutRes vararg layouts: Int) =
            append(viewAdapterOf(*layouts))

        override fun adapterOf(
            block: AdapterBuilderBlock
        ) =
            append(Builder().apply(block).build())

        override fun <E : Any> adapterOf(items: List<E>, mapper: Mapper<E>): Builder =
            append(itemAdapterOf(items, mapper))

        override fun <E : Any> adapterOf(items: List<E>, block: MapperBuilderBlock<E>): Builder {
            val mapperBuilder = Mapper.Builder<E>()
            return append(itemAdapterOf(items, mapperBuilder.apply(block).build()))
        }

        override fun <E : Any> adapterOf(items: List<E>, binder: RecyclerBinder<E>): Builder =
            append(itemAdapterOf(items, binder))

        override fun <E : Any> adapterOf(
            items: List<E>,
            @LayoutRes
            layoutRes: Int,
            block: BinderBuilderBlock<E>
        ): Builder {
            val binderBuilder = Binder.Builder<E>(layoutRes)
            return append(itemAdapterOf(items, binderBuilder.apply(block).build()))
        }

        override fun <E : Any> adapterOf(
            items: List<E>,
            @LayoutRes
            layoutRes: Int,
            @IdRes
            viewType: Int,
            block: BinderBuilderBlock<E>
        ): Builder {
            val binderBuilder = Binder.Builder<E>(layoutRes, viewType)
            return append(itemAdapterOf(items, binderBuilder.apply(block).build()))
        }

        override fun build(): Adapter {
            return compositeBuilder.build()
        }

    }

}

internal fun emptyAdapter(): Adapter = EmptyAdapter