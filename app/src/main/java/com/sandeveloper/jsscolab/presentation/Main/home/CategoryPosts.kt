package com.sandeveloper.jsscolab.presentation.Main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentCategoryPostsBinding

class CategoryPosts : Fragment() {

    private var _binding:FragmentCategoryPostsBinding? = null
    private val binding = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        FragmentCategoryPostsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}