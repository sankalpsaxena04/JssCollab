package com.sandeveloper.jsscolab.presentation.Main.chat

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sandeveloper.MainActivity
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.data.Room.ChatDatabase
import com.sandeveloper.jsscolab.databinding.FragmentChatInboxBinding
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Messages.MessageSend
import com.sandeveloper.jsscolab.presentation.StartScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ChatInbox : Fragment() {

    private var _binding: FragmentChatInboxBinding? = null
    private val binding get() = _binding!!
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var inboxAdapter: InboxAdapter
    private val startViewModel:StartScreenViewModel by viewModels()
    @Inject
    lateinit var chatDatabase: ChatDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatInboxBinding.inflate(inflater, container, false)

        inboxAdapter = InboxAdapter{
            CoroutineScope(Dispatchers.IO).launch {

            }
            startActivity(Intent(requireContext(), Chat::class.java))
        }
        chatViewModel.getInbox()
        binding.recyclerView.adapter = inboxAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.refreshFragment.setOnRefreshListener {
        refreshData()
        }
        return binding.root
    }

    private fun refreshData() {
        startViewModel.getMessages().also {
            chatViewModel.getInbox()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                chatViewModel.searchInbox(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        startViewModel.messageResponse.observe(viewLifecycleOwner, Observer {
            when(it) {
                is ServerResult.Failure -> {
                    startViewModel.getMessages()
                }
                ServerResult.Progress -> {}
                is ServerResult.Success -> {
                    binding.refreshFragment.isRefreshing = false
                }
            }
        })
        chatViewModel.inboxData.observe(viewLifecycleOwner, Observer {
            Log.d("InboxData", it.toString())
            inboxAdapter.submitList(it)
        })

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}