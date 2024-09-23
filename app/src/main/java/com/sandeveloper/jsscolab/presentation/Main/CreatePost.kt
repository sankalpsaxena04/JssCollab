package com.sandeveloper.jsscolab.presentation.Main

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.FragmentCreatePostBinding
import com.sandeveloper.jsscolab.databinding.HostelSelectionBottomsheetBinding
import com.sandeveloper.jsscolab.databinding.SelectCategoryBottomsheetBinding
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.Filter
import com.sandeveloper.jsscolab.domain.Modules.Post.createPost
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.isNull
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.visible
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CreatePost : Fragment() {

    private val viewModel: CreatePostViewModel by viewModels()
    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var chipGroup: ChipGroup
    private var selectedTime: Long? = null
    private val selectedLocations = mutableSetOf<String>()
    val locationViewSet = arrayOf(
        "hostel_1", "hostel_2", "hostel_3", "hostel_4", "hostel_5",
        "hostel_6", "hostel_7", "hostel_8", "hostel_9", "hostel_10",
        "hostel_11", "hostel_12", "hostel_13", "hostel_14", "hostel_15",
        "hostel_16", "hostel_17", "hostel_18", "hostel_19", "hostel_20", "hostel_21"
    )

    fun String.toOriginalCategories(): String {
        return when (this) {
                Endpoints.categories.Pharmaceuticals -> "pharmaceutical"
                Endpoints.categories.QuickCommerce -> "quick-commerce"
                Endpoints.categories.FoodDelivery -> "food-delivery"
                Endpoints.categories.ECommerce -> "e-commerce"
                Endpoints.categories.SharedCab -> "cab"
                Endpoints.categories.Exchange -> "subscription-service"
                else -> "other"
            }

    }
    val selectedApps = mutableSetOf<String>()
    private val locations = mutableListOf("GH", "BH", "ISH","University Boy's Hostel","Near Mithaas", "Near YourSpace","Near Hoolive","Women Hostel","Day Scholars")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.category.setOnClickThrottleBounceListener {
            hideKeyboard()
            setUpCategoryBottomDialog()
        }
        binding.hostels.setOnClickThrottleBounceListener {
            showHostelSelectionBottomSheet()
        }
        PrefManager.getSelectedCategory()?.let {
            updateCategorySelection(it)
        }
        binding.createPost.setOnClickThrottleBounceListener{
            if(validPost()){
                when(binding.category.text.toString()){
                    Endpoints.categories.SharedCab->{
                        val formattedTime = formatTimeMillisToDateTime(selectedTime!!)
                        val updatedComment = binding.Comment.text.toString() + "\nJourney is scheduled for $formattedTime"

                        viewModel.createCoshopPost(
                            createPost(
                                listOf(),
                                binding.category.text.toString().toOriginalCategories(),
                                updatedComment, // Updated comment with journey time
                                binding.senderContribution.text.toString().toInt(),
                                Filter(true, selectedLocations.toList(), listOf()),
                                binding.totalAmount.text.toString().toInt(),
                                selectedTime!! // original Long value
                            )
                        )

                    }
                    Endpoints.categories.ECommerce,Endpoints.categories.QuickCommerce,Endpoints.categories.FoodDelivery,Endpoints.categories.Pharmaceuticals->{
                        viewModel.createCoshopPost(
                            createPost(
                                listOf(),
                                binding.category.text.toString().toOriginalCategories(),
                                binding.Comment.text.toString(),
                                binding.senderContribution.text.toString().toInt(),
                                Filter(true, selectedLocations.toList(), listOf()),
                                binding.totalAmount.text.toString().toInt(),
                                selectedTime!!
                            )
                        )
                    }
                    Endpoints.categories.Exchange->{

                    }

                }
            }
        }
        binding.date.setOnClickThrottleBounceListener {
            selectDateTime()
        }
        bindObserver()
    }

    private fun bindObserver() {

        viewModel.createPostState.observe(viewLifecycleOwner, Observer {
            when(it){
                is ServerResult.Failure -> {
                    Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT).show()
                    binding.itemView.visibility = View.VISIBLE
                    binding.createPost.isEnabled = true
                    binding.progressbar.visibility = View.GONE
                }
                is ServerResult.Progress -> {
                    binding.itemView.visibility = View.INVISIBLE
                    binding.createPost.isEnabled = false
                    binding.progressbar.visibility = View.VISIBLE
                }
                is ServerResult.Success -> {
                    binding.itemView.visibility = View.VISIBLE
                    binding.createPost.isEnabled = true
                    binding.progressbar.visibility = View.GONE
                    clearSelections()
                    Toast.makeText(requireContext(), "Post Created Successfully", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun clearSelections(){
        binding.category.setText("Select Category")
        selectedTime = null
        selectedApps.clear()
        selectedLocations.clear()
        binding.totalAmount.setText(null)
        binding.senderContribution.setText(null)
        binding.Comment.setText(null)
        binding.hostels.setText(null)
        binding.date.setText("Post Expires on")
    }
    private fun validPost(): Boolean {
        if(binding.category.text.toString().isEmpty()){
            Toast.makeText(requireContext(), "Select Category", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedLocations.isEmpty()){
            Toast.makeText(requireContext(), "Select Hostels", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedApps.isEmpty()){
            Toast.makeText(requireContext(),"Select Apps",Toast.LENGTH_SHORT).show()
            return false
        }
        else if (binding.senderContribution.text.toString().isEmpty()){
            Toast.makeText(requireContext(),"Enter Sender Contribution",Toast.LENGTH_SHORT).show()
            return false
        }
        else if (binding.totalAmount.text.toString().isEmpty()){
            Toast.makeText(requireContext(),"Enter Total Amount",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedTime==null){
            Toast.makeText(requireContext(),"Select Date and Time",Toast.LENGTH_SHORT).show()
            return false
        }
        return true

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun selectDateTime() {
        // Create constraints for the date picker (disallow past dates)
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

        // Open the Date Picker first
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.show(childFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            // Convert the selected timestamp into Calendar format
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selectedDate

            // Proceed with time picker after selecting the date
            openTimePickerForDate(calendar)
        }
    }

    private fun openTimePickerForDate(selectedDate: Calendar) {
        val currentTime = Calendar.getInstance()
        val isSystem24Hour = is24HourFormat(requireContext())

        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        // If the selected date is today, ensure the time is at least 5 minutes from now
        val minHour: Int
        val minMinute: Int
        if (selectedDate.isSameDayAs(currentTime)) {
            minHour = currentTime.get(Calendar.HOUR_OF_DAY)
            minMinute = currentTime.get(Calendar.MINUTE) + 5
        } else {
            minHour = 0
            minMinute = 0
        }

        // Open the Time Picker
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(minHour)
            .setMinute(minMinute)
            .setTitleText("Select Time")
            .build()

        timePicker.show(childFragmentManager, "TIME_PICKER")

        timePicker.addOnPositiveButtonClickListener {
            // Set selected time in the selectedDate calendar object
            selectedDate.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            selectedDate.set(Calendar.MINUTE, timePicker.minute)

            val now = Calendar.getInstance()
            if (selectedDate.before(now)) {
                Toast.makeText(requireContext(), "Selected time is in the past", Toast.LENGTH_SHORT).show()
            } else {
                // Format and display the selected date and time
                val sdf = SimpleDateFormat("dd:MM HH:mm", Locale.getDefault())
                val formattedDateTime = sdf.format(selectedDate.time)

                // Store the selected date and time in milliseconds
                selectedTime = selectedDate.timeInMillis

                // Display formatted date and time
                binding.date.setText(formattedDateTime)
            }
        }
    }

    // Helper function to check if two dates are the same day
    private fun Calendar.isSameDayAs(other: Calendar): Boolean {
        return this.get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
                this.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)
    }


    private fun setUpCategoryBottomDialog() {
        val categoryBottomSheetBinding = SelectCategoryBottomsheetBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())

        categoryBottomSheetBinding.apply {
            setCategoryClickListener(QuickCommerce, Endpoints.categories.QuickCommerce, dialog)
            setCategoryClickListener(Pharmaceuticals, Endpoints.categories.Pharmaceuticals, dialog)
            setCategoryClickListener(FoodDelivery, Endpoints.categories.FoodDelivery, dialog)
            setCategoryClickListener(ECommerce, Endpoints.categories.ECommerce, dialog)
            setCategoryClickListener(Exchange, Endpoints.categories.Exchange, dialog)
            setCategoryClickListener(SharedCab, Endpoints.categories.SharedCab, dialog)
        }

        dialog.setCancelable(true)
        dialog.setContentView(categoryBottomSheetBinding.root)
        dialog.show()
    }

    private fun setCategoryClickListener(view: View, category: String, dialog: BottomSheetDialog) {
        view.setOnClickThrottleBounceListener {
            updateCategorySelection(category)
            dialog.dismiss()
        }
    }

    private fun updateCategorySelection(category: String) {
        PrefManager.setSelectedCategory(category)
        binding.category.setText(category)
        when(category){
            Endpoints.categories.SharedCab->{
                binding.date.text = "Select Date& Time of Journey"
                selectedApps.clear()
                selectedTime = null
                selectedLocations.clear()
                binding.Comment.hint = "Describe your Journey Details"
                binding.selectItemsToGiveText.visibility = View.GONE
                binding.selectItemsToReceiveText.visibility = View.GONE
                binding.giveChipGroup.visibility = View.GONE
                binding.receiveChipGroup.visibility = View.GONE
            }
            Endpoints.categories.QuickCommerce,Endpoints.categories.FoodDelivery,Endpoints.categories.ECommerce->{
                selectedApps.clear()
                binding.date.text = "Post Expires on"
                selectedTime = null
                selectedLocations.clear()
               binding.selectItemsToGiveText.visibility = View.GONE
                binding.selectItemsToReceiveText.visibility = View.GONE
                binding.giveChipGroup.visibility = View.GONE
                binding.receiveChipGroup.visibility = View.GONE
            }
            Endpoints.categories.Exchange->{
                selectedApps.clear()
                binding.date.text = "Post Expires on"
                selectedTime = null
                selectedLocations.clear()
                binding.Comment.visibility = View.GONE
                binding.senderContribution.visibility = View.GONE
                binding.totalAmount.visibility = View.GONE

                binding.selectItemsToGiveText.visibility = View.VISIBLE
                binding.selectItemsToReceiveText.visibility = View.VISIBLE
                binding.giveChipGroup.visibility = View.VISIBLE
                binding.receiveChipGroup.visibility = View.VISIBLE
            }
        }
        setUpChipSelect(requireContext(), category)
        binding.appChipGroup.visible()
    }

    private fun setUpChipSelect(context: Context, selectedCategory: String) {
        chipGroup = binding.appChipGroup
        chipGroup.removeAllViews()

        val categoryApplicationsMap = mapOf(
            "Quick Commerce" to listOf(
                "Zepto",
                "SwiggyInstamart ",
                "Blinkit",
                "BigBasket",
                "Dunzo"
            ),
            "Food Delivery" to listOf("Zomato", "Swiggy", "EatSure", "Domino's"),
            "E-Commerce" to listOf("Amazon", "Flipkart", "Vishal Megamart", "Myntra", "Meesho"),
            "Shared Cab" to listOf("Ola", "Uber", "Rapido", "InDrive", "BluSmart"),
            "Pharmaceuticals" to listOf("Apollo 24x7", "PharmEasy", "1 mg", "NetMeds")
        )

        categoryApplicationsMap[selectedCategory]?.forEach { app ->
            addChipToGroup(context, app)
        }

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            selectedApps.clear() // Clear the set first
            val checkedApps = checkedIds.map { id ->
                (group.findViewById<Chip>(id)).text.toString()
            }
            selectedApps.addAll(checkedApps)
        }

    }
    private fun showHostelSelectionBottomSheet() {
        // Use ViewBinding for the bottom sheet layout
        val bottomSheetBinding = HostelSelectionBottomsheetBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        // Preselect checkboxes if already selected
        locations.forEachIndexed {index, loc->
            val res = resources.getIdentifier(locationViewSet[index],"id",requireContext().packageName)
            val ev:CheckBox=bottomSheetBinding.root.findViewById(res)
            ev.visibility = View.VISIBLE
            ev.text = loc
            ev.isChecked = selectedLocations.contains(loc)

        }

        bottomSheetBinding.btnHostelDone.setOnClickListener {
            selectedLocations.clear()
            locationViewSet.forEachIndexed{index,loc->
                if(bottomSheetBinding.root.findViewById<CheckBox>(resources.getIdentifier(loc,"id",requireContext().packageName)).isChecked){
                    selectedLocations.add(locations[index])
            }
            }

            binding.hostels.setText(selectedLocations.joinToString(", "))

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun addChipToGroup(context: Context, app: String) {
        val chip = LayoutInflater.from(context).inflate(R.layout.app_chip_layout, chipGroup, false) as Chip
        chip.id = View.generateViewId()
        chip.text = app
        chip.isCheckable = true
        chipGroup.addView(chip)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
    fun formatTimeMillisToDateTime(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val date = Date(timeInMillis)
        return dateFormat.format(date)
    }

}
