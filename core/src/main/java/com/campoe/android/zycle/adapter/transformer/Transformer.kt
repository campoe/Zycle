package com.campoe.android.zycle.adapter.transformer

import com.campoe.android.zycle.adapter.Adapter

interface Transformer {

    fun transform(adapter: Adapter): Adapter

}