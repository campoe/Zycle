package com.campoe.android.zycle.databinding.extension

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.campoe.android.zycle.Zycle
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.databinding.adapter.toBindingAdapter
import com.campoe.android.zycle.databinding.mapper.mapper
import com.campoe.android.zycle.databinding.viewholder.BindingViewHolder
import com.campoe.android.zycle.mapper.Mapper
import com.campoe.android.zycle.viewholder.ViewHolder

@Suppress("UNCHECKED_CAST")
fun <E : Any> Zycle.bindingAdapterOf(
    elements: MutableList<E>,
    block: Adapter<E, ViewHolder<E>>.() -> Unit
) =
    adapter(elements.toBindingAdapter<E, ViewDataBinding>().apply(block))

// FIXME: make public
internal fun <T : E, E : Any, VH : ViewHolder<E>> Adapter<E, VH>.map(
    clazz: Class<T>,
    @LayoutRes layoutRes: Int,
    bindingVariable: Int,
    block: (Mapper<T, ViewHolder<T>>.() -> Unit)? = null
): Adapter<E, VH> =
    map(clazz, mapper<T>(layoutRes, bindingVariable).apply { block?.invoke(this) })

// FIXME: make public
internal inline fun <reified T : E, E : Any, VH : ViewHolder<E>> Adapter<E, VH>.map(
    @LayoutRes layoutRes: Int,
    bindingVariable: Int
): Adapter<E, VH> =
    map(T::class.java, layoutRes, bindingVariable)

// FIXME: make public
internal inline fun <reified T : E, E : Any, VH : ViewHolder<E>> Adapter<E, VH>.map(
    @LayoutRes layoutRes: Int,
    bindingVariable: Int,
    noinline block: (Mapper<T, ViewHolder<T>>.() -> Unit)
) =
    map(T::class.java, layoutRes, bindingVariable, block)

internal val <E : Any> ViewHolder<E>.dataBinding: ViewDataBinding?
    get() = if (this is BindingViewHolder<E, *>) {
        binding
    } else null

@Suppress("UNCHECKED_CAST")
fun <DB : ViewDataBinding> ViewHolder<*>.binding(): DB? =
    if (this is BindingViewHolder<*, *>) {
        binding as? DB
    } else null

fun <DB : ViewDataBinding> ViewHolder<*>.requireBinding(): DB =
    requireNotNull(binding())