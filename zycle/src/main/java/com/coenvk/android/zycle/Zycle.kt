package com.coenvk.android.zycle

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coenvk.android.zycle.`typealias`.AdapterBuilderBlock
import com.coenvk.android.zycle.`typealias`.BinderBuilderBlock
import com.coenvk.android.zycle.`typealias`.MapperBuilderBlock
import com.coenvk.android.zycle.adapter.Adapter
import com.coenvk.android.zycle.adapter.itemAdapterOf
import com.coenvk.android.zycle.adapter.viewAdapterOf
import com.coenvk.android.zycle.binder.Binder
import com.coenvk.android.zycle.binder.RecyclerBinder
import com.coenvk.android.zycle.mapper.Mapper

@DslMarker
annotation class ZycleDsl

@ZycleDsl
class Zycle(
    private val recyclerView: RecyclerView
) {

    private var adapter: Adapter? = null

    fun adapter(adapter: Adapter): Zycle =
        apply { this.adapter = adapter }

    fun viewsOf(@LayoutRes vararg layouts: Int) =
        adapter(viewAdapterOf(*layouts))

    fun adapterOf(
        block: AdapterBuilderBlock
    ) =
        adapter(Adapter.Builder().apply(block).build())

    fun <E : Any> adapterOf(items: List<E>, mapper: Mapper<E>) =
        adapter(itemAdapterOf(items, mapper))

    fun <E : Any> adapterOf(items: List<E>, block: MapperBuilderBlock<E>): Zycle {
        val mapperBuilder = Mapper.Builder<E>()
        return adapter(itemAdapterOf(items, mapperBuilder.apply(block).build()))
    }

    fun <E : Any> adapterOf(items: List<E>, binder: RecyclerBinder<E>) =
        adapter(itemAdapterOf(items, binder))

    fun <E : Any> adapterOf(
        items: List<E>,
        @LayoutRes
        layoutRes: Int,
        block: BinderBuilderBlock<E>
    ): Zycle {
        val binderBuilder = Binder.Builder<E>(layoutRes)
        return adapter(itemAdapterOf(items, binderBuilder.apply(block).build()))
    }

    fun <E : Any> adapterOf(
        items: List<E>,
        @LayoutRes
        layoutRes: Int,
        @IdRes
        viewType: Int,
        block: BinderBuilderBlock<E>
    ): Zycle {
        val binderBuilder = Binder.Builder<E>(layoutRes, viewType)
        return adapter(itemAdapterOf(items, binderBuilder.apply(block).build()))
    }

    fun layoutManager(layoutManager: RecyclerView.LayoutManager): Zycle =
        apply { recyclerView.layoutManager = layoutManager }

    fun build(): RecyclerView {
        if (recyclerView.layoutManager == null) recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context)
        adapter?.also {
            it.attach(recyclerView)
        }
        return recyclerView
    }

}

fun RecyclerView.zycle(): Zycle {
    return Zycle(this)
}

fun RecyclerView.zycle(block: Zycle.() -> Unit) {
    zycle().apply(block).build()
}