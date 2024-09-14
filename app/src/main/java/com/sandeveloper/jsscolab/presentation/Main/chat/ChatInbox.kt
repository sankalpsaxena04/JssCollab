package com.sandeveloper.jsscolab.presentation.Main.chat

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.domain.Modules.Messages.Message

class ChatInbox : Fragment() {

    companion object {
        fun newInstance() = ChatInbox()
    }

    private lateinit var inboxAdapter: InboxAdapter

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        inboxAdapter =InboxAdapter(listOf(Message("Ranjan","Hello",1726192327273)))

        return inflater.inflate(R.layout.fragment_chat_inbox, container, false)
    }
}