package com.campoe.android.zycle.mapper

import androidx.annotation.LayoutRes
import com.campoe.android.zycle.ZycleDsl
import com.campoe.android.zycle.`typealias`.BinderBuilderBlock
import com.campoe.android.zycle.binder.Binder
import com.campoe.android.zycle.binder.RecyclerBinder
import com.campoe.android.zycle.ktx.requireCast

sealed class Mapper<E : Any> : IMapper<E> {

    @ZycleDsl
    class Builder<E : Any> : IMapper.IBuilder<E> {

        private val map: MutableMap<Class<*>, RecyclerBinder<E>> = mutableMapOf()

        override fun <T : E> map(clazz: Class<T>, binder: RecyclerBinder<T>) =
            apply { map[clazz] = binder.requireCast() }

        inline fun <reified T : E> map(binder: RecyclerBinder<T>) =
            map(T::class.java, binder)

        override fun <T : E> map(
            clazz: Class<T>,
            @LayoutRes
            layoutRes: Int,
            viewType: Int,
            block: BinderBuilderBlock<T>?
        ) =
            map(
                clazz, Binder.Builder<T>(layoutRes, viewType)
                    .apply { block?.invoke(this) }.build()
            )

        override fun <T : E> map(
            clazz: Class<T>,
            @LayoutRes
            layoutRes: Int,
            block: BinderBuilderBlock<T>?
        ) =
            map(
                clazz,
                Binder.Builder<T>(
                    layoutRes,
                    clazz.name.hashCode()
                )
                    .apply { block?.invoke(this) }.build()
            )

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

        override fun build(): Mapper<E> {
            return when (map.size) {
                0 -> EmptyMapper()
                1 -> SingleMapper(map.values.toList()[0])
                else -> MultiMapper(map)
            }
        }

    }

}

private class MultiMapper<E : Any>(private val map: MutableMap<Class<*>, RecyclerBinder<E>> = mutableMapOf()) :
    Mapper<E>() {

    override fun <T : E> getBinder(clazz: Class<T>): RecyclerBinder<E> {
        return map[clazz]
            ?: throw RuntimeException()
    }

    override fun hasStableIds(): Boolean {
        return map.any {
            it.value.hasStableIds()
        }
    }

}

private class SingleMapper<E : Any>(private var binder: RecyclerBinder<E>) : Mapper<E>() {

    override fun <T : E> getBinder(clazz: Class<T>): RecyclerBinder<E> = binder

    override fun hasStableIds(): Boolean = binder.hasStableIds()

}

private class EmptyMapper<E : Any> : Mapper<E>() {

    override fun <T : E> getBinder(clazz: Class<T>): RecyclerBinder<E> =
        throw UnsupportedOperationException()

    override fun hasStableIds(): Boolean = true

}

internal fun <E : Any> mapperOf(binder: RecyclerBinder<E>): Mapper<E> = SingleMapper(binder)