package com.campoe.android.zycle.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.campoe.android.zycle.R
import com.campoe.android.zycle.observablelist.ObservableList
import com.campoe.android.zycle.observablelist.observableListOf
import com.campoe.android.zycle.zycle
import kotlinx.android.synthetic.main.activity_sample.*
import kotlinx.android.synthetic.main.header_section.view.*

class MainActivity : AppCompatActivity(), StickyHeaderListener {

    companion object {

        private const val Sections: Int = 5
        const val SectionItemCount: Int = 6
        const val ImageOffset: Int = 160
        const val ItemCount: Int = Sections * SectionItemCount

        const val GridSpanCount: Int = 2

    }

    private val list: ObservableList<Any> = observableListOf()

    private lateinit var menuItemGrid: MenuItem
    private lateinit var menuItemList: MenuItem

    private var headerBinder = HeaderBinder()
    private var imageBinder = ImageBinder()

    private var spanSizeLookup: GridLayoutManager.SpanSizeLookup =
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                if (list[position] is HeaderBinder.Header) {
                    GridSpanCount
                } else {
                    1
                }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        setSupportActionBar(toolbar)

        recyclerView.zycle {
            layoutManager(LinearLayoutManager(this@MainActivity))
            adapterOf(list) {
                map(headerBinder)
                map(imageBinder)
            }
        }
        recyclerView.addItemDecoration(StickyHeaderDecoration(this))

        var section = 1
        val range = ImageOffset until (ImageOffset + ItemCount) step SectionItemCount
        for (i in range) {
            list.add(newHeader(section))
            for (j in 0 until SectionItemCount) {
                list.add(newImage(i + j))
            }
            section++
        }
    }

    private fun newHeader(i: Int) =
        HeaderBinder.Header(
            i.toLong(),
            "Section $i"
        )

    private fun newImage(i: Int) =
        ImageBinder.Image(
            "https://picsum.photos/id/${i}/960/600",
            i.toLong(),
            "Lorem Ipsum",
            "In chislic ut labore ipsum dolore nulla excepteur frankfurter aute shoulder swine."
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
        menuItemGrid = menu.findItem(R.id.switch_grid)
        menuItemList = menu.findItem(R.id.switch_list)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.switch_grid -> {
                item.isVisible = false
                menuItemList.isVisible = true
                recyclerView.layoutManager =
                    imageBinder.switchLayout(1, recyclerView.context)
                true
            }
            R.id.switch_list -> {
                item.isVisible = false
                menuItemGrid.isVisible = true
                recyclerView.layoutManager =
                    imageBinder.switchLayout(GridSpanCount, recyclerView.context).apply {
                        if (this is GridLayoutManager) {
                            spanSizeLookup = this@MainActivity.spanSizeLookup
                        }
                    }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}