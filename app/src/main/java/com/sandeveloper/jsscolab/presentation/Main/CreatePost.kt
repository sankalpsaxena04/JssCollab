package com.sandeveloper.jsscolab.presentation.Main

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentCreatePostBinding
import com.sandeveloper.jsscolab.databinding.SelectCategoryBottomsheetBinding
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.visible
import kotlin.random.Random

class CreatePost : Fragment() {

    companion object {
        fun newInstance() = CreatePost()
    }

    private lateinit var categoryBottomSheetBinding: SelectCategoryBottomsheetBinding
    private val viewModel: CreatePostViewModel by viewModels()
    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var chipGroup: ChipGroup
    private var time:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.category.setOnClickThrottleBounceListener {
            hideKeyboard()
            setUpCategoryBottomDialog()
        }

        PrefManager.getSelectedCategory()?.let {
            binding.category.setText(it)
            setUpChipSelect(requireContext(), it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)

        binding.Time.setOnClickThrottleBounceListener {
            openTimePicker()
        }

        return binding.root
    }

    private fun openTimePicker() {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if(!isSystem24Hour&&PrefManager.getSelectedCategory()==Endpoints.categories.SharedCab) TimeFormat.CLOCK_12H else TimeFormat.CLOCK_24H

        val picker  = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(1)
            .setMinute(0)
            .setTitleText(if(PrefManager.getSelectedCategory()==Endpoints.categories.SharedCab) "Departure Time" else "Select Duration")
            .build()
        picker.show(childFragmentManager,"TAG")
        picker.addOnPositiveButtonClickListener {
            val h = picker.hour
            val m = picker.minute
            binding.Time.gravity = Gravity.CENTER
            binding.Time.setText(if(m<10) "$h:0$m" else "$h:$m")

        }
    }
    private fun openDatePicker(){
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if(isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker  = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Departure Time")
            .build()
        picker.show(childFragmentManager,"TAG")
        picker.addOnPositiveButtonClickListener {
            val d = picker.selection.toString()

        }
    }

    private fun setUpCategoryBottomDialog() {
        categoryBottomSheetBinding = SelectCategoryBottomsheetBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())

        categoryBottomSheetBinding.QuickCommerce.setOnClickThrottleBounceListener {
            PrefManager.setSelectedCategory(Endpoints.categories.QuickCommerce)
            binding.category.setText(Endpoints.categories.QuickCommerce)
            setUpChipSelect(requireContext(), Endpoints.categories.QuickCommerce)
            dialog.dismiss()
            binding.appChipGroup.visible()
        }

        categoryBottomSheetBinding.Pharmaceuticals.setOnClickThrottleBounceListener {
            PrefManager.setSelectedCategory(Endpoints.categories.Pharmaceuticals)
            binding.category.setText(Endpoints.categories.Pharmaceuticals)
            setUpChipSelect(requireContext(), Endpoints.categories.Pharmaceuticals)
            dialog.dismiss()
            binding.appChipGroup.visible()
        }

        categoryBottomSheetBinding.FoodDelivery.setOnClickThrottleBounceListener {
            PrefManager.setSelectedCategory(Endpoints.categories.FoodDelivery)
            binding.category.setText(Endpoints.categories.FoodDelivery)
            setUpChipSelect(requireContext(), Endpoints.categories.FoodDelivery)
            dialog.dismiss()
            binding.appChipGroup.visible()
        }

        categoryBottomSheetBinding.ECommerce.setOnClickThrottleBounceListener {
            PrefManager.setSelectedCategory(Endpoints.categories.ECommerce)
            binding.category.setText(Endpoints.categories.ECommerce)
            setUpChipSelect(requireContext(), Endpoints.categories.ECommerce)
            dialog.dismiss()
            binding.appChipGroup.visible()
        }

        categoryBottomSheetBinding.Exchange.setOnClickThrottleBounceListener {
            PrefManager.setSelectedCategory(Endpoints.categories.Exchange)
            binding.category.setText(Endpoints.categories.Exchange)
            setUpChipSelect(requireContext(), Endpoints.categories.Exchange)
            dialog.dismiss()
            binding.appChipGroup.visible()
        }

        categoryBottomSheetBinding.SharedCab.setOnClickThrottleBounceListener {
            PrefManager.setSelectedCategory(Endpoints.categories.SharedCab)
            binding.category.setText(Endpoints.categories.SharedCab)
            setUpChipSelect(requireContext(), Endpoints.categories.SharedCab)
            dialog.dismiss()
            binding.appChipGroup.visible()
        }

        dialog.setCancelable(true)
        dialog.setContentView(categoryBottomSheetBinding.root)
        dialog.show()
    }



    private fun setUpChipSelect(context: Context, selectedCategory: String) {
        chipGroup = binding.appChipGroup
        chipGroup.removeAllViews() // Clear any existing chips

        // Define your category to app mapping
        val categoryApplicationsMap = mapOf(
            "Quick Commerce" to listOf("App 1.1", "App 1.2", "App 1.3"),
            "Food Delivery" to listOf("App 2.1", "App 2.2"),
            "E-Commerce" to listOf("App 3.1", "App 3.2", "App 3.3", "App 3.4"),
            "Exchange" to listOf("App 4.1", "App 4.2", "App 4.3"),
            "Shared Cab" to listOf("App 5.1", "App 5.2")
        )

        // Check if the selected category is valid
        val appsList = categoryApplicationsMap[selectedCategory] ?: return

        // Inflate and add chips to the ChipGroup
        appsList.forEach { app ->
            val chip = LayoutInflater.from(context).inflate(R.layout.app_chip_layout, chipGroup, false) as Chip
            chip.id = View.generateViewId() // Generate a unique ID for each chip
            chip.text = app
            chip.isCheckable = true // Make the chip checkable
            chipGroup.addView(chip)
        }

        // Set up a listener to handle chip selection
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedTopicList = checkedIds.map { id ->
                (group.findViewById<Chip>(id)).text.toString()
            }
            Toast.makeText(context, checkedTopicList.joinToString(", "), Toast.LENGTH_SHORT).show()
        }
    }


    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
