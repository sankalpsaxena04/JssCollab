package com.sandeveloper.jsscolab.presentation.Auth.ChooseScreen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sandeveloper.MainActivity
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentChooseBinding
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.animFadein
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChooseFragment @Inject constructor() : Fragment() {


    private var _binding: FragmentChooseBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()
    }

    private fun setUpViews() {

        binding.splashBg.animFadein(requireActivity(),2000)

        binding.title.animFadein(requireContext(),2000)
        binding.btnLogin.setOnClickThrottleBounceListener {

            findNavController().navigate(R.id.action_chooserFragment_to_loginScreen)
        }

        binding.btnGetStarted.setOnClickThrottleBounceListener {
            findNavController().navigate(R.id.action_chooserFragment_to_signupScreen)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
