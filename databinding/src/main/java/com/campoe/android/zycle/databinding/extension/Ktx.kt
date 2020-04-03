package com.campoe.android.zycle.databinding.extension

import androidx.databinding.ViewDataBinding
import com.campoe.android.zycle.Zycle
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.databinding.viewholder.BindingViewHolder
import com.campoe.android.zycle.viewholder.ViewHolder

@Suppress("UNCHECKED_CAST")
internal fun <E : Any> Array<out E>.toBindingAdapter(): Adapter<E, ViewHolder> =
    Adapter<E, BindingViewHolder<ViewDataBinding>>(this) as Adapter<E, ViewHolder>

@Suppress("UNCHECKED_CAST")
internal fun <E : Any> MutableList<E>.toBindingAdapter(): Adapter<E, ViewHolder> =
    Adapter<E, BindingViewHolder<ViewDataBinding>>(this) as Adapter<E, ViewHolder>

@Suppress("UNCHECKED_CAST")
fun <E : Any> Zycle.bindingAdapterOf(
    elements: MutableList<E>,
    block: Adapter<E, ViewHolder>.() -> Unit
) =
    adapter(elements.toBindingAdapter().apply(block))

internal val ViewHolder.dataBinding: ViewDataBinding?
    get() = if (this is BindingViewHolder<*>) {
        binding
    } else null

@Suppress("UNCHECKED_CAST")
fun <DB : ViewDataBinding> ViewHolder.binding(): DB? =
    if (this is BindingViewHolder<*>) {
        binding as? DB
    } else null

fun <DB : ViewDataBinding> ViewHolder.requireBinding(): DB =
    requireNotNull(binding())