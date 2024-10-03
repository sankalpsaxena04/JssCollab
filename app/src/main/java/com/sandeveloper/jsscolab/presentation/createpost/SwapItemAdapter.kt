package com.sandeveloper.jsscolab.presentation.createpost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapEntity

class SwapItemAdapter(private val onItemClick: (SwapEntity) -> Unit) :
    ListAdapter<SwapEntity, SwapItemAdapter.SwapItemViewHolder>(SwapItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwapItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_swap, parent, false)
        return SwapItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SwapItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class SwapItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.itemName)

        fun bind(swapItem: SwapEntity) {
            itemName.text = swapItem.name
            itemView.setOnClickListener {
                onItemClick(swapItem)
            }
        }
    }
}

class SwapItemDiffCallback : DiffUtil.ItemCallback<SwapEntity>() {
    override fun areItemsTheSame(oldItem: SwapEntity, newItem: SwapEntity): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: SwapEntity, newItem: SwapEntity): Boolean {
        return oldItem == newItem
    }
}
