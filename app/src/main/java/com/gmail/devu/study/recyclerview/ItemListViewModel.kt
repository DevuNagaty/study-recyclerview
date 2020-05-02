package com.gmail.devu.study.recyclerview

import android.util.Log
import androidx.lifecycle.ViewModel

class ItemListViewModel : ViewModel() {
    val TAG = this::class.java.simpleName

    data class Item(val id: Int, val title: String, val details: String) {
        override fun toString(): String = title
    }

    var items = ArrayList<Item>()
    var selected = 0

    // Initial count of dummy items
    private val INITIAL_ITEM_COUNT = 50

    init {
        // Generate dummy items
        for (i in 1..INITIAL_ITEM_COUNT) {
            val title = "item #" + i.toString()
            val details = StringBuilder()
                .append("This is detail of item #")
                .append(i)
                .also { builder ->
                    for (n in 1..i) {
                        builder.append("\n L")
                        builder.append(n)
                        builder.append(": description")
                    }
                }
                .toString()

            items.add(Item(i, title, details))
        }
    }

    fun selectItem(item: Item) {
        selected = items.indexOf(item)
    }

    fun moveItem(oldIndex : Int, newIndex : Int) {
        Log.d(TAG, "moveItem(%d, %d): id=%d".format(oldIndex, newIndex, items[oldIndex].id))
        val item = items.removeAt(oldIndex)
        items.add(newIndex, item)
    }

    fun removeItemAt(index : Int) {
        Log.d(TAG, "removeItemAt(%d): id=%s".format(index, items[index].id))
        items.removeAt(index)
    }
}