package com.sandeveloper.jsscolab.presentation.Main

import android.content.Intent
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
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentPostDescriptionBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.Posts
import com.sandeveloper.jsscolab.presentation.Main.chat.Chat
import com.sandeveloper.jsscolab.presentation.Main.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
@AndroidEntryPoint
class PostDescription : Fragment() {

    private lateinit var binding: FragmentPostDescriptionBinding
    private lateinit var applicationsAdapter: ApplicationsAdapter
    private lateinit var locationsAdapter: LocationsAdapter
    private val chatViewModel by viewModels<ChatViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDescriptionBinding.inflate(inflater, container, false)

        setupRecyclerViews()

        // Retrieve the clicked post from PrefManager
        val clickedPost = PrefManager.getOnClickedPost()

        // Populate the UI with the clicked post data
         populatePostDetails(clickedPost)

        return binding.root
    }

    private fun setupRecyclerViews() {
        // Setup Applications RecyclerView
        applicationsAdapter = ApplicationsAdapter()
        binding.applicationRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = applicationsAdapter
        }

        // Setup Locations RecyclerView
        locationsAdapter = LocationsAdapter()
        binding.locationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = locationsAdapter
        }
    }

    private fun populatePostDetails(post:Posts) {
        // Populate the data into UI elements
        binding.creatorName.text = "Name: ${post.sender?.full_name?:PrefManager.getcurrentUserdetails().full_name!!}"
        binding.expirationJourneyTime.text = "Post Expires in: ${getExpirationTime(post.expiration_date)}"
        binding.address.text = "Location: ${post.sender?.address?:PrefManager.getcurrentUserdetails().address!!}"
        binding.postCategory.text = "Post Category: ${post.category}"
        binding.postDescription.text = post.comment

        binding.textUser.setOnClickListener {
            chatViewModel.fetchRoomId(post.sender!!._id)
        }
        // Load profile picture and placeholder
        Glide.with(requireContext())
            .load(post.sender?.photo?.secure_url?:"")
            .placeholder(R.drawable.profile_pic_placeholder)
            .into(binding.creatorProfilePic)

        // Populate Applications and Locations RecyclerViews
        if (!post.apps.isNullOrEmpty()) {

            applicationsAdapter.submitList(post.apps)
        }
        else{
            binding.applicationRecyclerView.visibility = View.GONE
            binding.applicationsTextView.visibility = View.GONE
        }
        locationsAdapter.submitList(post.filter!!.address)

        chatViewModel.roomId.observe(viewLifecycleOwner, Observer {
            when(it){
                is ServerResult.Failure -> {
                    Log.d("roomId",it.exception.message.toString())
                    Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT).show()
                }
                is ServerResult.Progress -> {}
                is ServerResult.Success -> {
                    if(it.data.success){
                        PrefManager.setSelectedRoomId(it.data.room_id!!)
                        val intent = Intent(requireContext(), Chat::class.java)
                        startActivity(intent)
                    }
                    else{
                        Log.d("roomId",it.data.message)
                        Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
    fun getExpirationTime(isoString: String): String {
        val dateTime = LocalDateTime.parse(isoString, DateTimeFormatter.ISO_DATE_TIME)
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val duration = Duration.between(now, dateTime)

        return when {
            duration.toDays() > 0 -> "Expires in ${duration.toDays()} day(s)"
            duration.toHours() > 0 -> "Expires in ${duration.toHours()} hour(s)"
            duration.toMinutes() > 0 -> "Expires in ${duration.toMinutes()} minute(s)"
            else -> "Expired"
        }
    }
}
