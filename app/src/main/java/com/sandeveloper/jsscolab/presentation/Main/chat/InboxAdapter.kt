package com.sandeveloper.jsscolab.presentation.Main.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.databinding.ItemMessageBinding
import com.sandeveloper.jsscolab.domain.Modules.Messages.Message

class InboxAdapter(private val messages:List<Message>):RecyclerView.Adapter<InboxAdapter.InboxViewHolder>() {
    class InboxViewHolder(val binding:ItemMessageBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(message: Message){
            binding.senderName.text = message.sender
            binding.messageTime.text = message.timeSent.toString()
            binding.messagePreview.text = if(message.text.length<36)message.text else message.text.substring(0,36)
           // Glide.with(binding.root.context).load(message.sender.photo).into(binding.profileIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return InboxViewHolder(binding)
    }

    override fun getItemCount(): Int {
          return  messages.size
    }

    override fun onBindViewHolder(holder: InboxViewHolder, position: Int) {
        holder.bind(messages[position])
    }
}