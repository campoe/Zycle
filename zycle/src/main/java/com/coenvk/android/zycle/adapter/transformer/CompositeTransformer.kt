package com.coenvk.android.zycle.adapter.transformer

import com.coenvk.android.zycle.adapter.Adapter

class CompositeTransformer(private vararg val transformers: Transformer) : Transformer {

    override fun transform(adapter: Adapter): Adapter =
        transformers.fold(adapter) { acc, transformer -> transformer.transform(acc) }

}