package com.sandeveloper.jsscolab.presentation.Auth.Signup

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentSignupPhoneNoScreenBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Auth.OtpRequest
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.presentation.Auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupPhoneNoScreen@Inject constructor() : Fragment() {

    companion object {
        fun newInstance() = SignupPhoneNoScreen()
    }
    private val viewModel: AuthViewModel by viewModels()
    private var _binding: FragmentSignupPhoneNoScreenBinding? = null
    private val binding get() = _binding!!


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
            if(PrefManager.getSignForget()){
                getOtp("RESET")
            }else{
                getOtp("SIGNUP")
            }
        }
        viewSetup(PrefManager.getSignForget())
        binding.login.setOnClickThrottleBounceListener{
            findNavController().navigate(R.id.action_signupScreen_to_loginScreen)
        }
    }

    private fun viewSetup(signForget: Boolean) {
        if(signForget){
            binding.loginNav.visibility = View.GONE
            binding.toolbar.btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }else{
            binding.loginNav.visibility = View.VISIBLE
            binding.toolbar.btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun getOtp(action: String) {
        val phoneNo = binding.etPhone.text.toString()
        if(checkPhoneNo(phoneNo)){
            val otpReq = OtpRequest(phoneNo.toLong(),action)

            viewModel.sendOtp(otpReq)
            viewModel.otpResponse.observe(viewLifecycleOwner, Observer {

                when(it){
                    is ServerResult.Failure -> {
                        binding.progressbar.visibility = View.GONE
                        binding.getOtp.isEnabled = true
                        binding.etPhone.isEnabled = true
                        binding.login.isEnabled = true
                        Toast.makeText(requireContext(),it.exception.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    ServerResult.Progress -> {
                        binding.progressbar.visibility = View.VISIBLE
                        binding.getOtp.isEnabled = false
                        binding.etPhone.isEnabled = false
                        binding.login.isEnabled = false
                    }
                    is ServerResult.Success -> {
                        if(it.data.success){

                            findNavController().navigate(R.id.action_signupScreen_to_OTPVerification)
                            viewModel.startTimer()
                            Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })

        }
    }

    private fun checkPhoneNo(phoneNo: String):Boolean {
        val invalid = listOf(1,2,3,4,5)
        if(phoneNo.length!=10||invalid.contains(phoneNo[0].toString().toInt())){
            Toast.makeText(requireContext(),"Enter a valid phone number",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}