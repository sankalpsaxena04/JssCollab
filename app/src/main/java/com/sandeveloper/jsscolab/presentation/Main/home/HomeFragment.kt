package com.sandeveloper.jsscolab.presentation.Main.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentHomeBinding
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager.setOnClickedPost
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.getPostsRequest
import com.sandeveloper.jsscolab.domain.Modules.PostForView
import com.sandeveloper.jsscolab.domain.Modules.toPostSummary
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.presentation.Main.PostDescriptionActivity
import com.sandeveloper.jsscolab.presentation.Main.Profile
import com.sandeveloper.jsscolab.presentation.Main.home.HomeAdapters.BannerAdapter
import com.sandeveloper.jsscolab.presentation.Main.home.HomeAdapters.HorizontalAppAdapter
import com.sandeveloper.jsscolab.presentation.Main.home.HomeAdapters.PostAdapter
import com.sandeveloper.jsscolab.presentation.Notification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var appAdapter: HorizontalAppAdapter
    private lateinit var postAdapter: PostAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private val handler = Handler(Looper.getMainLooper())
    private val hints = listOf("Search \"Burger\"", "Search\"Groceries\"", "Search \"towards Station\"", "Search \"2nd sem Quantum\"", "Search \"drafter\"")
    private var hintIndex = 0
    private val handler2 = Handler(Looper.getMainLooper())
    private var currentPosition = Int.MAX_VALUE / 2
    private val delayMillis: Long = 3000
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initializeAdapters()
        setupViewModel()

        Glide.with(requireContext()).load(PrefManager.getcurrentUserdetails().photo?.secure_url).into(binding.topbar.userProfile)
        setupUIInteractions()
        setupRecyclerViews()
        startAutoScroll()
        startHintTextAnimation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (!s.isNullOrEmpty()) {
                    handler2.removeCallbacksAndMessages(null)
                    binding.bar.visibility = View.GONE

                } else {
                    startHintTextAnimation()
                    binding.bar.visibility = View.VISIBLE

                }


            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    private fun setupViewModel() {
        homeViewModel.getCoshopPosts(getPostsRequest(null,  null, null, null, null, null))
        homeViewModel.coshopPostResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ServerResult.Failure ->{
                    Toast.makeText(requireContext(),result.exception.toString(),Toast.LENGTH_SHORT).show()
                    showContentView(true)
                }
                ServerResult.Progress -> showLoadingView(true)
                is ServerResult.Success -> {
                    if(result.data.success){
                        showContentView(true)
                        postAdapter.submitList(result.data.posts)
                    }
                    else{
                        Toast.makeText(requireContext(),result.data.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initializeAdapters() {
        appAdapter = HorizontalAppAdapter()
        postAdapter = PostAdapter(
            onPostClicked = { post ->
                // Save the clicked post using setOnClickedPost
                setOnClickedPost(post)
                // Open the new activity
                val intent = Intent(requireContext(), PostDescriptionActivity::class.java)
                startActivity(intent)
            },
            onPostLongClicked = { post ->
                    AlertDialog.Builder(requireContext())
                        .setTitle("Report User")
                        .setMessage("Do you want to report this user?")
                        .setPositiveButton("Yes") { _, _ ->
                            findNavController().navigate(R.id.action_homeFragment_to_reportUser)
                        }
                        .setNegativeButton("No", null)
                        .show()

            }
        )
        bannerAdapter = BannerAdapter(listOf(
            R.drawable.food_post,
            R.drawable.exchange_banner,
            R.drawable.team_up_banner,
            R.drawable.share_ride_banner
        ))
    }

    private fun setupUIInteractions() {
        binding.topbar.notificationButton.setOnClickThrottleBounceListener {
            startActivity(Intent(requireContext(), Notification::class.java))
        }
        binding.topbar.userProfile.setOnClickThrottleBounceListener {
            startActivity(Intent(requireContext(), Profile::class.java))
        }
        binding.searchBarBg.setOnClickThrottleBounceListener {
            startActivity(Intent(requireContext(),SearchActivity::class.java))
        }
        binding.searchBar.setOnClickThrottleBounceListener {
            startActivity(Intent(requireContext(),SearchActivity::class.java))
        }
    }

    private fun setupRecyclerViews() {
        setupHorizontalIconsRecyclerView()
        setupBannerRecyclerView()
        setupPostsRecyclerView()
    }

    private fun setupHorizontalIconsRecyclerView() {
        binding.recyclerViewIcons.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = appAdapter
        }
        appAdapter.submitList(listOf(R.drawable.coshop, R.drawable.quantum_exchange, R.drawable.shared_cab))
    }

    private fun setupBannerRecyclerView() {
        binding.bannerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bannerAdapter
            scrollToPosition(currentPosition)
            LinearSnapHelper().attachToRecyclerView(this)
            addOnScrollListener(autoScrollListener)
        }
    }

    private fun setupPostsRecyclerView() {
        binding.recyclerViewPosts.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            adapter = postAdapter

        }
    }

    private fun showContentView(isVisible: Boolean) {
        binding.screenView.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.progressBar.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    private fun showLoadingView(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.screenView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }


    private fun startAutoScroll() {
        handler.postDelayed(autoScrollRunnable, delayMillis)
    }

    private fun stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable)
    }

    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            currentPosition++
            binding.bannerRecyclerView.smoothScrollToPosition(currentPosition)
            handler.postDelayed(this, delayMillis)
        }
    }

    private val autoScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> startAutoScroll()
                else -> stopAutoScroll()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }

    override fun onDestroyView() {
        stopAutoScroll()
        handler2.removeCallbacksAndMessages(null)
        _binding = null
        super.onDestroyView()
    }
    private fun startHintTextAnimation() {
        val runnable = object : Runnable {
            override fun run() {
                val slideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                val slideDown = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)

                binding.searchBarBg.startAnimation(slideUp)

                slideUp.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                    override fun onAnimationStart(animation: android.view.animation.Animation?) {}

                    override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                        hintIndex = (hintIndex + 1) % hints.size
                        binding.searchBarBg.text = hints[hintIndex]

                        binding.searchBarBg.startAnimation(slideDown)
                    }

                    override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
                })

                handler2.postDelayed(this, 2000)
            }
        }

        handler2.post(runnable)
    }
}
