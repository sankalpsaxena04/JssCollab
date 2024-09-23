package com.sandeveloper.jsscolab.presentation.Main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandeveloper.jsscolab.databinding.ItemApplicationBinding

class ApplicationsAdapter : RecyclerView.Adapter<ApplicationsAdapter.ApplicationViewHolder>() {

    private var applicationNames: List<String> = listOf()

    inner class ApplicationViewHolder(private val binding: ItemApplicationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(applicationName: String) {
            binding.applicationNameTextView.text = applicationName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val binding = ItemApplicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApplicationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        holder.bind(applicationNames[position])
    }

    override fun getItemCount(): Int = applicationNames.size

    fun submitList(applications: List<String>) {
        applicationNames = applications
        notifyDataSetChanged()
    }
}
