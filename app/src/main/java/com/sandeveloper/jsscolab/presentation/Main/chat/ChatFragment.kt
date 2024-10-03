package com.sandeveloper.jsscolab.presentation.Main.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sandeveloper.jsscolab.JssColabApplication
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentChatBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Modules.Messages.Message
import com.sandeveloper.jsscolab.domain.Modules.Messages.MessageEntity
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.net.URISyntaxException

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var mSocket: Socket
    private val TAG = "SocketIO"

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter

    private lateinit var messageList: MutableList<MessageEntity>
    private val roomId =PrefManager.getSelectedRoomId()!!
    private val jwtToken = PrefManager.getToken()!!
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        viewModel.joinRoom(roomId,jwtToken)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageList = mutableListOf()
        chatAdapter = ChatAdapter(messageList)
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMessages.adapter = chatAdapter

        binding.sendButton.setOnClickThrottleBounceListener{
            val message = binding.editTextMessage.text.toString()
            val time = System.currentTimeMillis()
            viewModel.sendMessage(Message("me", message, time))
            chatAdapter.addMessage(MessageEntity(roomId = roomId, senderId = "", text = message, time=time, isSender =  true))
        }
        viewModel.receivedMessage.observe(viewLifecycleOwner) { message ->
            chatAdapter.addMessage(MessageEntity(roomId = roomId, senderId = message.sender, text = message.text, time=message.timeSent, isSender =  false))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }



}