package com.sandeveloper.jsscolab.presentation.Main.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sandeveloper.jsscolab.databinding.ActivityCategoryListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BroadCategoryPosts @Inject constructor() : AppCompatActivity() {

    private  val binding:ActivityCategoryListBinding by lazy{
        ActivityCategoryListBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}