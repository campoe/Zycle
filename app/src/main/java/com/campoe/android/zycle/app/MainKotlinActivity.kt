package com.campoe.android.zycle.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.campoe.android.zycle.R
import com.campoe.android.zycle.adapter.util.AdapterPositionLookup
import com.campoe.android.zycle.app.binder.HeaderBinder
import com.campoe.android.zycle.app.binder.ItemBinder
import com.campoe.android.zycle.condition.Condition
import com.campoe.android.zycle.eventhook.drag.DragCallback
import com.campoe.android.zycle.eventhook.drag.OnDragListener
import com.campoe.android.zycle.eventhook.swipe.SwipeCallback
import com.campoe.android.zycle.ktx.observableListOf
import com.campoe.android.zycle.ktx.onClick
import com.campoe.android.zycle.ktx.plusAssign
import com.campoe.android.zycle.observablelist.ObservableList
import com.campoe.android.zycle.zycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sample.*

class MainKotlinActivity : AppCompatActivity() {

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
                return if (innerPosition in list.indices && list[innerPosition] is ItemBinder.Item) {
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

        val dragCallback = DragCallback(
            object : OnDragListener {
                override fun onDragged(fromPosition: Int, toPosition: Int) {
                    val fromIndex = adapterPositionLookup.innerPosition(fromPosition)
                    val toIndex = adapterPositionLookup.innerPosition(toPosition)
                    if (fromIndex in list.indices && toIndex in list.indices) {
                        list.move(fromIndex, toIndex)
                    }
                }
            }
        )
        val swipeCallback = SwipeCallback.Builder()
            .left {
                background(this@MainKotlinActivity, android.R.color.holo_red_dark)
                text(this@MainKotlinActivity, "Delete", spSize = 20f)
                onSwiped {
                    val index = adapterPositionLookup.innerPosition(it)
                    if (index in list.indices) {
                        list.removeAt(index)
                    }
                }
            }.build()

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
                postBuild {
                    adapterPositionLookup = AdapterPositionLookup(this)
                    this
                }
            }
        }

        recyclerView += ItemTouchHelper(dragCallback)
        recyclerView += ItemTouchHelper(swipeCallback)

        var section = 1
        for (i in 0 until Sections) {
            list.add(newHeader(section))
            for (j in 0 until SectionItemCount) {
                list.add(newItem((section - 1) * SectionItemCount + j))
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