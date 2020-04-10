package com.campoe.android.zycle.adapter.conditional

import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.adapter.delegate.DelegateAdapter
import com.campoe.android.zycle.condition.Condition
import com.campoe.android.zycle.condition.observer.Observer


internal open class ConditionalAdapter(
    adapter: Adapter,
    @JvmField protected val condition: Condition
) :
    DelegateAdapter(adapter) {

    protected var isVisible: Boolean = false

    private val observer: Observer =
        object : Observer() {
            override fun onChanged() {
                val itemCount = getItemCount()
                isVisible = condition.eval()
                onChanged(itemCount, getItemCount())
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
            val itemCount = getItemCount()
            this.isVisible = isVisible
            onChanged(itemCount, getItemCount())
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

    protected open fun onChanged(preItemCount: Int, postItemCount: Int) {
        if (isVisible) {
            notifyItemRangeInserted(0, postItemCount)
        } else {
            notifyItemRangeRemoved(0, preItemCount)
        }
    }

}