package com.gmail.devu.study.recyclerview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A fragment representing a list of Items.
 */
class MasterFragment : Fragment(), ItemListRecyclerViewAdapter.InteractionListener {
    val TAG = this::class.java.simpleName

    private var columnCount = 1
    private lateinit var viewModel: ItemListViewModel
    private lateinit var viewAdapter: ItemListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        viewModel = ViewModelProvider(requireActivity()).get(ItemListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_master, container, false)

        viewAdapter = ItemListRecyclerViewAdapter(viewModel.items, this)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = viewAdapter

                itemTouchHelper.attachToRecyclerView(view)
            }
        }
        return view
    }

    override fun onClick(item: ItemListViewModel.Item) {
        viewModel.selectItem(item)
        findNavController().navigate(R.id.action_Master_to_Detail)
    }

    private val itemTouchHelper by lazy {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int =
                makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val oldPos = viewHolder.adapterPosition
                val newPos = target.adapterPosition
                Log.d(TAG, "onMove(%d -> %d)".format(oldPos, newPos))
                viewModel.moveItem(oldPos, newPos)
                viewAdapter.notifyItemMoved(oldPos, newPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                Log.d(TAG, "onSwiped(%d, %d)".format(pos, direction))
                viewModel.removeItemAt(pos)
                viewAdapter.notifyDataSetChanged()
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                when (actionState) {
                    ACTION_STATE_DRAG, ACTION_STATE_SWIPE -> {
                        // The background color of ViewHolder is changed with the selector.
                        viewHolder?.itemView?.isHovered = true
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.isHovered = false
            }

        })
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            MasterFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}