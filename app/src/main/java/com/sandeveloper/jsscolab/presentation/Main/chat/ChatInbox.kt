package com.sandeveloper.jsscolab.presentation.Main.chat

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentChatInboxBinding
import com.sandeveloper.jsscolab.domain.Modules.Messages.Message
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatInbox : Fragment() {

    private var _binding: FragmentChatInboxBinding? = null
    private val binding get() = _binding!!
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var inboxAdapter: InboxAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the RecyclerView
        setupRecyclerView()

        // Observe the list of chat rooms/messages from ViewModel
        chatViewModel.messages.observe(viewLifecycleOwner) { messages ->
            inboxAdapter = InboxAdapter(messages, requireContext())
            binding.recyclerView.adapter = inboxAdapter
        }

        // Fetch messages (room list in this case)
        chatViewModel.fetchRoomId("user_id_here")  // Assuming the user ID is available

        chatViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            // Handle error appropriately (show toast/snackbar)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        inboxAdapter = InboxAdapter(emptyList(), requireContext()) // Initialize with empty list
        binding.recyclerView.adapter = inboxAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}