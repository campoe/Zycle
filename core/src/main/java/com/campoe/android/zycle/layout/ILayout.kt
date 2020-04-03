package com.campoe.android.zycle.layout

import android.animation.Animator
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.viewholder.ViewHolder

internal interface ILayout<VH : ViewHolder> {

    @LayoutRes
    fun getLayoutRes(position: Int): Int
    fun getItemCount(): Int = 0
    fun getItemId(position: Int): Long = RecyclerView.NO_ID
    fun onCreate(holder: VH) = Unit
    fun onBind(holder: VH) = Unit
    fun onRecycle(holder: VH) = Unit
    fun onBindAnimation(): Animator? = null

}