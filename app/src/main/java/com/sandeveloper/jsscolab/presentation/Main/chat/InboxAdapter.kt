package com.sandeveloper.jsscolab.presentation.Main.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.databinding.ItemMessageBinding
import com.sandeveloper.jsscolab.domain.Modules.Messages.Message

class InboxAdapter(
    private val messages: List<Message>,
    private val context: Context // Pass context to start an activity
) : RecyclerView.Adapter<InboxAdapter.InboxViewHolder>() {

    class InboxViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.senderName.text = message.sender
            binding.messageTime.text = message.timeSent.toString()
            binding.messagePreview.text = if (message.text.length < 36) message.text else message.text.substring(0, 36)
            // Uncomment and modify this line to load profile image if needed
            // Glide.with(binding.root.context).load(message.sender.photo).into(binding.profileIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InboxViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: InboxViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)

        // Add click listener for opening chat activity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, Chat::class.java)
            intent.putExtra("senderId", message.sender) // Assuming `senderId` is available in the Message model
            intent.putExtra("senderName", message.sender) // Passing the sender's name
            context.startActivity(intent)
        }
    }
}
