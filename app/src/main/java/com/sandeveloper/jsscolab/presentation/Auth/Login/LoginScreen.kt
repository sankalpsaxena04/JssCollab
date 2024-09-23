package com.sandeveloper.jsscolab.presentation.Auth.Login

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sandeveloper.MainActivity
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentLoginScreenBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Auth.LoginRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.CreateProfile
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.presentation.Auth.AuthViewModel
import com.sandeveloper.jsscolab.presentation.Main.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginScreen @Inject constructor(): Fragment() {

    private var _binding: FragmentLoginScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel:AuthViewModel by viewModels()
    private val profileViewModel:ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        binding.toolbar.btnBack.setOnClickThrottleBounceListener{
            findNavController().popBackStack()
        }
        binding.SignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreen_to_signupScreen)

        }
        binding.login.setOnClickThrottleBounceListener {
            if(datacheck()){
                viewModel.login(LoginRequest(binding.etPhone.text.toString(),binding.password.text.toString()))
            }
        }
        return binding.root
    }

    private fun datacheck(): Boolean {
//        val invalid = listOf(1,2,3,4,5)
//        if(binding.etPhone.text.toString().length!=10||invalid.contains(binding.etPhone.text.first().digitToInt())){
//            Toast.makeText(requireContext(),"Enter a valid phone number", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        else
            if(binding.password.text.toString().length<8){
            Toast.makeText(requireContext(),"Password should have minimum 8 characters",Toast.LENGTH_SHORT).show()
            return false
        }
        return true

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObserver()
    }

    private fun bindObserver() {
        viewModel.authResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visibility=View.GONE
            when(it){
                is ServerResult.Failure -> {
                    Toast.makeText(requireContext(),it.exception.message.toString(),Toast.LENGTH_SHORT).show()
                    binding.progressbar.visibility = View.GONE

                }
                ServerResult.Progress -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.login.isEnabled = false


                }
                is ServerResult.Success -> {
                    Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                    Log.d("profilepicture",PrefManager.getToken()!!)
                    profileViewModel.getMyDetails()
                }
            }
        })
        profileViewModel.profileResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is ServerResult.Failure -> {
                    profileViewModel.getMyDetails()
                }
                ServerResult.Progress -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.login.isEnabled = false
                }
                is ServerResult.Success -> {
                    Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                    Log.d("profilepicture",PrefManager.getToken()!!)
                    val intent = Intent(requireContext(),MainActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}