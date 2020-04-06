package com.campoe.android.zycle.app

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.campoe.android.zycle.R
import com.campoe.android.zycle.observablelist.observableListOf
import com.campoe.android.zycle.zycle
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import kotlinx.android.synthetic.main.activity_sample.*

class IconActivity : SampleActivity(R.string.icon_sample, R.layout.activity_sample) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list = observableListOf<IIcon>()
        recyclerView.zycle {
            layoutManager(GridLayoutManager(this@IconActivity, 3))
            adapterOf(list) {
                map<FontAwesome.Icon>(IconItem())
            }
        }

        val fonts = ArrayList(Iconics.getRegisteredFonts(this))
        fonts.sortWith(Comparator { o1, o2 -> o1.fontName.compareTo(o2.fontName) })

        fonts.forEach { font ->
            font.icons.forEach { icon ->
                list.add(font.getIcon(icon))
            }
        }
    }

}