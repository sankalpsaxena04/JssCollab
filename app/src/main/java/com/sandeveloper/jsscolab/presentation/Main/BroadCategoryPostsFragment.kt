package com.sandeveloper.jsscolab.presentation.Main

import android.app.AlertDialog
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentCategoryListBinding
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager.setOnClickedPost
import com.sandeveloper.jsscolab.domain.HelperClasses.toOriginalCategories
import com.sandeveloper.jsscolab.domain.HelperClasses.toPost
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.getPostsRequest
import com.sandeveloper.jsscolab.domain.Modules.PostForView
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapsRequest
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.presentation.Main.home.HomeViewModel
import com.sandeveloper.jsscolab.presentation.Main.home.HomeAdapters.PostAdapter
import com.sandeveloper.jsscolab.presentation.ReportUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BroadCategoryPostsFragment  @Inject constructor() : Fragment() {

    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!
    private lateinit var postAdapter: PostAdapter
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentCategoryListBinding.inflate(inflater,container,false)

        binding.createPost.setOnClickThrottleBounceListener{
            findNavController().navigate(R.id.action_categoryListFragment_to_createPost2)
        }
        binding.backButton.setOnClickThrottleBounceListener{
            requireActivity().finish()
        }

        postAdapter = PostAdapter(
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
                            startActivity(Intent(requireContext(), ReportUser::class.java))
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        )
        binding.postsRecyclerView.adapter = postAdapter


        when(PrefManager.getBroadCategory()){
            Endpoints.broadcategories.coshop->{
                binding.categoryTitle.text= Endpoints.broadcategories.coshop
            }
            Endpoints.broadcategories.quantum_exchange->{
                binding.categoryTitle.text= Endpoints.broadcategories.quantum_exchange
            }
            Endpoints.broadcategories.shared_cab->{
                binding.categoryTitle.text= Endpoints.broadcategories.shared_cab
            }
            Endpoints.broadcategories.my_posts->{
                binding.categoryTitle.text = Endpoints.broadcategories.my_posts
            }
            Endpoints.broadcategories.my_swaps->{
                binding.categoryTitle.text = Endpoints.broadcategories.my_swaps
            }
        }


        binding.postsRecyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.postsRecyclerView.adapter=postAdapter

        return binding.root

    }
    fun setUpViewModel(){
        if(PrefManager.getBroadCategory()==Endpoints.broadcategories.coshop){

            homeViewModel.getCoshopPosts(
                getPostsRequest(
                    null,
                    null,
                    listOf(
                        Endpoints.categories.QuickCommerce.toOriginalCategories(),
                        Endpoints.categories.FoodDelivery.toOriginalCategories(),
                        Endpoints.categories.ECommerce.toOriginalCategories(),
                        Endpoints.categories.Pharmaceuticals.toOriginalCategories()
                    ),
                    null,
                    null,
                    null
                )
            )
        }
        else if(PrefManager.getBroadCategory()==Endpoints.broadcategories.quantum_exchange){
            homeViewModel.getSwaps(getSwapsRequest(null,null,null,null,null))
        }
        else if(PrefManager.getBroadCategory()==Endpoints.broadcategories.my_posts){
            homeViewModel.getMyCoshopPosts()
        }
        else if(PrefManager.getBroadCategory()==Endpoints.broadcategories.my_swaps){
            homeViewModel.getMySwaps()
        }
        else{
            homeViewModel.getCoshopPosts(
                getPostsRequest(
                    null,
                    null,
                    listOf(
                        Endpoints.categories.SharedCab.toOriginalCategories()
                    ),
                    null,
                    null,
                    null
                )
            )
        }
    }
    fun bindObserver(){
    homeViewModel.coshopPostResponse.observe(viewLifecycleOwner, Observer {
        when(it){
            is ServerResult.Failure -> {
                Toast.makeText(requireContext(),it.exception.message,Toast.LENGTH_SHORT).show()
            }
            is ServerResult.Progress -> {
                binding.progressbar.visibility = View.VISIBLE
                binding.postsRecyclerView.visibility = View.GONE
                binding.searchbox.visibility = View.GONE
                binding.createPost.visibility = View.GONE
            }
            is ServerResult.Success -> {
                if(it.data.success){
                    postAdapter.submitList(it.data.posts)
                    binding.progressbar.visibility = View.GONE
                    binding.postsRecyclerView.visibility = View.VISIBLE
                    binding.searchbox.visibility = View.VISIBLE
                    binding.createPost.visibility = View.VISIBLE
                }
                else{
                    Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    })
    homeViewModel.swapPostResponse.observe(viewLifecycleOwner, Observer {
        when(it){
            is ServerResult.Failure -> {
                Log.e("swapError",it.exception.toString())
                Toast.makeText(requireContext(),it.exception.message,Toast.LENGTH_SHORT).show()
            }
            is ServerResult.Progress -> {
                binding.progressbar.visibility = View.VISIBLE
                binding.postsRecyclerView.visibility = View.GONE
                binding.searchbox.visibility = View.GONE
                binding.createPost.visibility = View.GONE
            }
            is ServerResult.Success -> {
                if(it.data.success){
                    Log.e("swapSuccess",it.data.swaps.toString())
                    postAdapter.submitList(it.data.swaps?.map {
                        it.toPost()
                    })
                    binding.progressbar.visibility = View.GONE
                    binding.postsRecyclerView.visibility = View.VISIBLE
                    binding.searchbox.visibility = View.VISIBLE
                    binding.createPost.visibility = View.VISIBLE
                }
                else{
                    Toast.makeText(requireContext(),"here ${it.data.message}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    })

        homeViewModel.commonResponse.observe(viewLifecycleOwner, Observer {
            when(it) {
                is ServerResult.Failure -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT).show()
                }
                is ServerResult.Progress -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is ServerResult.Success -> {
                    if(it.data.success){
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                        setUpViewModel()
                    }
                    else{
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        bindObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}