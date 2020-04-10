package com.campoe.android.zycle.filter

class CompositeFilter<T>(private vararg val filters: Filter<T>) : Filter<T> {

    override fun apply(applyTo: T): Boolean {
        return filters.all { it.apply(applyTo) }
    }

}