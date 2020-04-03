package com.campoe.android.zycle

import android.os.Bundle
import android.os.Handler
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.campoe.android.zycle.click.extension.onClick
import com.campoe.android.zycle.click.extension.onLongClick
import com.campoe.android.zycle.observablelist.ObservableList
import com.campoe.android.zycle.pagination.extension.paginate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_text.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val list = ObservableList<Any>()
        val layoutManager = LinearLayoutManager(recyclerView.context)
        val handler = Handler()
        recyclerView.zycle {
            layoutManager(layoutManager)
            emptyView(R.id.emptyView)
            adapterOf(list) {
                paginate(R.layout.item_progress) {
                    handler.postDelayed({
                        list.addAll(listOf("Hello", "Here", "Is", "Some", "More", "Text"))
                    }, 2000)
                }
                map<String>(R.layout.item_text) {
                    onBind { item, _ ->
                        itemView.textView.text = item
                    }
                    onClick { item, _ ->
                        Toast.makeText(applicationContext, item, Toast.LENGTH_SHORT)
                            .show()
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