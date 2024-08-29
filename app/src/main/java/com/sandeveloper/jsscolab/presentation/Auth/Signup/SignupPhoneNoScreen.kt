package com.sandeveloper.jsscolab.presentation.Auth.Signup

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentSignupPhoneNoScreenBinding
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupPhoneNoScreen@Inject constructor() : Fragment() {

    companion object {
        fun newInstance() = SignupPhoneNoScreen()
    }
    private var _binding: FragmentSignupPhoneNoScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupPhoneNoScreenBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.getOtp.setOnClickThrottleBounceListener{
            findNavController().navigate(R.id.action_signupScreen_to_OTPVerification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}