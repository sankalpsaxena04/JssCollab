package com.sandeveloper.jsscolab.presentation.Auth
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _otpResponse = MutableStateFlow<ServerResult<OtpResponse>>(ServerResult.Progress)
    val otpResponse: StateFlow<ServerResult<OtpResponse>> = _otpResponse

    private val _otpVerifiedResponse = MutableStateFlow<ServerResult<OtpVerifiedResponse>>(ServerResult.Progress)
    val otpVerifiedResponse: StateFlow<ServerResult<OtpVerifiedResponse>> = _otpVerifiedResponse

    private val _authResponse = MutableStateFlow<ServerResult<AuthResponse>>(ServerResult.Progress)
    val authResponse: StateFlow<ServerResult<AuthResponse>> = _authResponse

    fun sendOtp(otpRequest: OtpRequest) {
        viewModelScope.launch {
            authRepository.sendOtp(otpRequest) { result ->
                _otpResponse.value = result
            }
        }
    }

    fun resendOtp(orderId: String) {
        viewModelScope.launch {
            authRepository.resendOtp(orderId) { result ->
                _otpResponse.value = result
            }
        }
    }

    fun verifyOtp(phone: Long, otp: Int, orderId: String) {
        viewModelScope.launch {
            authRepository.verifyOtp(phone, otp, orderId) { result ->
                _otpVerifiedResponse.value = result
            }
        }
    }

    fun signUp(signupRequest: signupRequest) {
        viewModelScope.launch {
            authRepository.signUp(signupRequest) { result ->
                _authResponse.value = result
            }
        }
    }

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            authRepository.login(loginRequest) { result ->
                _authResponse.value = result
            }
        }
    }

    fun forgetPassword(forgetPasswordRequest: ForgetPasswordRequest) {
        viewModelScope.launch {
            authRepository.forgetPassword(forgetPasswordRequest) { result ->
                _authResponse.value = result
            }
        }
    }
}
