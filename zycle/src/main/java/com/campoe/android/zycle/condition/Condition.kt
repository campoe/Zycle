package com.campoe.android.zycle.condition

import android.database.Observable
import com.campoe.android.zycle.condition.observer.Observer
import kotlin.reflect.KProperty

class Condition(private var value: Boolean = true) : ICondition, Observable<Observer>() {

    override fun assign(value: Boolean) {
        if (this.value != value) {
            this.value = value
            notifyChanged()
        }
    }

    override fun eval(): Boolean = value

    private fun notifyChanged() = mObservers.reversed().forEach { it.onChanged() }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean = eval()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) = assign(value)

}