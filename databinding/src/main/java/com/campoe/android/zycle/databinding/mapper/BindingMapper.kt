package com.campoe.android.zycle.databinding.mapper

import androidx.annotation.LayoutRes
import com.campoe.android.zycle.mapper.Mapper
import com.campoe.android.zycle.viewholder.ViewHolder

internal class BindingMapper<E : Any, VH : ViewHolder>(
    @LayoutRes layoutRes: Int,
    private val bindingVariable: Int
) : Mapper<E, VH>(layoutRes) {

}

internal fun <T : Any> mapper(
    @LayoutRes layoutRes: Int,
    bindingVariable: Int
): BindingMapper<T, ViewHolder> =
    BindingMapper(layoutRes, bindingVariable)