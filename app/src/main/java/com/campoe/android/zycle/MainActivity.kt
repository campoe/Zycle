package com.campoe.android.zycle

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.campoe.android.zycle.observablelist.ObservableList
import com.campoe.android.zycle.observablelist.observableListOf
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_text.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val list: ObservableList<Any> = observableListOf()
        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.zycle {
            layoutManager(layoutManager)
            emptyView(R.id.emptyView)
            adapterOf(list) {
                map<String>(R.layout.item_text) {
                    stableId { _, position ->
                        position.toLong()
                    }
                    onBind {
                        itemView.textView.text = item
                    }
                    onItemClick {
                        Toast.makeText(applicationContext, item, Toast.LENGTH_SHORT).show()
                    }
                    onItemLongClick {
                        list.removeAt(adapterPosition)
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