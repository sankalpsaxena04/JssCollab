package com.sandeveloper.jsscolab.presentation.Main.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentHomeBinding
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
                    Toast.makeText(requireContext(),result.data.message,Toast.LENGTH_SHORT).show()
                    showContentView(true)
                    postAdapter.submitList(result.data.posts)
                }
            }
        }
    }

    private fun initializeAdapters() {
        appAdapter = HorizontalAppAdapter()
        postAdapter = PostAdapter { post ->
            // Save the clicked post using setOnClickedPost
            setOnClickedPost(post)

            // Open the new activity
            val intent = Intent(requireContext(), PostDescriptionActivity::class.java)
            startActivity(intent)
        }
        bannerAdapter = BannerAdapter(listOf(
            "https://example.com/photos/alice.jpg",
            "https://example.com/photos/alice.jpg",
            "https://example.com/photos/alice.jpg"
        ))
    }

    private fun setupUIInteractions() {
        binding.topbar.notificationButton.setOnClickThrottleBounceListener {
            startActivity(Intent(requireContext(), Notification::class.java))
        }
        binding.topbar.userProfile.setOnClickThrottleBounceListener {
            startActivity(Intent(requireContext(), Profile::class.java))
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

    private fun onPostClicked(post: PostForView) {
        Toast.makeText(requireContext(), "Post clicked: ${post.senderContribution}", Toast.LENGTH_SHORT).show()
    }

    private fun generateSamplePosts() = listOf(
        PostForView("Great contribution to the project!", 60000, 50000, "Alice Johnson", "https://example.com/photos/alice.jpg", 4.5, "ProjectX","2024-10-03T21:52:57.000Z" ),
        PostForView("Excellent work on the implementation.", 37500, 75000, "Bob Smith", "https://example.com/photos/bob.jpg", 4.8, "ProjectY" ,"2024-10-03T21:52:57.000Z"),
        PostForView("Very helpful in the testing phase.", 3000, 30000, "Carol Davis", "https://example.com/photos/carol.jpg", 4.2, "ProjectZ" ,"2024-10-03T21:52:57.000Z"),
        PostForView("Great support during the launch.", 55000, 60000, "David Wilson", "https://example.com/photos/david.jpg", 4.6, "ProjectA" ,"2024-10-03T21:52:57.000Z"),
        PostForView("Amazing job with the UI/UX.", 2500, 45000, "Eva Martinez", "https://example.com/photos/eva.jpg", 4.7, "ProjectB","2024-10-03T21:52:57.000Z")
    )

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
        // Runnable task to update the hint text every 2 seconds
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

                // Schedule the next hint change after 2 seconds
                handler2.postDelayed(this, 2000)
            }
        }

        // Start the runnable immediately
        handler2.post(runnable)
    }



}
