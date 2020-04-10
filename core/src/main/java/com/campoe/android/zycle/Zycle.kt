package com.campoe.android.zycle

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.`typealias`.AdapterBuilderBlock
import com.campoe.android.zycle.`typealias`.BinderBuilderBlock
import com.campoe.android.zycle.`typealias`.MapperBuilderBlock
import com.campoe.android.zycle.`typealias`.ZycleBlock
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.adapter.itemAdapterOf
import com.campoe.android.zycle.adapter.viewAdapterOf
import com.campoe.android.zycle.binder.Binder
import com.campoe.android.zycle.binder.RecyclerBinder
import com.campoe.android.zycle.mapper.Mapper

@DslMarker
annotation class ZycleDsl

@ZycleDsl
class Zycle internal constructor(
    private val recyclerView: RecyclerView
) {

    private var adapter: Adapter? = null

    fun adapter(adapter: Adapter): Adapter {
        this.adapter = adapter
        return adapter
    }

    fun adapter(
        block: AdapterBuilderBlock
    ) =
        adapter(Adapter.Builder().apply(block).build())

    fun viewsOf(@LayoutRes vararg layouts: Int) =
        adapter(viewAdapterOf(*layouts))

    fun adapterOf(
        block: AdapterBuilderBlock
    ) =
        adapter(Adapter.Builder().apply(block).build())

    fun <E : Any> adapterOf(items: List<E>, mapper: Mapper<E>): Adapter =
        adapter(itemAdapterOf(items, mapper))

    fun <E : Any> adapterOf(items: List<E>, block: MapperBuilderBlock<E>): Adapter {
        val mapperBuilder = Mapper.Builder<E>()
        return adapter(itemAdapterOf(items, mapperBuilder.apply(block).build()))
    }

    fun <E : Any> adapterOf(items: List<E>, binder: RecyclerBinder<E>): Adapter =
        adapter(itemAdapterOf(items, binder))

    fun <E : Any> adapterOf(
        items: List<E>,
        @LayoutRes
        layoutRes: Int,
        block: BinderBuilderBlock<E>
    ): Adapter {
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
    ): Adapter {
        val binderBuilder = Binder.Builder<E>(layoutRes, viewType)
        return adapter(itemAdapterOf(items, binderBuilder.apply(block).build()))
    }

    fun layoutManager(layoutManager: RecyclerView.LayoutManager): RecyclerView.LayoutManager {
        recyclerView.layoutManager = layoutManager
        return layoutManager
    }

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

fun RecyclerView.zycle(block: ZycleBlock) {
    zycle().apply(block).build()
}