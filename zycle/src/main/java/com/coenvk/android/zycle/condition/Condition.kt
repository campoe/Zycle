package com.coenvk.android.zycle.condition

import android.database.Observable
import com.coenvk.android.zycle.condition.observer.Observer
import kotlin.reflect.KProperty

class Condition(private var value: Boolean = true) : ICondition, Observable<Observer>() {

    override fun assign(value: Boolean) {
        if (this.value != value) {
            this.value = value
            notifyChanged()
        }
    }

    override fun eval(): Boolean = value

    private fun notifyChanged() {
        for (i in mObservers.indices.reversed()) {
            mObservers[i].onChanged()
        }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean = eval()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) = assign(value)

}