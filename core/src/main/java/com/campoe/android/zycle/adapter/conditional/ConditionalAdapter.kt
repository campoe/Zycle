package com.campoe.android.zycle.adapter.conditional

import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.adapter.delegate.DelegateAdapter
import com.campoe.android.zycle.condition.Condition
import com.campoe.android.zycle.condition.observer.Observer


internal open class ConditionalAdapter(adapter: Adapter, protected val condition: Condition) :
    DelegateAdapter(adapter) {

    protected var isVisible: Boolean = false

    private val observer: Observer =
        object : Observer() {
            override fun onChanged() {
                isVisible = condition.eval()
                onChanged(isVisible)
                updateObservers(isVisible)
            }
        }

    override fun getItemCount(): Int {
        return if (isVisible) super.getItemCount()
        else 0
    }

    override fun onFirstObserverRegistered() {
        condition.registerObserver(observer)
        val isVisible = condition.eval()
        if (this.isVisible != isVisible) {
            this.isVisible = isVisible
            onChanged(isVisible)
            if (isVisible) {
                super.onFirstObserverRegistered()
            }
        }
    }

    override fun onLastObserverUnregistered() {
        if (isVisible) {
            super.onLastObserverUnregistered()
        }
        condition.unregisterObserver(observer)
    }

    private fun updateObservers(isVisible: Boolean) {
        if (isVisible) {
            super.onFirstObserverRegistered()
        } else {
            super.onLastObserverUnregistered()
        }
    }

    protected open fun onChanged(isVisible: Boolean) {
        val count = super.getItemCount()
        if (isVisible) {
            notifyItemRangeInserted(0, count)
        } else {
            notifyItemRangeRemoved(0, count)
        }
    }

}