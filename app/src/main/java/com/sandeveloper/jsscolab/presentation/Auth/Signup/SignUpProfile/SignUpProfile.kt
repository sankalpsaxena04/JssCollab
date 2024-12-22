package com.sandeveloper.jsscolab.presentation.Auth.Signup.SignUpProfile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.google.firebase.messaging.FirebaseMessaging
import com.sandeveloper.MainActivity
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentSignupProfileBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Auth.signupRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.CreateProfile
import com.sandeveloper.jsscolab.domain.Modules.Profile.PictureUpdateRequest
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.isNull
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.presentation.Auth.AuthViewModel
import com.sandeveloper.jsscolab.presentation.Main.ProfileViewModel
import com.sandeveloper.jsscolab.presentation.StartScreen
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
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
    private var selectedLocation:String? = null

    private var capturedUserImageUri: Uri? = null
    private val REQUEST_USER_IMAGE_CAPTURE = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupProfileBinding.inflate(inflater,container,false)
        val hostelTypes = arrayOf(
            "GH", "BH", "ISH", "University Boy's Hostel", "Near Mithaas",
            "Near YourSpace", "Near Hoolive", "Women Hostel", "Day Scholars"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            hostelTypes
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        // Attach the adapter to the Spinner
        binding.locationDropdown.adapter = adapter

        // Set listener for item selection
        binding.locationDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLocation = hostelTypes[position]
                Toast.makeText(requireContext(), "Selected: $selectedLocation", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedLocation = null
            }
        }


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
                    if (it.data.success){
                        val fcm = FirebaseMessaging.getInstance().token.addOnSuccessListener {
                            val createProfile = CreateProfile(binding.nameEditText.text.toString(),binding.admissionNumberEditText.text.toString(),selectedLocation!!,it)
                            profileViewModel.createProfile(createProfile)
                        }
                    }
                    else{
                        Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                    }


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
                    if(it.data.success){

                        profileViewModel.updatePicture(uri = Uri.parse(profileViewModel.userSelfie.value!!), context = requireContext())
                    }
                    else{
                        Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                    }

                }
            }
        })
        profileViewModel.updateProfilePictureResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visibility=View.GONE
            when(it){
                is ServerResult.Failure -> {
                    Toast.makeText(requireContext(),it.exception.message.toString(),Toast.LENGTH_SHORT).show()

                    profileViewModel.getMyDetails()
                }
                is ServerResult.Progress -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is ServerResult.Success -> {
                    if(it.data.success){
                    Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        profileViewModel.userSelfie.observe(viewLifecycleOwner) {
            if (!it.isNull){
                val imageBitmap = handleImageRotation(Uri.parse(it!!))
                binding.profileImageView.setImageBitmap(imageBitmap)

            }
        }
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
                    if(it.data.success){
                    Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), StartScreen::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    }
                    else{
                        Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                    }
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
        if(selectedLocation.isNullOrEmpty()){
            Toast.makeText(requireContext(),"Select a valid Location",Toast.LENGTH_SHORT).show()
            return false
        }
        if(!isValidEmail(binding.emailId.text.toString())){
            Toast.makeText(requireContext(),"Enter an Valid Email",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.password.text.toString().length<8){
            Toast.makeText(requireContext(),"Password should have minimum 8 characters",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.reEnterPassword.text.toString()!= binding.password.text.toString()){
            Toast.makeText(requireContext(),"ReEnter Correct password",Toast.LENGTH_SHORT).show()
            return false
        }
        if(!isValidAdmissionNo(binding.admissionNumberEditText.text.toString())){
            Toast.makeText(requireContext(),"Enter a valid AdmissionNo.",Toast.LENGTH_SHORT).show()
            return false
        }
        if(profileViewModel.userSelfie.value.isNullOrEmpty()){
            Toast.makeText(requireContext(),"Upload a selfie",Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isValidAdmissionNo(admn:String): Boolean {
        val admno = Regex("^(?:[0-2][0-9]|3[0-9])(DL)?(cseds|cseaiml|cse|it|ece|eee|ee|me|ce)(?:0?[1-9][0-9]?|[1-2][0-9]{2})\$")
        return admno.matches(admn)
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern = Regex(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )
        return emailPattern.matches(email)
    }
    private fun setUpView() {
        // Profile image click listener
        binding.profileImageView.setOnClickThrottleBounceListener{
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST
                )
            } else {
                pickUserImage()
            }
        }
    }
    private fun pickUserImage() {
        val options = arrayOf<CharSequence>("Take Selfie", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Click your selfie in a well lit environment")
        builder.setItems(options) { dialog, item ->
            when (options[item]) {
                "Take Selfie" -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    capturedUserImageUri = FileProvider.getUriForFile(
                        requireContext(),
                        "${requireContext().packageName}.provider",  // Matches the manifest authority
                        createImageFile("User_Selfie")
                    )


                    intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedUserImageUri)
                    startActivityForResult(intent, REQUEST_USER_IMAGE_CAPTURE)
                }

                "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }
    private fun handleImageRotation(imageUri: Uri): Bitmap {
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val exif = ExifInterface(requireContext().contentResolver.openInputStream(imageUri)!!)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        val rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
        return rotatedBitmap
    }
    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
    private fun createImageFile(type: String): File {
        val storageDir: File = File(requireContext().filesDir, "images")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        return File(storageDir, "JSS-Collab-${type}-${System.currentTimeMillis()}.jpeg")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_USER_IMAGE_CAPTURE -> {
                    capturedUserImageUri?.let { uri ->
                        profileViewModel.setUserSelfie(uri.toString())
                    }
                }

            }
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