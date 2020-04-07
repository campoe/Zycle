package com.campoe.android.zycle.`typealias`

import com.campoe.android.zycle.Zycle
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.binder.Binder

internal typealias BinderBuilderBlock<E> = Binder.Builder<E>.() -> Unit
internal typealias AdapterBuilderBlock<E> = Adapter.Builder<E>.() -> Unit
internal typealias ZycleBlock = Zycle.() -> Unit