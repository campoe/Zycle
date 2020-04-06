package com.campoe.android.zycle.app

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.campoe.android.zycle.R
import com.campoe.android.zycle.click.ktx.onClick
import com.campoe.android.zycle.click.ktx.onLongClick
import com.campoe.android.zycle.drag.ktx.onDragDrop
import com.campoe.android.zycle.emptiness.ktx.emptyView
import com.campoe.android.zycle.observablelist.observableListOf
import com.campoe.android.zycle.zycle
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.item_text.view.*

class EditActivity : SampleActivity(R.string.edit_sample, R.layout.activity_edit) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list = observableListOf<Any>()
        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.zycle {
            layoutManager(layoutManager)
            adapterOf(list) {
                emptyView(emptyView)
                map<String>(R.layout.item_bind_text) {
                    onBind { item ->
                        itemView.textView.text = item
                    }
                    onClick { item, _ ->
                        Toast.makeText(applicationContext, item, Toast.LENGTH_SHORT).show()
                    }
                    onLongClick { _, position ->
                        list.removeAt(position)
                        true
                    }
                }
            }
        }
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                layoutManager.orientation
            )
        )

        editText.setOnEditorActionListener { textView, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEND) {
                list.add(textView.text.toString())
                textView.text = ""
                true
            } else false
        }
    }

}