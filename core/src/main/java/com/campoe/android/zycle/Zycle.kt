package com.campoe.android.zycle

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.adapter.adapterOf
import com.campoe.android.zycle.adapter.toAdapter
import com.campoe.android.zycle.viewholder.ViewHolder

@DslMarker
annotation class ZycleDsl

@ZycleDsl
class Zycle internal constructor(
    private val recyclerView: RecyclerView
) {

    private var emptyView: View? = null
    private var adapter: Adapter<*, *>? = null

    fun emptyView(@IdRes idRes: Int) =
        apply { emptyView = recyclerView.rootView.findViewById(idRes) }

    fun emptyView(emptyView: View) =
        apply { this.emptyView = emptyView }

    fun <E : Any> adapter(adapter: Adapter<E, ViewHolder<E>>) =
        apply { this.adapter = adapter }

    fun <E : Any> adapterOf(
        elements: MutableList<E>,
        block: Adapter<E, ViewHolder<E>>.() -> Unit
    ) =
        apply { adapter = elements.toAdapter().apply(block) }

    fun <E : Any> adapterOf(
        elements: Array<out E>,
        block: Adapter<E, ViewHolder<E>>.() -> Unit
    ) =
        apply { adapter = adapterOf<E>(*elements).apply(block) }

    fun <E : Any> adapterOf(
        block: Adapter<E, ViewHolder<E>>.() -> Unit
    ) =
        apply { adapter = adapterOf<E>().apply(block) }

    fun layoutManager(layoutManager: RecyclerView.LayoutManager) =
        apply { recyclerView.layoutManager = layoutManager }

    fun paginate(refreshSize: Int) =
        apply { /* TODO */ }

    fun swipeUpToRefresh() =
        apply { /* TODO */ }

    fun scrollbar() =
        apply { /* TODO */ }

    inline fun extension(crossinline f: (View, Int) -> Unit) =
        apply { /* TODO */ }

    inline fun extension(crossinline f: (RecyclerView) -> Unit) =
        apply { /* TODO */ }

    private fun showEmptyView(visible: Boolean) {
        recyclerView.visibility = if (visible) View.GONE else View.VISIBLE
        emptyView?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun build(): RecyclerView {
        if (recyclerView.layoutManager == null) recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context)
        adapter?.apply {
            setHasStableIds(hasStableIds() || anyHasStableIds())
            attach(this@Zycle.recyclerView)
        }
        val emptyObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                showEmptyView(adapter?.items.isNullOrEmpty())
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                showEmptyView(adapter?.items.isNullOrEmpty())
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                showEmptyView(adapter?.items.isNullOrEmpty())
            }
        }
        adapter?.apply {
            registerAdapterDataObserver(emptyObserver)
            onDetach { unregisterAdapterDataObserver(emptyObserver) }
        }
        adapter?.items?.ifEmpty { showEmptyView(true) }
        return recyclerView
    }

}

fun RecyclerView.zycle(): Zycle {
    return Zycle(this)
}

fun RecyclerView.zycle(block: Zycle.() -> Unit) {
    zycle().apply(block).build()
}