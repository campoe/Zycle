package com.campoe.android.zycle.emptiness.ktx

import android.view.View
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.emptiness.extension.EmptinessExtension

fun Adapter.Builder<*>.emptyView(emptyView: View) =
    extendWith(EmptinessExtension(emptyView))