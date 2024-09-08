package com.sandeveloper.jsscolab.presentation.Main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentHomeBinding
import com.sandeveloper.jsscolab.domain.Modules.PostForView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HorizontalAppAdapter
    private lateinit var postAdapter: PostAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var currentPosition = Int.MAX_VALUE / 2
    private val delayMillis: Long = 3000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupViewModel()
        setupAdapters()
        setupRecyclerViews()
        startAutoScroll()
        return binding.root
    }

    private fun setupViewModel() {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private fun setupAdapters() {
        adapter = HorizontalAppAdapter()
        postAdapter = PostAdapter(generatePosts())
        bannerAdapter = BannerAdapter(listOf(R.drawable.placeholder, R.drawable.placeholder, R.drawable.placeholder))
    }

    private fun setupRecyclerViews() {
        setupHorizontalIconsRecyclerView()
        setupBannerRecyclerView()
        setupPostsRecyclerView()
    }

    private fun setupHorizontalIconsRecyclerView() {
        binding.recyclerViewIcons.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@HomeFragment.adapter
        }
        val apps = listOf(R.drawable.coshop, R.drawable.quantum_exchange, R.drawable.shared_cab)
        adapter.submitList(apps)
    }

    private fun setupBannerRecyclerView() {
        binding.bannerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@HomeFragment.bannerAdapter
            scrollToPosition(currentPosition)
            LinearSnapHelper().attachToRecyclerView(this)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> startAutoScroll()
                        else -> stopAutoScroll()
                    }
                }
            })
        }
    }
    private fun setupPostsRecyclerView() {
        binding.recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@HomeFragment.postAdapter
        }
    }

    private fun startAutoScroll() {
        handler.postDelayed(scrollRunnable, delayMillis)
    }
    private fun stopAutoScroll() {
        handler.removeCallbacks(scrollRunnable)
    }
    private val scrollRunnable = object : Runnable {
        override fun run() {
            currentPosition++
            binding.bannerRecyclerView.smoothScrollToPosition(currentPosition)
            handler.postDelayed(this, delayMillis)
        }
    }

    private fun generatePosts() = listOf(
        PostForView("Great contribution to the project!", "₹5,000", "₹50,000", "Alice Johnson", "https://example.com/photos/alice.jpg", 4.5, "ProjectX"),
        PostForView("Excellent work on the implementation.", "₹7,500", "₹75,000", "Bob Smith", "https://example.com/photos/bob.jpg", 4.8, "ProjectY"),
        PostForView("Very helpful in the testing phase.", "₹3,000", "₹30,000", "Carol Davis", "https://example.com/photos/carol.jpg", 4.2, "ProjectZ"),
        PostForView("Great support during the launch.", "₹6,000", "₹60,000", "David Wilson", "https://example.com/photos/david.jpg", 4.6, "ProjectA"),
        PostForView("Amazing job with the UI/UX.", "₹4,500", "₹45,000", "Eva Martinez", "https://example.com/photos/eva.jpg", 4.7, "ProjectB"),
        PostForView("Helped with database management.", "₹4,000", "₹40,000", "Frank Lee", "https://example.com/photos/frank.jpg", 4.3, "ProjectC"),
        PostForView("Great work on the API integration.", "₹5,500", "₹55,000", "Grace Kim", "https://example.com/photos/grace.jpg", 4.4, "ProjectD"),
        PostForView("Fantastic effort on user support.", "₹3,500", "₹35,000", "Hannah White", "https://example.com/photos/hannah.jpg", 4.9, "ProjectE"),
        PostForView("Excellent job with the analytics.", "₹5,000", "₹50,000", "Ian Brown", "https://example.com/photos/ian.jpg", 4.6, "ProjectF"),
        PostForView("Great collaboration on the project.", "₹7,000", "₹70,000", "Judy Green", "https://example.com/photos/judy.jpg", 4.8, "ProjectG"),
        PostForView("Helped with the deployment process.", "₹4,000", "₹40,000", "Kevin Anderson", "https://example.com/photos/kevin.jpg", 4.5, "ProjectH")
    )
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
        _binding = null
        super.onDestroyView()
    }
}
