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
    private val mValues: List<ItemListViewModel.Item>,
    private val mListener: InteractionListener?
) : RecyclerView.Adapter<ItemListRecyclerViewAdapter.ViewHolder>() {

    interface InteractionListener {
        fun onClick(item: ItemListViewModel.Item)
    }

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ItemListViewModel.Item
            mListener?.onClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemlist_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.id.toString()
        holder.mContentView.text = item.title

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
