package com.sandeveloper.jsscolab.presentation.Auth.Signup.SignUpProfile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.sandeveloper.MainActivity
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentSignupProfileBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Auth.signupRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.CreateProfile
import com.sandeveloper.jsscolab.domain.Modules.Profile.PictureUpdateRequest
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.presentation.Auth.AuthViewModel
import com.sandeveloper.jsscolab.presentation.Main.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class SignUpProfile@Inject constructor() : Fragment() {

    private var _binding: FragmentSignupProfileBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private val CAMERA_PERMISSION_REQUEST = 100
    private val authViewModel:AuthViewModel by viewModels()
    private val profileViewModel:ProfileViewModel by viewModels()
    private var base64Profile:String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupProfileBinding.inflate(inflater,container,false)
        val hostelTypes = arrayOf("GH", "BH", "ISH","University Boy's Hostel","Near Mithaas", "Near YourSpace","Near Hoolive","Women Hostel","Day Scholars")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hostelTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.locationDropdown.setAdapter(adapter)
        binding.locationDropdown.textSize = 22.0f
        // Set click listener for the profile image
        setUpView()
        binding.SignUp.setOnClickThrottleBounceListener {
                if (dataChecker()) {

                    val signUpReq = signupRequest(
                        binding.emailId.text.toString(),
                        PrefManager.getPhoneNumber()!!.toLong(),
                        binding.password.text.toString(),
                        PrefManager.getTempAuthToken()!!
                    )

                    authViewModel.signUp(signUpReq)
                }

        }


        return binding.root
    }
    fun bindObservers(){
        authViewModel.authResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visibility=View.GONE
            enableInputs(true)
            when(it){
                is ServerResult.Failure -> {
                    Toast.makeText(requireContext(),it.exception.message.toString(),Toast.LENGTH_SHORT).show()
                    binding.progressbar.visibility = View.GONE
                    enableInputs(true)
                }
                ServerResult.Progress -> {
                    binding.progressbar.visibility = View.VISIBLE
                    enableInputs(false)
                }
                is ServerResult.Success -> {
                    val createProfile = CreateProfile(binding.nameEditText.text.toString(),binding.admissionNumberEditText.text.toString(),binding.locationDropdown.text.toString(),"asde123")
                    profileViewModel.createProfile(createProfile)

                }
            }
        })
        profileViewModel.createProfileResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visibility=View.GONE
            enableInputs(true)
            when(it){
                is ServerResult.Failure -> {
                    Toast.makeText(requireContext(),it.exception.message.toString(),Toast.LENGTH_SHORT).show()
                    binding.progressbar.visibility = View.GONE
                    enableInputs(true)
                }
                is ServerResult.Progress -> {
                    binding.progressbar.visibility = View.VISIBLE
                    enableInputs(false)
                }
                is ServerResult.Success -> {
                    profileViewModel.updatePicture(PictureUpdateRequest(base64Profile!!))

                }
            }
        })
        profileViewModel.updateProfilePictureResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visibility=View.GONE
            enableInputs(true)
            when(it){
                is ServerResult.Failure -> {
                    Toast.makeText(requireContext(),it.exception.message.toString(),Toast.LENGTH_SHORT).show()

                    binding.progressbar.visibility = View.GONE
                    enableInputs(true)
                }
                is ServerResult.Progress -> {
                    binding.progressbar.visibility = View.VISIBLE
                    enableInputs(false)
                }
                is ServerResult.Success -> {
                    profileViewModel.getMyDetails()
                    Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()


                }
            }

        })
        profileViewModel.profileResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is ServerResult.Failure -> {
                    Toast.makeText(requireContext(),it.exception.message.toString(),Toast.LENGTH_SHORT).show()
                    binding.progressbar.visibility = View.GONE
                    enableInputs(true)
                }
                is ServerResult.Progress -> {
                    binding.progressbar.visibility = View.VISIBLE
                    enableInputs(false)
                }
                is ServerResult.Success -> {
                    Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(),MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()

                }
            }
        })
    }
    private fun enableInputs(isEnabled: Boolean) {
        binding.nameEditText.isEnabled = isEnabled
        binding.admissionNumberEditText.isEnabled = isEnabled
        binding.locationDropdown.isEnabled = isEnabled
        binding.emailId.isEnabled = isEnabled
        binding.password.isEnabled = isEnabled
        binding.reEnterPassword.isEnabled = isEnabled
        binding.SignUp.isEnabled = isEnabled
    }
    private fun dataChecker():Boolean {
        if(binding.nameEditText.text.toString()==""){
            Toast.makeText(requireContext(),"Enter a valid name",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(base64Profile==null){
            Toast.makeText(requireContext(),"Insert Profile Picture",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.locationDropdown.text.toString()=="Select Location"){
            Toast.makeText(requireContext(),"Select a valid Location",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(!isValidEmail(binding.emailId.text.toString())){
            Toast.makeText(requireContext(),"Enter an Valid Email",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.password.text.toString().length<8){
            Toast.makeText(requireContext(),"Password should have minimum 8 characters",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.reEnterPassword.text.toString()!= binding.password.text.toString()){
            Toast.makeText(requireContext(),"ReEnter Correct password",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(!isValidAdmissionNo(binding.admissionNumberEditText.text.toString())){
            Toast.makeText(requireContext(),"Enter a valid AdmissionNo.",Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isValidAdmissionNo(admn:String): Boolean {

        return true
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern = Regex(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )
        return emailPattern.matches(email)
    }
    private fun setUpView() {
        // Profile image click listener
        binding.profileImageView.setOnClickThrottleBounceListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST
                )
            } else {
                pickImage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    Log.d("profilebase63",imageBitmap.toString())
                    base64Profile = imageBitmap.toBase64DataUri()
                    binding.profileImageView.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_PICK -> {
                    val selectedImage = data?.data
                    base64Profile = uriToBase64(requireContext(), selectedImage!!)
                    binding.profileImageView.setImageURI(selectedImage)
                }
            }
        }
    }
    fun Bitmap.toBase64DataUri(imageFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): String {
        val byteArrayOutputStream = ByteArrayOutputStream()

        // Compress the Bitmap into the ByteArrayOutputStream using the chosen format (PNG or JPEG)
        this.compress(imageFormat, 50, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        // Convert the byte array to a Base64 string
        val base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP)

        // Determine the MIME type based on the image format
        val mimeType = when (imageFormat) {
            Bitmap.CompressFormat.PNG -> "image/png"
            Bitmap.CompressFormat.JPEG -> "image/jpeg"
            else -> "image/png" // Default to PNG
        }

        // Return the complete Data URI string
        return "data:$mimeType;base64,$base64String"
    }

    private fun pickImage() {

        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose an option")
        builder.setItems(options) { dialog, item ->
            when (options[item]) {
                "Take Photo" -> {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
                    } else {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                    }
                }
                "Choose from Gallery" -> {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, REQUEST_IMAGE_PICK)
                }
                "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }
    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            // Get input stream from the Uri
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val byteArrayOutputStream = ByteArrayOutputStream()

            // Buffer to read the input stream
            val buffer = ByteArray(1024)
            var bytesRead: Int

            // Read the InputStream and write to ByteArrayOutputStream
            while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }

            // Convert the ByteArrayOutputStream to a byte array
            val byteArray = byteArrayOutputStream.toByteArray()

            // Encode the byte array to a Base64 string
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            } else {
                Toast.makeText(requireContext(), "Camera Permission Denied, can't take photo", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}