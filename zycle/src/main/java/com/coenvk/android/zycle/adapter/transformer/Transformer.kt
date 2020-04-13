package com.coenvk.android.zycle.adapter.transformer

import com.coenvk.android.zycle.adapter.Adapter

interface Transformer {

    fun transform(adapter: Adapter): Adapter

}