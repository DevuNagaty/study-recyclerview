package com.gmail.devu.study.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.devu.study.recyclerview.ItemListRecyclerViewAdapter.InteractionListener
import kotlinx.android.synthetic.main.itemlist_row.view.*

/**
 * [RecyclerView.Adapter] that can display a [ItemListViewModel.Item] and makes a call to the
 * specified [InteractionListener].
 */
class ItemListRecyclerViewAdapter(
    private val items: List<ItemListViewModel.Item>,
    private val interactionlistener: InteractionListener?
) : RecyclerView.Adapter<ItemListRecyclerViewAdapter.ViewHolder>() {

    interface InteractionListener {
        fun onClick(item: ItemListViewModel.Item)
    }

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ItemListViewModel.Item
            interactionlistener?.onClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemlist_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.numberView.text = item.id.toString()
        holder.nameView.text = item.title

        with(holder.view) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val numberView: TextView = view.item_number
        val nameView: TextView = view.item_name
    }
}
