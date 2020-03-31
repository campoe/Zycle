package com.campoe.android.zycle.databinding.mapper

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.campoe.android.zycle.databinding.extension.binding
import com.campoe.android.zycle.mapper.Mapper
import com.campoe.android.zycle.viewholder.ViewHolder

internal class BindingMapper<E : Any, VH : ViewHolder<E>>(
    @LayoutRes layoutRes: Int,
    private val bindingVariable: Int
) : Mapper<E, VH>(layoutRes) {

    override val onBindListener: (VH.() -> Unit)?
        get() = {
            binding<ViewDataBinding>()?.setVariable(this@BindingMapper.bindingVariable, item)
        }

}

internal fun <T : Any> mapper(
    @LayoutRes layoutRes: Int,
    bindingVariable: Int
): BindingMapper<T, ViewHolder<T>> =
    BindingMapper(layoutRes, bindingVariable)