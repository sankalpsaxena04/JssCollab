package com.sandeveloper

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.ActivityAuthBinding
import com.sandeveloper.jsscolab.databinding.ActivityMainBinding
import com.sandeveloper.jsscolab.presentation.Main.FragmentPageAdapter
import com.sandeveloper.jsscolab.presentation.Main.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var fragmentPageAdapter: FragmentPageAdapter

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tabLayout = binding.tabLayout
        viewPager2 = binding.mainViewPager
        fragmentPageAdapter = FragmentPageAdapter(
            supportFragmentManager,
            lifecycle

        )
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.baseline_add_box_24))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_chat))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_profile))

        viewPager2.adapter = fragmentPageAdapter

        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab!=null)
                viewPager2.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

    }
}