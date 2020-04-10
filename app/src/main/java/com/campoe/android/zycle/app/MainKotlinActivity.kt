package com.campoe.android.zycle.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.campoe.android.zycle.R
import com.campoe.android.zycle.adapter.util.AdapterPositionLookup
import com.campoe.android.zycle.app.binder.HeaderBinder
import com.campoe.android.zycle.app.binder.ItemBinder
import com.campoe.android.zycle.app.stickyheader.StickyHeaderListener
import com.campoe.android.zycle.condition.Condition
import com.campoe.android.zycle.ktx.onClick
import com.campoe.android.zycle.observablelist.ObservableList
import com.campoe.android.zycle.observablelist.observableListOf
import com.campoe.android.zycle.zycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sample.*
import kotlinx.android.synthetic.main.header_section.view.*

class MainKotlinActivity : AppCompatActivity(),
    StickyHeaderListener {

    private val list: ObservableList<Any> = observableListOf()

    private lateinit var menuItemGrid: MenuItem
    private lateinit var menuItemList: MenuItem

    private lateinit var menuItemHide: MenuItem
    private lateinit var menuItemShow: MenuItem

    private val condition: Condition = Condition(true)
    private var isEnabled: Boolean by condition

    private var headerBinder = HeaderBinder()
    private var itemBinder = ItemBinder()

    private lateinit var adapterPositionLookup: AdapterPositionLookup
    private var spanSizeLookup: GridLayoutManager.SpanSizeLookup =
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val innerPosition = adapterPositionLookup.innerPosition(position)
                return if (list[innerPosition] is ItemBinder.Item) {
                    1
                } else {
                    GridSpanCount
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        setSupportActionBar(toolbar)

        recyclerView.zycle {
            layoutManager(LinearLayoutManager(this@MainKotlinActivity))
            adapter {
                viewsOf(R.layout.header_top)
                adapterOf {
                    adapterOf(list) {
                        map(headerBinder)
                        itemBinder.onClick { item, _ ->
                            Snackbar.make(this.itemView, item.description, Snackbar.LENGTH_SHORT)
                                .show()
                        }
                        map(itemBinder)
                    }
                    postBuild {
                        showIfElse(condition, R.layout.item_empty)
                    }
                }
                viewsOf(R.layout.footer_bottom)
            }.also {
                adapterPositionLookup =
                    AdapterPositionLookup(
                        it
                    )
            }
        }

        var section = 1
        val range = 0 until ItemCount step SectionItemCount
        for (i in range) {
            list.add(newHeader(section))
            for (j in 0 until SectionItemCount) {
                list.add(newItem(i + j))
            }
            ++section
        }
    }

    private fun newHeader(i: Int) =
        HeaderBinder.Header(
            i.toLong(),
            "Section $i"
        )

    private fun newItem(i: Int) =
        ItemBinder.Item(
            i.toLong(),
            String(Character.toChars(EmojiOffset + i)),
            "In chislic ut labore ipsum dolore nulla excepteur frankfurter aute shoulder swine.",
            "https://picsum.photos/id/${ImageOffset + i}/320/200"
        )

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = 0
        var i = itemPosition
        while (i >= 0) {
            if (isHeader(i)) {
                headerPosition = i
                break
            }
            --i
        }
        return headerPosition
    }

    override fun getHeaderLayoutRes(headerPosition: Int): Int =
        R.layout.header_section // TODO: should get from HeaderItem

    override fun bindHeaderData(view: View, headerPosition: Int) {
        val header = list[headerPosition] as? HeaderBinder.Header ?: return
        view.section_title.text = header.title
        view.section_switch.visibility = View.GONE
    }

    override fun isHeader(itemPosition: Int): Boolean = list[itemPosition] is HeaderBinder.Header
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menuItemHide = menu.findItem(R.id.hide_list)
        menuItemShow = menu.findItem(R.id.show_list)
        menuItemGrid = menu.findItem(R.id.switch_grid)
        menuItemList = menu.findItem(R.id.switch_list)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.hide_list -> {
                item.isVisible = false
                menuItemShow.isVisible = true
                isEnabled = false
                true
            }
            R.id.show_list -> {
                item.isVisible = false
                menuItemHide.isVisible = true
                isEnabled = true
                true
            }
            R.id.switch_grid -> {
                item.isVisible = false
                menuItemList.isVisible = true
                recyclerView.layoutManager =
                    itemBinder.switchLayout(GridSpanCount, recyclerView.context).apply {
                        if (this is GridLayoutManager) {
                            spanSizeLookup = this@MainKotlinActivity.spanSizeLookup
                        }
                    }
                true
            }
            R.id.switch_list -> {
                item.isVisible = false
                menuItemGrid.isVisible = true
                recyclerView.layoutManager =
                    itemBinder.switchLayout(1, recyclerView.context)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun scrollToBottom(view: View) {
        recyclerView.smoothScrollToPosition(recyclerView.adapter!!.itemCount - 1)
    }

    fun scrollToTop(view: View) {
        recyclerView.smoothScrollToPosition(0)
    }

    companion object {

        private const val Sections: Int = 5
        const val SectionItemCount: Int = 9
        const val ImageOffset: Int = 160
        const val ItemCount: Int = Sections * SectionItemCount

        const val EmojiOffset: Int = 0x1F60A

        const val GridSpanCount: Int = 3

    }

}