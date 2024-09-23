package com.sandeveloper.jsscolab.presentation.Main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentCategoryListBinding
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager.setOnClickedPost
import com.sandeveloper.jsscolab.domain.Modules.PostForView
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.presentation.Main.home.HomeViewModel
import com.sandeveloper.jsscolab.presentation.Main.home.HomeAdapters.PostAdapter
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
        binding.searchButton.setOnClickThrottleBounceListener {
            if(binding.searchbox.visibility==View.VISIBLE){
                binding.searchbox.visibility=View.GONE
                binding.searchButton.setImageResource(R.drawable.baseline_search_24)
                binding.searchButtontoSearch.visibility = View.GONE
                binding.selectCategory.visibility = View.VISIBLE
                binding.createPost.visibility = View.VISIBLE
            }else{

                binding.searchbox.visibility=View.VISIBLE
                binding.searchButton.setImageResource(R.drawable.baseline_search_off_24)
                binding.searchButtontoSearch.visibility = View.VISIBLE
                binding.selectCategory.visibility = View.GONE
                binding.createPost.visibility = View.GONE
            }
        }
        postAdapter = PostAdapter { post ->
            // Save the clicked post using setOnClickedPost
            setOnClickedPost(post)

            // Open the new activity
            val intent = Intent(requireContext(), PostDescriptionActivity::class.java)
            startActivity(intent)
        }

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
        }
        homeViewModel.getMySwaps()
        //homeViewModel.getCoshopPosts(getPostsRequest(null, null, listOf(Endpoints.categories.QuickCommerce,Endpoints.categories.FoodDelivery), null, null, null,null))

        binding.postsRecyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.postsRecyclerView.adapter=postAdapter

        return binding.root

    }

    private fun onClickedPost(postForView: PostForView) {

    }

    private fun generatePosts() = listOf(
        PostForView("Great contribution to the project!", 60000, 50000, "Alice Johnson", "https://example.com/photos/alice.jpg", 4.5, "ProjectX","2024-10-03T21:52:57.000Z"),
        PostForView("Excellent work on the implementation.", 37500, 75000, "Bob Smith", "https://example.com/photos/bob.jpg", 4.8, "ProjectY","2024-10-03T21:52:57.000Z"),
        PostForView("Very helpful in the testing phase.", 3000, 30000, "Carol Davis", "https://example.com/photos/carol.jpg", 4.2, "ProjectZ","2024-10-03T21:52:57.000Z"),
        PostForView("Great support during the launch.", 55000, 60000, "David Wilson", "https://example.com/photos/david.jpg", 4.6, "ProjectA","2024-10-03T21:52:57.000Z"),
        PostForView("Amazing job with the UI/UX.", 2500, 45000, "Eva Martinez", "https://example.com/photos/eva.jpg", 4.7, "ProjectB","2024-10-03T21:52:57.000Z"),
        PostForView("Helped with database management.", 120, 200, "Frank Lee", "https://example.com/photos/frank.jpg", 4.3, "ProjectC","2024-10-03T21:52:57.000Z"),
        PostForView("Great work on the API integration.", 5500, 55000, "Grace Kim", "https://example.com/photos/grace.jpg", 4.4, "ProjectD","2024-10-03T21:52:57.000Z"),
        PostForView("Fantastic effort on user support.", 25000, 35000, "Hannah White", "https://example.com/photos/hannah.jpg", 4.9, "ProjectE","2024-10-03T21:52:57.000Z"),
        PostForView("Excellent job with the analytics.", 5000, 50000, "Ian Brown", "https://example.com/photos/ian.jpg", 4.6, "ProjectF","2024-10-03T21:52:57.000Z"),
        PostForView("Great collaboration on the project.", 7000, 70000, "Judy Green", "https://example.com/photos/judy.jpg", 4.8, "ProjectG","2024-10-03T21:52:57.000Z"),
        PostForView("Helped with the deployment process.", 4000, 40000, "Kevin Anderson", "https://example.com/photos/kevin.jpg", 4.5, "ProjectH","2024-10-03T21:52:57.000Z")
    )


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}