package com.campoe.android.zycle.condition

interface ICondition {

    fun assign(value: Boolean)

    fun eval(): Boolean

}