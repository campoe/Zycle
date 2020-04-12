package com.campoe.android.zycle.app.data.binder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.R
import com.campoe.android.zycle.binder.Binder
import com.campoe.android.zycle.diff.Diffable
import kotlinx.android.synthetic.main.header_section.view.*

class HeaderBinder : Binder<HeaderBinder.Header, RecyclerView.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.header_section

    override fun hasStableIds(): Boolean = true
    override fun getItemId(item: Header, position: Int): Long = item.id
    override fun onBind(holder: RecyclerView.ViewHolder, item: Header) {
        val itemView = holder.itemView
        itemView.section_title.text = item.title
        itemView.section_switch.visibility = View.GONE
    }

    override fun onRecycle(holder: RecyclerView.ViewHolder) {
        holder.itemView.section_title.text = null
    }

    data class Header(
        val id: Long,
        val title: String
    ) : Diffable<Header> {

        override fun areItemsTheSame(other: Header): Boolean {
            return this.id == other.id
        }

        override fun areContentsTheSame(other: Header): Boolean {
            return this == other
        }
    }

}