package com.sandeveloper.jsscolab.presentation.Main.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.JssColabApplication
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.data.Room.ChatDatabase
import com.sandeveloper.jsscolab.databinding.FragmentChatBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Messages.SendMessageRequest
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import dagger.hilt.android.AndroidEntryPoint
import com.sandeveloper.jsscolab.data.Room.Message
import com.sandeveloper.jsscolab.domain.Modules.Messages.MessageSend
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private val TAG = "SocketIO"

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter

    private var messageList = emptyList<Message>()
    private val roomId =PrefManager.getSelectedRoomId()!!
    private val jwtToken = PrefManager.getToken()!!
    private val viewModel: ChatViewModel by viewModels()

    @Inject
    lateinit var chatDatabase: ChatDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        viewModel.initialize()
        viewModel.getMessageFromRoom(roomId)
        viewModel.socketConnected.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    viewModel.joinRoom(roomId,jwtToken)
                }
                false -> {
                    Toast.makeText(requireContext(),"Unable to connect,",Toast.LENGTH_SHORT).show()
                }
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = ChatAdapter(messageList)
        viewModel.messageList.observe(viewLifecycleOwner, Observer {
            messageList = it
            it.forEach { msg->
                chatAdapter.addMessage(msg)
                binding.recyclerViewMessages.scrollToPosition(chatAdapter.itemCount - 1)
            }
        })
        binding.senderName.text = PrefManager.getTextUser().full_name
        binding.backButton.setOnClickThrottleBounceListener {
            requireActivity().finish()
        }
        Glide.with(requireContext()).load(PrefManager.getTextUser().photo?.secure_url?:"").placeholder(R.drawable.profile_pic_placeholder).into(binding.senderProfileImage)
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMessages.adapter = chatAdapter

        viewModel.cameOnlineCount.observe(viewLifecycleOwner, Observer {
            when(it){
                true ->{
                    binding.status.text = "Online"
                }
                false ->{
                    binding.status.text = "Offline"
                }
            }
        })
        viewModel.sentMessage.observe(viewLifecycleOwner, Observer {
            when(it){
                is ServerResult.Failure -> {
                    Toast.makeText(requireContext(),it.exception.message,Toast.LENGTH_SHORT).show()
                }
                is ServerResult.Progress -> {}
                is ServerResult.Success -> {
                    Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.sendButton.setOnClickThrottleBounceListener{
            val message = binding.editTextMessage.text.toString()
            val time = System.currentTimeMillis()
            if(viewModel.cameOnlineCount.value == true){
                viewModel.sendMessage(roomId,MessageSend( message, Date(time)))
            }
            else{
                PrefManager.getSelectedRoomId()
                    ?.let { SendMessageRequest(it,message,time) }?.let { viewModel.sendMessage(it) }
            }
            val msg = Message(roomId = roomId, isSender = true, message = message, timeSent =time, isRead = true)
            chatAdapter.addMessage(msg)
            binding.recyclerViewMessages.scrollToPosition(chatAdapter.itemCount - 1)
            viewModel.insertMessage(msg)
            binding.editTextMessage.text.clear()
        }
        viewModel.receivedMessage.observe(viewLifecycleOwner) { message ->
            val msg = Message(roomId = roomId, isSender = false, message = message.text, timeSent =message.timeSent.time, isRead = true)
            viewModel.insertMessage(msg)
            chatAdapter.addMessage(msg)
            binding.recyclerViewMessages.scrollToPosition(chatAdapter.itemCount - 1)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.leaveRoom(roomId)
        _binding = null

    }

    override fun onPause() {
        super.onPause()
        viewModel.leaveRoom(roomId)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.leaveRoom(roomId)
    }

    override fun onResume() {
        super.onResume()
        viewModel.joinRoom(roomId,jwtToken)
    }



}