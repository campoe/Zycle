package com.campoe.android.zycle.extension

import androidx.recyclerview.widget.RecyclerView

internal interface Attachable {

    fun onAttach(recyclerView: RecyclerView)

}