package com.sandeveloper.jsscolab.presentation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentProfileBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Profile.ProfileUpdateRequest
import com.sandeveloper.jsscolab.presentation.Auth.Auth
import com.sandeveloper.jsscolab.presentation.Main.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var isTextChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        setUI()

        setupTextWatchers()  // Setup TextWatchers to monitor changes
        setupUpdateButton()  // Setup the update button behavior
        setupBackPressedHandling()  // Handle back press behavior

        // Return the root view
        return binding.root
    }

    private fun setUI() {
        val details = PrefManager.getcurrentUserdetails()
        if(details.email==null||details.email==""){
            viewModel.getMyDetails()
        }
        // Setup the delete button listener
        Log.d("profilePhoto",details.photo?.secure_url?:"")
        Glide.with(requireContext()).load(details.photo?.secure_url).placeholder(R.drawable.profile_pic_placeholder).into(binding.profileImage)
        binding.etName.setText(details.full_name)
        binding.etAddress.setText(details.address)
        binding.tvAdmissionNumber.setText(details.admission_number)
        binding.tvEmail.text = details.email
        binding.tvPhone.text = details.phone.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Function to show the confirmation dialog
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Profile")
            .setMessage("Are you sure you want to delete your profile? This action cannot be undone.")
            .setPositiveButton("Delete") { dialog, _ ->
                // Call deleteAccount from the ViewModel when confirmed
                viewModel.deleteAccount()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Optionally observe the delete account response here
    private fun observeViewModel() {
        viewModel.profileResponse.observe(viewLifecycleOwner, Observer {
            when( it){
                is ServerResult.Failure -> {
                    Toast.makeText(requireContext(),it.exception.message,Toast.LENGTH_SHORT).show()
                }
                is ServerResult.Progress -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.viewUi.visibility = View.INVISIBLE
                    binding.btnDeleteProfile.isEnabled = false
                    binding.btnSave.isEnabled= false
                }
                is ServerResult.Success -> {
                    if(it.data.success){

                        binding.progressbar.visibility = View.INVISIBLE
                        binding.viewUi.visibility = View.VISIBLE
                        binding.btnDeleteProfile.isEnabled = true
                        binding.btnSave.isEnabled= true
                        Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(),it.data.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.deleteAccountResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ServerResult.Success -> {

                    Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), Auth::class.java))
                    requireActivity().finish()
                }
                is ServerResult.Failure -> {
                    binding.progressbar.visibility = View.INVISIBLE
                    binding.btnDeleteProfile.isEnabled = true
                    binding.viewUi.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Failed to delete account: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
                is ServerResult.Progress -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.btnDeleteProfile.isEnabled = false
                    binding.viewUi.visibility = View.INVISIBLE
                }
            }
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isTextChanged = true  // Mark that text has been changed
                binding.btnSave.isEnabled = true  // Enable the Update Profile button
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        // Add TextWatcher to each EditText
        binding.etName.addTextChangedListener(textWatcher)
        binding.etAddress.addTextChangedListener(textWatcher)

    }

    // Setup Update Profile button behavior
    private fun setupUpdateButton() {
        binding.btnSave.setOnClickListener {
            // Check if changes are detected, then trigger updateProfile in the ViewModel
            if (isTextChanged) {
                val profileUpdateRequest = ProfileUpdateRequest(
                    name = binding.etName.text.toString(),
                    address = binding.etAddress.text.toString()
                )
                viewModel.updateProfile(profileUpdateRequest)  // Trigger update
                Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()

                // Reset the change flag and disable button after update
                isTextChanged = false
                binding.btnSave.isEnabled = false
            }
        }
    }

    // Setup back press handling to ask if user wants to save changes
    private fun setupBackPressedHandling() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isTextChanged) {
                    // Show dialog if text was changed
                    showSaveChangesDialog()
                } else {
                    // If no changes, proceed with normal back press behavior
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
    }

    // Show a dialog asking if the user wants to save changes
    private fun showSaveChangesDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Unsaved Changes")
            .setMessage("You have unsaved changes. Do you want to save them before exiting?")
            .setPositiveButton("Save") { dialog, _ ->
                // Save the changes and navigate back
                binding.btnSave.performClick()
                dialog.dismiss()
                requireActivity().onBackPressed()  // Proceed with back press after saving
            }
            .setNegativeButton("Discard") { dialog, _ ->
                dialog.dismiss()
                requireActivity().onBackPressed()  // Discard changes and proceed with back press
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()  // Cancel the dialog and stay on the current screen
            }
            .show()
    }
}
