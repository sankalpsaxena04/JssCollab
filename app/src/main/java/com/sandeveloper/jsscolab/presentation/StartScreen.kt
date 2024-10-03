package com.sandeveloper.jsscolab.presentation

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.sandeveloper.MainActivity
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.ActivityStartScreenBinding
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.NetworkChangeReceiver
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.toast
import com.sandeveloper.jsscolab.domain.Utility.GlobalUtils
import com.sandeveloper.jsscolab.presentation.Auth.Auth
import com.sandeveloper.jsscolab.presentation.Auth.AuthViewModel
import com.sandeveloper.jsscolab.presentation.Main.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class StartScreen @Inject constructor() : AppCompatActivity(), NetworkChangeReceiver.NetworkChangeCallback {

    private val APP_UPDATE_REQUEST_CODE = 901
    private val viewModel: StartScreenViewModel by viewModels()
    private lateinit var appUpdateManager: AppUpdateManager
    private val util: GlobalUtils.EasyElements by lazy {
        GlobalUtils.EasyElements(this@StartScreen)
    }
    private val binding: ActivityStartScreenBinding by lazy {
        ActivityStartScreenBinding.inflate(layoutInflater)
    }
    private val networkChangeReceiver = NetworkChangeReceiver(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)

        if (PrefManager.getAppMode() == Endpoints.ONLINE_MODE) {
            startMainActivity()
        }

        bindObservers()
        registerNetworkChangeReceiver()
    }

    // Binds observers to LiveData to handle the response flow
    private fun bindObservers() {
        viewModel.profileResponse.observe(this) { result ->
            when (result) {
                is ServerResult.Failure -> {
                    showToast(result.exception.message)
                }
                is ServerResult.Success -> {
                    viewModel.getSwapItems()
                }
                else -> {}
            }
        }

        // Observe ban status to handle if user is banned or not
        viewModel.banStatusResponse.observe(this) { result ->
            when (result) {
                is ServerResult.Failure -> {
                    showToast(result.exception.message)
                    viewModel.isBanned()
                }
                is ServerResult.Success -> {
                    handleBanStatus(result.data.ban)
                }
                else -> {}
            }
        }

        // Observe swap items response
        viewModel.swapItemsResponse.observe(this) { result ->
            when (result) {
                is ServerResult.Failure -> {
                    showToast(result.exception.message)
                }
                is ServerResult.Success -> {
                    if (result.data.success) {
                        showToast("Swaps received")
                        Log.d("swapdb",result.data.swapItems.toString())
                        viewModel.setSwapsToDB(result.data.swapItems)
                    } else {
                        showToast(result.data.message)
                    }
                }
                else -> {}
            }
        }

        // Observe DB save response for swaps
        viewModel.swapItemsDBResponse.observe(this) { result ->
            when (result) {
                is ServerResult.Failure -> {
                    showToast(result.exception.message)
                    Log.d("swapdb",result.exception.message.toString())
                }
                is ServerResult.Success -> {
                    showToast("Saved Successfully")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else -> {}
            }
        }
    }

    private fun startMainActivity() {
        val token = PrefManager.getToken()
        if (token != null) {
            PrefManager.setOfflineDialogShown(false)
            PrefManager.setAppMode(Endpoints.ONLINE_MODE)

            // Check if the user is banned
            viewModel.isBanned()
        } else {
            // If token is not available, redirect to Auth screen
            startActivity(Intent(this, Auth::class.java))
            finish()
        }
    }

    private fun handleBanStatus(isBanned: Boolean) {
        if (isBanned) {
            showToast("You are banned")
            startActivity(Intent(this, BanActivity::class.java))
            finish()
        } else {
            checkEmailAndProceed()
        }
    }

    // Check if email is null or empty, and proceed accordingly
    private fun checkEmailAndProceed() {
        val currentUserDetails = PrefManager.getcurrentUserdetails()
        if (currentUserDetails.email.isNullOrEmpty()) {
            viewModel.getMyDetails().also {
                // After getting details, fetch swaps
                viewModel.getSwapItems()
            }
        } else {
            // Directly fetch swaps if email is available
            viewModel.getSwapItems()
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message ?: "Unknown error", Toast.LENGTH_SHORT).show()
    }

    override fun onOnlineModePositiveSelected() {
        PrefManager.setAppMode(Endpoints.ONLINE_MODE)
        util.restartApp()
    }

    override fun onOfflineModePositiveSelected() {
        PrefManager.setAppMode(Endpoints.OFFLINE_MODE)

    }

    override fun onOfflineModeNegativeSelected() {
        networkChangeReceiver.retryNetworkCheck()
    }

    // Register the receiver to handle network changes
    private fun registerNetworkChangeReceiver() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }
}
