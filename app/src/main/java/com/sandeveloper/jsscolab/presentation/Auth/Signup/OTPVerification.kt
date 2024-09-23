package com.sandeveloper.jsscolab.presentation.Auth.Signup

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentOTPVerificationBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.presentation.Auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OTPVerification@Inject constructor() : Fragment() {

    private var resendClickCount = 0
    private val maxResendAttempts = 4
    private val resendCooldownTime = 30000L // 30 seconds
    private var isResendActive = false
    private var otp=""
    private var countDownTimer: CountDownTimer? = null
    private val viewModel: AuthViewModel by viewModels()

    private var _binding: FragmentOTPVerificationBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOTPVerificationBinding.inflate(inflater,container,false)
        binding.btnVerifyOtp.isEnabled = false
        binding.btnVerifyOtp.setOnClickThrottleBounceListener{
            viewModel.verifyOtp(PrefManager.getPhoneNumber()!!.toLong(),otp.toInt(),PrefManager.getOtpOrderId()!!)

        }

        binding.tvOtpInstructions.setText("Enter the 6-digit code sent to your number +91 ${PrefManager.getPhoneNumber()}")
        binding.tvResendOtp.setOnClickListener {
            if(binding.tvResendOtp.isEnabled){
                viewModel.resendOtp(PrefManager.getOtpOrderId()!!)
            }
        }
        viewModel.otpResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is ServerResult.Failure ->{
                    binding.btnVerifyOtp.isEnabled = true
                    binding.progressbar.visibility = View.GONE
                    binding.tvResendOtp.isEnabled = true
                    Toast.makeText(requireContext(),it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                ServerResult.Progress -> {
                    binding.btnVerifyOtp.isEnabled = false
                    binding.progressbar.visibility = View.VISIBLE
                    binding.tvResendOtp.isEnabled = false
                }
                is ServerResult.Success -> {
                    Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_LONG).show()
                }
            }
        })
        viewModel.otpVerifiedResponse.observe(viewLifecycleOwner){
            when(it){
                is ServerResult.Failure -> {
                    binding.btnVerifyOtp.isEnabled = true
                    binding.progressbar.visibility = View.GONE
                    binding.tvResendOtp.isEnabled = true
                    Toast.makeText(requireContext(),it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                ServerResult.Progress -> {
                    binding.btnVerifyOtp.isEnabled = false
                    binding.progressbar.visibility = View.VISIBLE
                    binding.tvResendOtp.isEnabled = false
                }
                is ServerResult.Success -> {
                    findNavController().navigate(R.id.action_OTPVerification_to_signUpProfile)
                    Toast.makeText(requireContext(),it.data.message, Toast.LENGTH_SHORT).show()

                }
            }
        }
        viewModel.resendOtpResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is ServerResult.Failure -> {
                    binding.btnVerifyOtp.isEnabled = true
                    binding.progressbar.visibility = View.GONE
                    binding.tvResendOtp.isEnabled = true
                    Toast.makeText(requireContext(),it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is ServerResult.Progress -> {
                    binding.btnVerifyOtp.isEnabled = false
                    binding.progressbar.visibility = View.VISIBLE
                    binding.tvResendOtp.isEnabled = false
                }
                is ServerResult.Success -> {
                    binding.btnVerifyOtp.isEnabled = true
                    binding.progressbar.visibility = View.GONE
                    binding.tvResendOtp.isEnabled = true
                    Toast.makeText(requireContext(),it.data.message, Toast.LENGTH_SHORT).show()
            }}
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOtpInputFields()
        setupResendOtpButton()
        binding.tvResendOtp.setOnClickListener {
            if(binding.tvResendOtp.isEnabled){
                viewModel.resendOtp(PrefManager.getOtpOrderId()!!)
            }
        }
        startResendCooldown()
    }

    private fun setupOtpInputFields() {
        val otpInputs = listOf(
            binding.etOtp1,
            binding.etOtp2,
            binding.etOtp3,
            binding.etOtp4,
            binding.etOtp5,
            binding.etOtp6
        )

        otpInputs.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        otp += s.toString()
                        if (index < otpInputs.size - 1) {
                            otpInputs[index + 1].requestFocus()
                        } else {
                            binding.btnVerifyOtp.isEnabled = true
                        }
                    } else if (s?.length == 0) {
                        if (index > 0) {
                            otpInputs[index - 1].requestFocus()
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Detecting backspace press
                    if (before == 1 && count == 0 && s?.isEmpty() == true) {
                        // Handle backspace event
                        if (otp.isNotEmpty()) {
                            otp = otp.dropLast(1)
                        }
                        // Move focus to the previous input
                        if (index > 0) {
                            otpInputs[index - 1].requestFocus()
                        }
                    }
                }
            })


    }
    }

    private fun setupResendOtpButton() {
        binding.tvResendOtp.setOnClickListener {
            if (isResendActive) {
                if (resendClickCount < maxResendAttempts) {
                    resendClickCount++
                    startResendCooldown()
                    // Handle OTP resend action here
                    Toast.makeText(requireContext(), "OTP Resent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Max resend attempts reached", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please wait before resending OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startResendCooldown() {
        isResendActive = false
        binding.tvResendOtp.isEnabled = false
        binding.tvResendOtp.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))

        countDownTimer = object : CountDownTimer(resendCooldownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.tvResendOtp.text = "Resend OTP in $secondsRemaining s"
            }

            override fun onFinish() {
                isResendActive = true
                binding.tvResendOtp.isEnabled = true
                binding.tvResendOtp.text = "Resend OTP"
                binding.tvResendOtp.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        _binding = null
    }

}