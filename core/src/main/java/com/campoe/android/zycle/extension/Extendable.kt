package com.campoe.android.zycle.extension

import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.`typealias`.OnAttachListener
import com.campoe.android.zycle.`typealias`.OnDetachListener

internal interface Extendable {

    fun extendWith(extensionPoint: AdapterExtension): Extendable

}

internal fun Extendable.extendWith(
    onAttach: OnAttachListener,
    onDetach: OnDetachListener
): Extendable =
    extendWith(object : AdapterExtension() {
        override fun onAttach(recyclerView: RecyclerView) = onAttach(recyclerView)
        override fun onDetach(recyclerView: RecyclerView) = onDetach(recyclerView)
    })