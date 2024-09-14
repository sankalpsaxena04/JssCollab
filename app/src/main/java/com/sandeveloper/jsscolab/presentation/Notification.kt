package com.sandeveloper.jsscolab.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.ActivityNotificationBinding
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickFadeInListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Notification : AppCompatActivity() {

    private val binding: ActivityNotificationBinding by lazy {
        ActivityNotificationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tabUnread.setOnClickFadeInListener {
            binding.tabUnread.setBackgroundResource(R.drawable.filled_gray_box)
            binding.tabAll.setBackgroundResource(R.color.transparent)

        }
        binding.tabAll.setOnClickFadeInListener {
            binding.tabAll.setBackgroundResource(R.drawable.filled_gray_box)

            binding.tabUnread.setBackgroundResource(R.color.transparent)
        }

    }
}