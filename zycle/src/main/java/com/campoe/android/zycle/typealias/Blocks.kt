package com.campoe.android.zycle.`typealias`

import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.binder.Binder
import com.campoe.android.zycle.mapper.Mapper

internal typealias BinderBuilderBlock<E> = Binder.Builder<E>.() -> Unit
internal typealias MapperBuilderBlock<E> = Mapper.Builder<E>.() -> Unit
internal typealias AdapterBuilderBlock = Adapter.Builder.() -> Unit
internal typealias AdapterBlock = Adapter.() -> Adapter