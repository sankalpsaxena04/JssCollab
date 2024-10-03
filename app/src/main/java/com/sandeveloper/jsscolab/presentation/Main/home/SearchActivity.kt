package com.sandeveloper.jsscolab.presentation.Main.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val binding: ActivitySearchBinding by lazy{
        ActivitySearchBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}