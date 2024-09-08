package com.sandeveloper.jsscolab.presentation.Main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.BiasAlignment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sandeveloper.jsscolab.databinding.HorizontalAppItemBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.setImageToCircleImageView
import com.sandeveloper.jsscolab.domain.Modules.app.App

class HorizontalAppAdapter():ListAdapter<Int,HorizontalAppAdapter.AppViewHolder>(ComparatorDiffUtil()) {

    inner class AppViewHolder(private val binding:HorizontalAppItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(app: Int){
            binding.appImg.setImageResource(app)
        }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = HorizontalAppItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = getItem(position)
        app?.let {
            holder.bind(it)
        }
    }
    class ComparatorDiffUtil : DiffUtil.ItemCallback<Int>(){
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem==newItem
        }

    }

}

