package com.sandeveloper.jsscolab.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.sandeveloper.MainActivity
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.ActivityStartScreenBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.NetworkChangeReceiver
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Utility.GlobalUtils
import com.sandeveloper.jsscolab.presentation.Auth.Auth
import com.sandeveloper.jsscolab.presentation.Auth.AuthViewModel
import com.sandeveloper.jsscolab.presentation.Main.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartScreen @Inject constructor(): AppCompatActivity(),NetworkChangeReceiver.NetworkChangeCallback {

//    @Inject
//    lateinit var repository: Repository

    private val viewModel:StartScreenViewModel by viewModels()
    private val util : GlobalUtils.EasyElements by lazy{
        GlobalUtils.EasyElements(this@StartScreen)
    }
    private val binding: ActivityStartScreenBinding by lazy{
        ActivityStartScreenBinding.inflate(layoutInflater)
    }
    private val networkChangeReceiver = NetworkChangeReceiver(this,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(PrefManager.getToken()!=null){



        viewModel.isBanned()


        }
        else{
            val intent = Intent(this, Auth::class.java)
            startActivity(intent)
            finish()
        }
        viewModel.banStatusResponse.observe(this, Observer {
            when(it){
                is ServerResult.Failure -> {
                    viewModel.isBanned()
                    Toast.makeText(this,it.exception.message,Toast.LENGTH_SHORT).show()
                }
                is ServerResult.Progress -> {

                }
                is ServerResult.Success -> {

                    if(!it.data.ban){
                        if(PrefManager.getcurrentUserdetails().email==null||PrefManager.getcurrentUserdetails().email==""){
                            viewModel.getMyDetails()
                        }
                        else{
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    else{
                        val intent = Intent(this,BanActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                }
            }
        })
        viewModel.profileResponse.observe(this, Observer {
         when(it){
             is ServerResult.Failure ->{
                 Toast.makeText(this,it.exception.message,Toast.LENGTH_SHORT).show()
             }
             is ServerResult.Progress -> {}
             is ServerResult.Success -> {
                 val intent = Intent(this,MainActivity::class.java)
                 startActivity(intent)
                 finish()
             }
         }
        })

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