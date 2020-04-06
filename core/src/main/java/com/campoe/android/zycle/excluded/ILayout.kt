package com.campoe.android.zycle.excluded

import android.animation.Animator
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.layout.Item
import com.campoe.android.zycle.viewholder.ViewHolder

//internal interface ILayout<E : Item<*, VH>, VH : RecyclerView.ViewHolder> {
//
//    @LayoutRes
//    fun getLayoutRes(position: Int): Int
//    fun getItemCount(): Int = 0
//    fun getItem(position: Int): E
//    fun getItemId(position: Int): Long = RecyclerView.NO_ID
//    fun onCreate(holder: VH) = Unit
//    fun onBind(holder: VH) = Unit
//    fun onRecycle(holder: VH) = Unit
//    fun onBindAnimation(): Animator? = null
//
//}