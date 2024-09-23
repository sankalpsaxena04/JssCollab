package com.sandeveloper.jsscolab.presentation.Main.home.HomeAdapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.HorizontalAppItemBinding
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.presentation.Main.home.BroadCategoryPosts

class HorizontalAppAdapter():ListAdapter<Int, HorizontalAppAdapter.AppViewHolder>(ComparatorDiffUtil()) {

    inner class AppViewHolder(private val binding:HorizontalAppItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(app: Int){
            binding.appImg.setImageResource(app)
            binding.root.setOnClickThrottleBounceListener{

                when(app){
                    R.drawable.coshop->PrefManager.setBroadCategory(Endpoints.broadcategories.coshop)
                    R.drawable.quantum_exchange->PrefManager.setBroadCategory(Endpoints.broadcategories.quantum_exchange)
                    R.drawable.shared_cab->PrefManager.setBroadCategory(Endpoints.broadcategories.shared_cab)
                }

                val intent = Intent(binding.root.context, BroadCategoryPosts::class.java)
                binding.root.context.startActivity(intent)
            }
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

