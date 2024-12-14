package com.sandeveloper.jsscolab.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.ActivityReportUserBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Profile.ReportUserRequest
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportUser : AppCompatActivity() {

    private val binding: ActivityReportUserBinding by lazy {
        ActivityReportUserBinding.inflate(layoutInflater)
    }
    private val viewModel: ReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setView()
        bindObserver()
        val type = intent.getStringExtra("type")
        if (type=="report"){
            val post = PrefManager.getOnClickedPost()
            binding.creatorName.text = post.sender!!.full_name
            binding.address.text = "Location: ${post.sender.address}"
            binding.reportOn.text = "Report On: ${post.category}"
            Glide.with(this).load(post.sender.photo?.secure_url?:"")
                .placeholder(R.drawable.profile_pic_placeholder)
                .into(binding.creatorProfilePic)
            binding.report.setOnClickThrottleBounceListener{
                if (binding.description.text.toString().isNotEmpty()){
                    val desc = binding.description.text.toString()+"\n Post Category: ${post.category}" +
                            "\n Post Description: ${post.comment}"
                    viewModel.reportUser(ReportUserRequest(post.sender._id,desc))
                }else{
                    Toast.makeText(this,"Please provide description", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            binding.postBy.text = "FeedBack"
            binding.reportUserView.visibility = View.GONE
            binding.reportText.text = "Submit Feedback"
            binding.report.setOnClickThrottleBounceListener{
                if (binding.description.text.toString().isNotEmpty()){
                    val desc = "Feedback: "+binding.description.text.toString()
                    viewModel.reportUser(ReportUserRequest(null,desc))
                }else{
                    Toast.makeText(this,"Please provide description", Toast.LENGTH_SHORT).show()
                }
            }

        }


    }

    private fun bindObserver() {
        viewModel.reportUserResponse.observe(this, Observer {
            when(it){
                is ServerResult.Failure -> {
                    binding.progressbar.visibility = View.GONE
                    binding.report.isEnabled = true
                    Toast.makeText(this,it.exception.message, Toast.LENGTH_SHORT).show()
                }
                ServerResult.Progress ->{
                    binding.progressbar.visibility = View.VISIBLE
                    binding.report.isEnabled = false
                }
                is ServerResult.Success -> {
                    binding.progressbar.visibility = View.GONE
                    binding.report.isEnabled = true
                    if(it.data.success){
                        Toast.makeText(this,it.data.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else{
                        Toast.makeText(this,it.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    fun setView(){
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}