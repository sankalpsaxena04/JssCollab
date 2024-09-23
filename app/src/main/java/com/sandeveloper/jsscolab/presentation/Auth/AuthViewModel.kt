package com.sandeveloper.jsscolab.presentation.Auth
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


    // Send OTP
    fun sendOtp(otpRequest: OtpRequest) {
        viewModelScope.launch {
            authRepository.sendOtp(otpRequest) { result ->
                _otpResponse.postValue(result)
            }
        }
    }

    // Resend OTP
    fun resendOtp(orderId: String) {
        viewModelScope.launch {
            authRepository.resendOtp(orderId) { result ->
                _resendOtpResponse.postValue(result)
            }
        }
    }

    // Verify OTP
    fun verifyOtp(phone: Long, otp: Int, orderId: String) {
        viewModelScope.launch {
            authRepository.verifyOtp(phone, otp, orderId) { result ->
                _otpVerifiedResponse.postValue(result)
            }
        }
    }

    // SignUp
    fun signUp(signupRequest: signupRequest) {
        viewModelScope.launch {
            authRepository.signUp(signupRequest) { result ->
                _authResponse.postValue(result)
            }
        }
    }

    // Login
    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            authRepository.login(loginRequest) { result ->
                _authResponse.postValue(result)
            }
        }
    }

    // Forget Password
    fun forgetPassword(forgetPasswordRequest: ForgetPasswordRequest) {
        viewModelScope.launch {
            authRepository.forgetPassword(forgetPasswordRequest) { result ->
                _authResponse.postValue(result)
            }
        }
    }


}

