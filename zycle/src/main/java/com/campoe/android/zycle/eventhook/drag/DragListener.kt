package com.campoe.android.zycle.eventhook.drag

interface DragListener {

    fun onDragged(fromIndex: Int, toIndex: Int) = Unit
    fun onDropped(fromIndex: Int, toIndex: Int) = Unit

}