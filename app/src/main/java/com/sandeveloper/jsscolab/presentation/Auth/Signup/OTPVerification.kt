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
import androidx.navigation.fragment.findNavController
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentOTPVerificationBinding
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OTPVerification@Inject constructor() : Fragment() {

    private var resendClickCount = 0
    private val maxResendAttempts = 4
    private val resendCooldownTime = 30000L // 30 seconds
    private var isResendActive = false
    private var countDownTimer: CountDownTimer? = null

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
        binding.btnVerifyOtp.setOnClickThrottleBounceListener{
            findNavController().navigate(R.id.action_OTPVerification_to_profilePicture)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOtpInputFields()
        setupResendOtpButton()

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
                        if (index < otpInputs.size - 1) {
                            otpInputs[index + 1].requestFocus()
                        }
                    } else if (s?.length == 0) {
                        if (index > 0) {
                            otpInputs[index - 1].requestFocus()
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
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