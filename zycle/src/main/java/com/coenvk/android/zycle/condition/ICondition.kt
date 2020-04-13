package com.coenvk.android.zycle.condition

internal interface ICondition {

    fun assign(value: Boolean)

    fun eval(): Boolean

}