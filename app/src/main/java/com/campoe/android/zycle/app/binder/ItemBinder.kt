package com.campoe.android.zycle.app.binder

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.campoe.android.zycle.R
import com.campoe.android.zycle.binder.Binder
import com.campoe.android.zycle.diff.Diffable
import com.campoe.android.zycle.eventhook.EventHook
import com.campoe.android.zycle.eventhook.Hookable
import com.campoe.android.zycle.eventhook.drag.Draggable
import com.campoe.android.zycle.eventhook.swipe.Swipeable
import kotlinx.android.synthetic.main.item_image.view.*

class ItemBinder : Binder<ItemBinder.Item, RecyclerView.ViewHolder>(),
    Hookable<ItemBinder.Item, RecyclerView.ViewHolder>, Draggable, Swipeable {

    override val eventHooks: MutableCollection<EventHook<Item, RecyclerView.ViewHolder>> =
        mutableListOf()

    private var layoutList: Boolean = true
    override val layoutRes: Int
        get() = if (layoutList) R.layout.item_image else R.layout.item_image_small
    override val viewType: Int
        get() = super.viewType + (if (layoutList) 1 else 0)

    fun switchLayout(spanCount: Int, context: Context): RecyclerView.LayoutManager {
        return if (spanCount <= 1) {
            layoutList = true
            LinearLayoutManager(context)
        } else {
            layoutList = false
            GridLayoutManager(context, spanCount)
        }
    }

    override fun hasStableIds(): Boolean = true
    override fun getItemId(item: Item, position: Int): Long {
        return item.id
    }

    override fun onBind(holder: RecyclerView.ViewHolder, item: Item) {
        val itemView = holder.itemView

        itemView.title_text?.text = item.title
        itemView.description_text?.text = item.description
        itemView.id_text?.text = item.id.toString()

        itemView.image?.also {
            Glide.with(itemView.context)
                .asDrawable()
                .load(item.imageUrl)
                .error(R.color.colorError)
                .into(it)
        }
    }

    override fun onRecycle(holder: RecyclerView.ViewHolder) {
        val itemView = holder.itemView
        itemView.title_text?.text = null
        itemView.description_text?.text = null
        itemView.id_text?.text = null
        itemView.image?.setImageDrawable(null)
    }

    data class Item(
        val id: Long,
        val title: String,
        val description: String,
        val imageUrl: String
    ) : Diffable<Item> {
        override fun areItemsTheSame(other: Item): Boolean {
            return this.id == other.id
        }

        override fun areContentsTheSame(other: Item): Boolean {
            return this == other
        }
    }

}