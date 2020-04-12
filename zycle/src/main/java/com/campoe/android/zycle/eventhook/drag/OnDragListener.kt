package com.campoe.android.zycle.eventhook.drag

interface OnDragListener {

    fun onDragged(fromPosition: Int, toPosition: Int) = Unit
    fun onDropped(fromPosition: Int, toPosition: Int) = Unit

}