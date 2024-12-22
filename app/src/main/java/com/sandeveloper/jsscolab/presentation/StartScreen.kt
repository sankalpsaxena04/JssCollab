package com.sandeveloper.jsscolab.presentation

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.messaging.FirebaseMessaging
import com.sandeveloper.MainActivity
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.data.Room.AppsDatabase
import com.sandeveloper.jsscolab.data.Room.SwapDatabase
import com.sandeveloper.jsscolab.databinding.ActivityStartScreenBinding
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.NetworkChangeReceiver
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.toast
import com.sandeveloper.jsscolab.domain.Utility.GlobalUtils
import com.sandeveloper.jsscolab.presentation.Auth.Auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StartScreen @Inject constructor() : AppCompatActivity(), NetworkChangeReceiver.NetworkChangeCallback {

    private val viewModel: StartScreenViewModel by viewModels()
    private val util: GlobalUtils.EasyElements by lazy {
        GlobalUtils.EasyElements(this@StartScreen)
    }
    private val binding: ActivityStartScreenBinding by lazy {
        ActivityStartScreenBinding.inflate(layoutInflater)
    }
    @Inject
    lateinit var swapDatabase: SwapDatabase
    @Inject
    lateinit var appsDatabase: AppsDatabase
    private val networkChangeReceiver = NetworkChangeReceiver(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("tokenjwt", PrefManager.getToken().toString())
        registerNetworkChangeReceiver()

        if (PrefManager.getAppMode() == Endpoints.ONLINE_MODE) {
            checkToken()
        }

        bindObserver()
    }

    private fun bindObserver() {
        viewModel.banStatusResponse.observe(this, Observer {
            when(it) {
                is ServerResult.Failure -> {
                    viewModel.isBanned()
                }
                ServerResult.Progress -> {}
                is ServerResult.Success -> {
                    if (it.data.success) {
                        if (it.data.ban) {
                            startActivity(Intent(this, BanActivity::class.java))
                            finish()
                        }
                    } else {
                        viewModel.isBanned()
                    }
                }
            }
        })

        viewModel.profileResponse.observe(this, Observer {
            when(it) {
                is ServerResult.Failure -> {}
                ServerResult.Progress -> {}
                is ServerResult.Success -> {
                    if (it.data.success) {
                        PrefManager.setcurrentUserdetails(it.data.details!!)
                        checkDetails()
                    } else {
                        toast(it.data.message)
                    }
                }
            }
        })

        viewModel.swapItemsResponse.observe(this, Observer {
            when(it) {
                is ServerResult.Failure -> {
                    viewModel.getSwapItems()
                }
                ServerResult.Progress -> {}
                is ServerResult.Success -> {
                    if (it.data.success) {
                        val swaps = it.data.swapItems
                        CoroutineScope(Dispatchers.IO).launch {
                            swapDatabase.swapDao().insertSwaps(swaps)
                        }
                    } else {
                        Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.getAppAppsResponse.observe(this, Observer {
            when(it) {
                is ServerResult.Failure -> {
                    viewModel.getApps(null, null)
                }
                ServerResult.Progress -> {}
                is ServerResult.Success -> {
                    if (it.data.success) {
                        val apps = it.data.apps
                        CoroutineScope(Dispatchers.IO).launch {
                            appsDatabase.appsDao().insertApps(apps ?: listOf())
                        }
                    }
                }
            }
        })

        viewModel.messageResponse.observe(this, Observer {
            when(it) {
                is ServerResult.Failure -> {
                    viewModel.getMessages()
                }
                ServerResult.Progress -> {}
                is ServerResult.Success -> {
                    if (it.data) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun checkToken() {
        PrefManager.setOfflineDialogShown(false)
        PrefManager.setAppMode(Endpoints.ONLINE_MODE)
        if (PrefManager.getToken().isNullOrEmpty()) {
            startActivity(Intent(this, Auth::class.java))
            finish()
        } else {
            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                viewModel.setFCMToken(it)
            }
            viewModel.isBanned().also {
                checkDetails()
            }
        }
    }

    private fun checkDetails() {
        if (PrefManager.getcurrentUserdetails().email.isNullOrEmpty()) {
            viewModel.getMyDetails()
        }
        viewModel.getSwapItems().also {
            viewModel.getApps(null, null).also {
                viewModel.getMessages()
            }
        }
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

    private fun registerNetworkChangeReceiver() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }
}
