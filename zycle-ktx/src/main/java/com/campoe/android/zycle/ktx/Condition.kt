package com.campoe.android.zycle.ktx

import com.campoe.android.zycle.condition.Condition
import com.campoe.android.zycle.condition.observer.Observer

operator fun Condition.plusAssign(observer: Observer) =
    registerObserver(observer)

operator fun Condition.minusAssign(observer: Observer) =
    registerObserver(observer)