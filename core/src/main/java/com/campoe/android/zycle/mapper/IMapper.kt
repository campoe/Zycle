package com.campoe.android.zycle.mapper

import androidx.annotation.LayoutRes
import com.campoe.android.zycle.`typealias`.BinderBuilderBlock
import com.campoe.android.zycle.adapter.Adapter

interface IMapper<E : Any> {

    fun <T : E> map(
        clazz: Class<T>,
        @LayoutRes layoutRes: Int,
        block: BinderBuilderBlock<T>? = null
    ): Mapper<E>

    fun <T : E> map(
        clazz: Class<T>,
        @LayoutRes layoutRes: Int,
        viewType: Int,
        block: BinderBuilderBlock<T>? = null
    ): Mapper<E>

}