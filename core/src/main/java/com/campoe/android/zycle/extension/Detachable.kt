package com.campoe.android.zycle.extension

import androidx.recyclerview.widget.RecyclerView

internal interface Detachable {

    fun onDetach(recyclerView: RecyclerView)

}