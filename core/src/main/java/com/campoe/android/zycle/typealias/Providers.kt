package com.campoe.android.zycle.`typealias`

internal typealias ViewTypeProvider<E> = (E, Int) -> Int
internal typealias LayoutProvider = (Int) -> Int
internal typealias StableIdProvider<E> = (E, Int) -> Long