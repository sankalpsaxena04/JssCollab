package com.sandeveloper.jsscolab.presentation.Auth
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.domain.Interfaces.AuthRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Auth.AuthResponse
import com.sandeveloper.jsscolab.domain.Modules.Auth.ForgetPasswordRequest
import com.sandeveloper.jsscolab.domain.Modules.Auth.LoginRequest
import com.sandeveloper.jsscolab.domain.Modules.Auth.OtpRequest
import com.sandeveloper.jsscolab.domain.Modules.Auth.OtpResponse
import com.sandeveloper.jsscolab.domain.Modules.Auth.OtpVerifiedResponse
import com.sandeveloper.jsscolab.domain.Modules.Auth.signupRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.BanStatusResponse
import com.sandeveloper.jsscolab.domain.Modules.Profile.CreateProfile
import com.sandeveloper.jsscolab.domain.Modules.Profile.MyProfileResponse
import com.sandeveloper.jsscolab.domain.Modules.Profile.MyRatingResponse
import com.sandeveloper.jsscolab.domain.Modules.Profile.PictureUpdateRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.ProfileUpdateRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.RateUserRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.ReportUserRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Repositories.RetrofitAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: RetrofitAuthRepository
) : ViewModel() {

    private val commonTag = "JSSColabDebug"  // Common log tag
    private var countDownTimer: CountDownTimer? = null
    private val _otpResponse = MutableLiveData<ServerResult<OtpResponse>>()
    val otpResponse: LiveData<ServerResult<OtpResponse>> get() = _otpResponse

    private val _resendOtpResponse = MutableLiveData<ServerResult<OtpResponse>>()
    val resendOtpResponse: LiveData<ServerResult<OtpResponse>> get() = _resendOtpResponse

    private val _otpVerifiedResponse = MutableLiveData<ServerResult<OtpVerifiedResponse>>()
    val otpVerifiedResponse: LiveData<ServerResult<OtpVerifiedResponse>> get() = _otpVerifiedResponse

    private val _authResponse = MutableLiveData<ServerResult<AuthResponse>>()
    val authResponse: LiveData<ServerResult<AuthResponse>> get() = _authResponse

    private val _commonResponse = MutableLiveData<ServerResult<commonResponse>>()
    val commonResponse: LiveData<ServerResult<commonResponse>> get() = _commonResponse

    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> get() = _timerText

    private val _isResendVisible = MutableLiveData<Boolean>(false)
    val isResendVisible: LiveData<Boolean> get() = _isResendVisible

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Send OTP
    fun sendOtp(otpRequest: OtpRequest) {
        Log.d(commonTag, "sendOtp: Initiating OTP request for orderId: ${otpRequest}")
        viewModelScope.launch {
            authRepository.sendOtp(otpRequest) { result ->
                Log.d(commonTag, "sendOtp: Received result: $result")
                _otpResponse.postValue(result)
            }
        }
    }

    // Resend OTP
    fun resendOtp(orderId: String) {
        Log.d(commonTag, "resendOtp: Resending OTP for orderId: $orderId")
        viewModelScope.launch {
            authRepository.resendOtp(orderId) { result ->
                Log.d(commonTag, "resendOtp: Received result: $result")
                _resendOtpResponse.postValue(result)
            }
        }
    }

    // Verify OTP
    fun verifyOtp(phone: Long, otp: Int, orderId: String) {
        Log.d(commonTag, "verifyOtp: Verifying OTP for phone: $phone, orderId: $orderId")
        viewModelScope.launch {
            authRepository.verifyOtp(phone, otp, orderId) { result ->
                Log.d(commonTag, "verifyOtp: Received result: $result")
                _otpVerifiedResponse.postValue(result)
            }
        }
    }

    // SignUp
    fun signUp(signupRequest: signupRequest) {
        Log.d(commonTag, "signUp: Attempting sign-up with request: $signupRequest")
        viewModelScope.launch {
            authRepository.signUp(signupRequest) { result ->
                Log.d(commonTag, "signUp: Received result: $result")
                _authResponse.postValue(result)
            }
        }
    }

    // Login
    fun login(loginRequest: LoginRequest) {
        Log.d(commonTag, "login: Attempting login with request: $loginRequest")
        viewModelScope.launch {
            authRepository.login(loginRequest) { result ->
                Log.d(commonTag, "login: Received result: $result")
                _authResponse.postValue(result)
            }
        }
    }

    // Forget Password
    fun forgetPassword(forgetPasswordRequest: ForgetPasswordRequest) {
        Log.d(commonTag, "forgetPassword: Requesting password reset with request: $forgetPasswordRequest")
        viewModelScope.launch {
            authRepository.forgetPassword(forgetPasswordRequest) { result ->
                Log.d(commonTag, "forgetPassword: Received result: $result")
                _authResponse.postValue(result)
            }
        }
    }

    // Timer Methods


    fun setIsResendVisible(newValue: Boolean) {
        Log.d(commonTag, "setIsResendVisible: Setting resend visibility to $newValue")
        _isResendVisible.value = newValue
    }

    fun startTimer() {
        Log.d(commonTag, "startTimer: Starting OTP resend timer.")
        countDownTimer?.cancel()
        var countDownTimer = object : CountDownTimer(180000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                _timerText.value = String.format("Resend OTP in %02d:%02d", minutes, seconds)
                Log.d(commonTag, "onTick: Timer updated to ${_timerText.value}")
            }

            override fun onFinish() {
                Log.d(commonTag, "onFinish: Timer finished.")
                _timerText.value = "00:00"
                _isResendVisible.value = true
            }
        }
        countDownTimer.start()
    }
    fun cancelTimer() {
        countDownTimer?.let {
            Log.d(commonTag, "cancelTimer: Cancelling the timer.")
            it.cancel()
            _timerText.value = "00:00" // Optionally reset the timer text
            _isResendVisible.value = true // Optionally make resend button visible
        }
    }

    override fun onCleared() {
        Log.d(commonTag, "AuthViewModel cleared. Timer stopped.")
        super.onCleared()
        countDownTimer?.cancel()
    }
}
