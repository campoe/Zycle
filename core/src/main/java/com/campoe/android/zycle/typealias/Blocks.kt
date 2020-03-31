package com.campoe.android.zycle.`typealias`

import com.campoe.android.zycle.Zycle
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.mapper.Mapper
import com.campoe.android.zycle.viewholder.ViewHolder

internal typealias MapperBlock<T> = Mapper<T, ViewHolder<T>>.() -> Unit
internal typealias AdapterBlock<E, VH> = Adapter<E, VH>.() -> Unit
internal typealias ZycleBlock = Zycle.() -> Unit