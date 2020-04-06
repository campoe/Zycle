package com.campoe.android.zycle.drag

interface DragListener {

    fun onDragged(fromIndex: Int, toIndex: Int)
    fun onDropped(fromIndex: Int, toIndex: Int)

}