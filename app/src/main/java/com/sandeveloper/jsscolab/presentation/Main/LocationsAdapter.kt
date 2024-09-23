package com.sandeveloper.jsscolab.presentation.Main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandeveloper.jsscolab.databinding.ItemLocationBinding

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.LocationViewHolder>() {

    private var locations: List<String> = listOf()

    inner class LocationViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(location: String) {
            binding.locationTextView.text = location
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(locations[position])
    }

    override fun getItemCount(): Int = locations.size

    fun submitList(locationList: List<String>) {
        locations = locationList
        notifyDataSetChanged()
    }
}
