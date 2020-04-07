package com.campoe.android.zycle.mapper

import androidx.annotation.LayoutRes
import com.campoe.android.zycle.`typealias`.BinderBuilderBlock
import com.campoe.android.zycle.binder.Binder
import com.campoe.android.zycle.binder.RecyclerBinder

open class Mapper<E : Any> : IMapper<E> {

    protected val map: MutableMap<Class<*>, RecyclerBinder<E>> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : E> map(clazz: Class<T>, binder: RecyclerBinder<T>) =
        apply { map[clazz] = binder as RecyclerBinder<E> }

    inline fun <reified T : E> map(binder: RecyclerBinder<T>) =
        map(T::class.java, binder)

    @Suppress("UNCHECKED_CAST")
    final override fun <T : E> map(
        clazz: Class<T>,
        @LayoutRes
        layoutRes: Int,
        viewType: Int,
        block: BinderBuilderBlock<T>?
    ): Mapper<E> =
        apply {
            map[clazz] = Binder.Builder<T>(layoutRes, viewType)
                .apply { block?.invoke(this) }.build() as RecyclerBinder<E>
        }

    @Suppress("UNCHECKED_CAST")
    final override fun <T : E> map(
        clazz: Class<T>,
        @LayoutRes
        layoutRes: Int,
        block: BinderBuilderBlock<T>?
    ): Mapper<E> =
        apply {
            map[clazz] =
                Binder.Builder<T>(
                    layoutRes,
                    clazz.name.hashCode()
                )
                    .apply { block?.invoke(this) }.build() as RecyclerBinder<E>
        }

    inline fun <reified T : E> map(
        @LayoutRes layoutRes: Int
    ) =
        map(T::class.java, layoutRes)

    inline fun <reified T : E> map(
        @LayoutRes layoutRes: Int,
        viewType: Int
    ) =
        map(T::class.java, layoutRes, viewType)

    inline fun <reified T : E> map(
        @LayoutRes layoutRes: Int,
        noinline block: BinderBuilderBlock<T>
    ) =
        map(T::class.java, layoutRes, block)

    inline fun <reified T : E> map(
        @LayoutRes layoutRes: Int,
        viewType: Int,
        noinline block: BinderBuilderBlock<T>
    ) =
        map(T::class.java, layoutRes, viewType, block)

}