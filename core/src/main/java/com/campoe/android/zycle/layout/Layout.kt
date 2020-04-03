package com.campoe.android.zycle.layout

import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.viewholder.ViewHolder

abstract class Layout<VH : ViewHolder> : ILayout<VH>, Attachable, Detachable {

    override fun onAttach(recyclerView: RecyclerView) = Unit
    override fun onDetach(recyclerView: RecyclerView) = Unit

}