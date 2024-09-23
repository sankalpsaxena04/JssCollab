package com.sandeveloper.jsscolab.presentation.Main.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentChatBinding
import com.sandeveloper.jsscolab.domain.Modules.Messages.MessageEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var roomId: String
    private lateinit var senderId: String
    private lateinit var senderDpUrl: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        // Get roomId, senderId, and senderDpUrl from arguments passed to this fragment
        roomId = arguments?.getString("roomId") ?: ""
        senderId = arguments?.getString("senderId") ?: ""
        senderDpUrl = arguments?.getString("senderDpUrl") ?: ""

        // Setup RecyclerView
        setupRecyclerView()

        // Listen for new messages
        chatViewModel.listenForMessages()

        // Fetch chat messages
        chatViewModel.fetchMessages(roomId)

        // Observe incoming messages
        chatViewModel.messages.observe(viewLifecycleOwner) { messages ->
            val msg: MutableList<MessageEntity> = mutableListOf()
            messages.forEach { message ->
                msg.add(MessageEntity(roomId = roomId, senderId = message.sender, text = message.text, time = message.timeSent, isSender = message.sender == senderId))
            }
            chatAdapter = ChatAdapter(msg)
            binding.recyclerViewMessages.adapter = chatAdapter
            binding.recyclerViewMessages.scrollToPosition(msg.size - 1) // Scroll to last message
        }

        chatViewModel.newMessage.observe(viewLifecycleOwner) { newMessage ->
            chatAdapter.addMessage(MessageEntity(roomId = roomId, senderId = newMessage.sender, text = newMessage.text, time = newMessage.timeSent, isSender = newMessage.sender == senderId))
            binding.recyclerViewMessages.scrollToPosition(chatAdapter.itemCount - 1)
        }

        // Send message logic
        binding.sendButton.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val message = MessageEntity(
                    roomId = roomId,
                    senderId = senderId,
                    text = messageText,
                    time = System.currentTimeMillis(),
                    isSender = true
                )
                chatViewModel.sendMessageSocket(message)

                // Clear input field
                binding.editTextMessage.setText("")
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(requireContext())
        chatAdapter = ChatAdapter(emptyList()) // Initialize with an empty list
        binding.recyclerViewMessages.adapter = chatAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        chatViewModel.disconnectSocket() // Disconnect socket when fragment view is destroyed
    }
}