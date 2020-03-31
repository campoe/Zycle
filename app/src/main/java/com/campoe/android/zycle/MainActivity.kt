package com.campoe.android.zycle

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.campoe.android.zycle.databinding.extension.bindingAdapterOf
import com.campoe.android.zycle.databinding.extension.requireBinding
import com.campoe.android.zycle.observablelist.ObservableList
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val list = ObservableList<Any>()
        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.zycle {
            layoutManager(layoutManager)
            emptyView(R.id.emptyView)
            bindingAdapterOf(list) {
                map<String>(R.layout.item_bind_text) {
                    stableId { _, position ->
                        position.toLong()
                    }
                    onBind {
                        requireBinding<ViewDataBinding>().setVariable(BR.item, item)
                    }
                    onClick {
                        Toast.makeText(applicationContext, item, Toast.LENGTH_SHORT).show()
                    }
                    onLongClick {
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