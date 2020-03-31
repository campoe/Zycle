package com.campoe.android.zycle.databinding.viewholder

import androidx.databinding.ViewDataBinding
import com.campoe.android.zycle.viewholder.ViewHolder

internal class BindingViewHolder<E : Any, DB : ViewDataBinding>(internal val binding: DB) :
    ViewHolder<E>(binding.root)