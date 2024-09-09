package com.sandeveloper.jsscolab.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.ActivityStartScreenBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.NetworkChangeReceiver
import com.sandeveloper.jsscolab.domain.Utility.GlobalUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartScreen @Inject constructor(): AppCompatActivity(),NetworkChangeReceiver.NetworkChangeCallback {

//    @Inject
//    lateinit var repository: Repository

    private val util : GlobalUtils.EasyElements by lazy{
        GlobalUtils.EasyElements(this@StartScreen)
    }
    private val binding: ActivityStartScreenBinding by lazy{
        ActivityStartScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    override fun onOnlineModePositiveSelected() {
        TODO("Not yet implemented")
    }

    override fun onOfflineModePositiveSelected() {
        TODO("Not yet implemented")
    }

    override fun onOfflineModeNegativeSelected() {
        TODO("Not yet implemented")
    }
}