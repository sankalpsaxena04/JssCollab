package com.sandeveloper.jsscolab.presentation.Main.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentSearchBinding
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager.setOnClickedPost
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.getPostsRequest
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.presentation.Main.PostDescriptionActivity
import com.sandeveloper.jsscolab.presentation.Main.home.HomeAdapters.PostAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding?=null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater,container,false)
        binding.img.visibility = View.GONE
        adapter = PostAdapter(
            onPostClicked = { post ->
                // Save the clicked post using setOnClickedPost
                setOnClickedPost(post)
                // Open the new activity
                val intent = Intent(requireContext(), PostDescriptionActivity::class.java)
                startActivity(intent)
            },
            onPostLongClicked = { post ->
                if (PrefManager.getBroadCategory() == Endpoints.broadcategories.my_posts) {
                    // Show delete confirmation dialog
                    AlertDialog.Builder(requireContext())
                        .setTitle("Delete Post")
                        .setMessage("Are you sure you want to delete this post?")
                        .setPositiveButton("Yes") { _, _ ->
                            homeViewModel.deleteCoshopPost(post._id)
                        }
                        .setNegativeButton("No", null)
                        .show()
                } else {
                    // Show report user dialog
                    AlertDialog.Builder(requireContext())
                        .setTitle("Report User")
                        .setMessage("Do you want to report this user?")
                        .setPositiveButton("Yes") { _, _ ->
                            //TODO()
                            findNavController().navigate(R.id.action_categoryListFragment_to_reportUser2)
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        )
        binding.postRecycleView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.postRecycleView.adapter=adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchBtn.setOnClickThrottleBounceListener{
            val key:String = binding.searchText.text.toString()
                homeViewModel.getCoshopPosts(getPostsRequest(key,null,null,null,null,null))
        }
        bindObserver()
    }
    fun bindObserver(){
        homeViewModel.coshopPostResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is ServerResult.Failure -> {
                    binding.img.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.searchBtn.isEnabled = true
                    binding.searchText.isEnabled = true
                    Toast.makeText(requireContext(),it.exception.message,Toast.LENGTH_SHORT).show()
                }
                is ServerResult.Progress -> {
                    binding.img.visibility = View.GONE
                    adapter.submitList(emptyList())
                    binding.progressBar.visibility = View.VISIBLE
                    binding.searchBtn.isEnabled = false
                    binding.searchText.isEnabled = false
                }
                is ServerResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.searchBtn.isEnabled = true
                    binding.searchText.isEnabled = true
                    if(it.data.success){
                        if(it.data.posts.isNullOrEmpty()){
                            binding.img.visibility = View.VISIBLE
                        }
                        else{
                            adapter.submitList(it.data.posts)
                        }
                    }

                }
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}