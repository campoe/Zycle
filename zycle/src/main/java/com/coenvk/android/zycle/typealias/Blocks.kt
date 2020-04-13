package com.coenvk.android.zycle.`typealias`

import com.coenvk.android.zycle.adapter.Adapter
import com.coenvk.android.zycle.binder.Binder
import com.coenvk.android.zycle.mapper.Mapper

internal typealias BinderBuilderBlock<E> = Binder.Builder<E>.() -> Unit
internal typealias MapperBuilderBlock<E> = Mapper.Builder<E>.() -> Unit
internal typealias AdapterBuilderBlock = Adapter.Builder.() -> Unit
internal typealias AdapterBlock = Adapter.() -> Adapter