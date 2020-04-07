package com.campoe.android.zycle.app

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.campoe.android.zycle.R
import com.campoe.android.zycle.binder.Binder
import kotlinx.android.synthetic.main.item_image.view.*

class ImageBinder : Binder<ImageBinder.Image, RecyclerView.ViewHolder>() {

    data class Image(
        val url: String,
        val id: Long,
        val title: String,
        val description: String
    )

    private var layoutList: Boolean = true

    internal fun switchLayout(spanCount: Int, context: Context): RecyclerView.LayoutManager {
        return if (spanCount <= 1) {
            layoutList = true
            LinearLayoutManager(context)
        } else {
            layoutList = false
            GridLayoutManager(context, spanCount)
        }
    }

    override val layoutRes: Int
        get() = if (layoutList) R.layout.item_image else R.layout.item_image_small

    override val viewType: Int
        get() = super.viewType + (if (layoutList) 1 else 0)

    override fun hasStableIds(): Boolean = true

    override fun getItemId(item: Image, position: Int): Long {
        return item.id
    }

    override fun onBind(holder: RecyclerView.ViewHolder, item: Image) {
        val itemView = holder.itemView

        itemView.title_text?.text = item.title
        itemView.description_text?.text = item.description
        itemView.id_text?.text = item.id.toString()

        itemView.image?.also {
            Glide.with(itemView.context)
                .asDrawable()
                .load(item.url)
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

}