package com.sandeveloper.jsscolab.presentation.Main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentMoreBinding

class More : Fragment() {
    private var _binding:FragmentMoreBinding?= null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoreBinding.inflate(inflater,container,false)



        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}