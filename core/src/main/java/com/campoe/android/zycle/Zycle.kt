package com.campoe.android.zycle

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.`typealias`.AdapterBuilderBlock
import com.campoe.android.zycle.`typealias`.ZycleBlock
import com.campoe.android.zycle.adapter.Adapter

@DslMarker
annotation class ZycleDsl

@ZycleDsl
class Zycle internal constructor(
    private val recyclerView: RecyclerView
) {

    private var adapter: Adapter<*>? = null

    fun <E : Any> adapter(adapter: Adapter<E>) =
        apply { this.adapter = adapter }

    fun <E : Any> adapterOf(
        elements: MutableList<E>,
        block: AdapterBuilderBlock<E>
    ) =
        adapter(Adapter.Builder<E>().apply(block).build(elements))

    fun <E : Any> adapterOf(
        elements: Array<out E>,
        block: AdapterBuilderBlock<E>
    ) =
        adapter(Adapter.Builder<E>().apply(block).build(elements))

    fun <E : Any> adapterOf(
        block: AdapterBuilderBlock<E>
    ) =
        adapter(Adapter.Builder<E>().apply(block).build())

    fun layoutManager(layoutManager: RecyclerView.LayoutManager) =
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

fun RecyclerView.zycle(block: ZycleBlock) {
    zycle().apply(block).build()
}