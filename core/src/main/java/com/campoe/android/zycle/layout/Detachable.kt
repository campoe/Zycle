package com.campoe.android.zycle.layout

import androidx.recyclerview.widget.RecyclerView

interface Detachable {

    fun onDetach(recyclerView: RecyclerView)

}