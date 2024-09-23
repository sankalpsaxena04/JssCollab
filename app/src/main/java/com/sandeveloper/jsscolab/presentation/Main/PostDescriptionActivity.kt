package com.sandeveloper.jsscolab.presentation.Main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sandeveloper.jsscolab.databinding.ActivityPostDescriptionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDescriptionActivity : AppCompatActivity() {

    private val binding : ActivityPostDescriptionBinding by lazy {
        ActivityPostDescriptionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}