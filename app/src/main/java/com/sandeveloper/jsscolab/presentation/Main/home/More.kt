package com.sandeveloper.jsscolab.presentation.Main.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentMoreBinding
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.presentation.Main.Profile
import com.sandeveloper.jsscolab.presentation.Main.ProfileViewModel
import com.sandeveloper.jsscolab.presentation.StartScreen
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.io.IOException

@AndroidEntryPoint
class More : Fragment() {
    private var _binding:FragmentMoreBinding?= null
    private val profileViewModel:ProfileViewModel by viewModels()
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(PrefManager.getcurrentUserdetails().email==null||PrefManager.getcurrentUserdetails().email==""){
            profileViewModel.getMyDetails()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoreBinding.inflate(inflater,container,false)


        binding.editProfile.setOnClickThrottleBounceListener {
            val intent = Intent(requireContext(), Profile::class.java)
            startActivity(intent)
        }
        binding.yourPosts.setOnClickThrottleBounceListener {
            PrefManager.setBroadCategory(Endpoints.broadcategories.my_posts)
            val intent = Intent(requireContext(),BroadCategoryPosts::class.java)
            startActivity(intent)
        }


        binding.logout.setOnClickThrottleBounceListener {
            showCustomDialogueBox("Do you want to logout.")
        }

        setUI()

        return binding.root
    }

    private fun setUI() {
        val details =PrefManager.getcurrentUserdetails()
        binding.username.text = details.full_name?:"Name"
        binding.phoneNo.text = details.phone?:"+91xxxxxxxxxx"
        Glide.with(binding.root.context).load(details.photo?.secure_url?:"").placeholder(R.drawable.profile_pic_placeholder).into(binding.profileImage)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
    private fun showCustomDialogueBox(message: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val msg= dialog.findViewById<TextView>(R.id.message)
        val btnyes = dialog.findViewById<TextView>(R.id.yes)
        val btnno = dialog.findViewById<TextView>(R.id.no)
        msg.text = message
        btnyes.setOnClickListener {
            PrefManager.saveToken(null)
            val intent = Intent(requireContext(),StartScreen::class.java)
            startActivity(intent)
            requireActivity().finish()
            dialog.dismiss()
        }
        btnno.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.profileResponse.observe(viewLifecycleOwner, Observer{
            when(it){
                is ServerResult.Failure -> {
                    Toast.makeText(requireContext(),it.exception.message,Toast.LENGTH_SHORT).show()
                }
                is ServerResult.Progress -> {

                }
                is ServerResult.Success -> {
                    setUI()
                }
            }
        })
    }
}