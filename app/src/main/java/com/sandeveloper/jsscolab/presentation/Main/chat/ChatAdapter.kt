package com.sandeveloper.jsscolab.presentation.Main.chat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.data.Room.Message
import com.sandeveloper.jsscolab.databinding.ItemMessageReceiverBinding
import com.sandeveloper.jsscolab.databinding.ItemMessageSenderBinding
import com.sandeveloper.jsscolab.domain.Modules.Messages.MessageSend
import com.sandeveloper.jsscolab.domain.Utility.DateTimeUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter(private var messageList: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_SENDER = 1
        const val VIEW_TYPE_RECEIVER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].isSender) VIEW_TYPE_SENDER else VIEW_TYPE_RECEIVER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENDER) {
            val binding = ItemMessageSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SenderViewHolder(binding)
        } else {
            val binding = ItemMessageReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceiverViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder is SenderViewHolder) {
            holder.bind(message)
        } else if (holder is ReceiverViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messageList.size

    // Add new message to the list dynamically
    fun addMessage(newMessage: Message) {
        messageList = messageList + newMessage
        notifyItemInserted(messageList.size - 1)
    }

    class SenderViewHolder(private val binding: ItemMessageSenderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.textViewMessage.text = message.message
            binding.textViewTimestamp.text = DateTimeUtils.formatTime(message.timeSent, "MM-dd HH:mm")
        }
    }

    class ReceiverViewHolder(private val binding: ItemMessageReceiverBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.textViewMessage.text = message.message
            binding.textViewTimestamp.text = SimpleDateFormat("dd-MM HH:mm",Locale.getDefault()).format(Date(message.timeSent))

//            // Load sender's profile picture using Glide
//            Glide.with(binding.root.context)
//                .load(message.sender_dp_url) // Load image from URL
//                .into(binding.) // ImageView for profile picture
        }
    }
}