package com.campoe.android.zycle.app

import android.view.View
import androidx.annotation.LayoutRes

interface StickyHeaderListener {

    fun getHeaderPositionForItem(itemPosition: Int): Int

    @LayoutRes
    fun getHeaderLayoutRes(headerPosition: Int): Int

    fun bindHeaderData(view: View, headerPosition: Int)

    fun isHeader(itemPosition: Int): Boolean

}