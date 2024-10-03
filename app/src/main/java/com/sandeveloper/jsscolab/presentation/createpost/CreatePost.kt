package com.sandeveloper.jsscolab.presentation.createpost

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.sandeveloper.jsscolab.domain.HelperClasses.toOriginalCategories
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.Filter
import com.sandeveloper.jsscolab.domain.Modules.Post.createPost
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapEntity
import com.sandeveloper.jsscolab.domain.Modules.swap.createSwapRequest
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

    private lateinit var searchAdapter: SwapItemAdapter
    private lateinit var selectedAdapter: SwapItemAdapter
    private val selectedItems = mutableSetOf<SwapEntity>()
    private lateinit var giveAdapter: SwapItemAdapter
    private lateinit var searchGiveAdapter: SwapItemAdapter
    private val selectedGiveItems = mutableSetOf<SwapEntity>()


    val selectedApps = mutableSetOf<String>()
    private val locations = mutableListOf("GH", "BH", "ISH","University Boy's Hostel","Near Mithaas", "Near YourSpace","Near Hoolive","Women Hostel","Day Scholars")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.category.setOnClickThrottleBounceListener {
            hideKeyboard()
            setUpCategoryBottomDialog()
        }
        binding.recyclerViewSearchResults.visibility = View.GONE
        binding.recyclerViewGiveSearchResults.visibility = View.GONE
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
                        Toast.makeText(requireContext(),selectedLocations.toString(),Toast.LENGTH_SHORT).show()
                        viewModel.createSwap(
                            createSwapRequest(
                                to_give = selectedGiveItems.map { it._id },
                                to_take = selectedItems.map { it._id },
                                filter = Filter(
                                    my_year = true,
                                    address = selectedLocations.toList(),
                                    branch = emptyList()
                                ),
                                expiration_date = selectedTime!!
                            )
                        )

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
                    Log.e("serverError",it.exception.toString())
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
                    updateCategorySelection(PrefManager.getSelectedCategory()!!)

                    Toast.makeText(requireContext(), "Post Created Successfully", Toast.LENGTH_SHORT).show()
                }
            }
        })
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
        else if(selectedTime==null){
            Toast.makeText(requireContext(),"Select Date and Time",Toast.LENGTH_SHORT).show()
            return false
        }

        if(PrefManager.getSelectedCategory()!=Endpoints.categories.Exchange){
            if (binding.senderContribution.text.toString().isEmpty()){
                Toast.makeText(requireContext(),"Enter Sender Contribution",Toast.LENGTH_SHORT).show()
                return false
            }
            else if (binding.totalAmount.text.toString().isEmpty()){
                Toast.makeText(requireContext(),"Enter Total Amount",Toast.LENGTH_SHORT).show()
                return false
            }

        }
        else{
            if (selectedGiveItems.isEmpty()){
                Toast.makeText(requireContext(),"Select Items to Give.",Toast.LENGTH_SHORT).show()
                return false
            }
            else if(selectedItems.isEmpty()){
                Toast.makeText(requireContext(),"Select Items to Receive.",Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        receiveSelect()
        giveSelect()
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
    private fun receiveSelect() {
        // Adapter for search results
        searchAdapter = SwapItemAdapter { swapItem ->
            selectedItems.add(swapItem)
            selectedAdapter.submitList(selectedItems.toList())

            // Clear the search bar text after selecting an item
            binding.searchBar.text?.clear()

            // Hide the search results RecyclerView
            binding.recyclerViewSearchResults.visibility = View.GONE
        }

        // Adapter for selected items
        selectedAdapter = SwapItemAdapter {}

        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearchResults.adapter = searchAdapter

        // Set up RecyclerView for selected items
        binding.recyclerViewSelectedItems.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSelectedItems.adapter = selectedAdapter

        // Observe search results
        viewModel.searchResults.observe(viewLifecycleOwner, Observer { results ->
            searchAdapter.submitList(results)
        })

        // Listen for text changes in the search bar
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isEmpty()) {
                    // Hide the RecyclerView if no text is entered
                    binding.recyclerViewSearchResults.visibility = View.GONE
                } else {
                    // Show the RecyclerView when text is entered
                    binding.recyclerViewSearchResults.visibility = View.VISIBLE
                    viewModel.setSearchQuery(query)
                }
            }
        })
    }

    private fun giveSelect() {
        searchGiveAdapter = SwapItemAdapter { swapItem ->
            selectedGiveItems.add(swapItem)
            giveAdapter.submitList(selectedGiveItems.toList())

            binding.givesearchBar.text?.clear()

            binding.recyclerViewGiveSearchResults.visibility = View.GONE
        }

        giveAdapter = SwapItemAdapter {}

        binding.recyclerViewGiveSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewGiveSearchResults.adapter = searchGiveAdapter

        binding.recyclerGiveViewSelectedItems.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerGiveViewSelectedItems.adapter = giveAdapter

        viewModel.searchResults.observe(viewLifecycleOwner, Observer { results ->
            searchGiveAdapter.submitList(results)
        })

        binding.givesearchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isEmpty()) {
                    binding.recyclerViewGiveSearchResults.visibility = View.GONE
                } else {
                    binding.recyclerViewGiveSearchResults.visibility = View.VISIBLE
                    viewModel.getSearchQuery(query)
                }
            }
        })
    }
    private fun openTimePickerForDate(selectedDate: Calendar) {
        val currentTime = Calendar.getInstance()
        val isSystem24Hour = is24HourFormat(requireContext())

        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val minHour: Int
        val minMinute: Int
        if (selectedDate.isSameDayAs(currentTime)) {
            minHour = currentTime.get(Calendar.HOUR_OF_DAY)
            minMinute = currentTime.get(Calendar.MINUTE) + 5
        } else {
            minHour = 0
            minMinute = 0
        }

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(minHour)
            .setMinute(minMinute)
            .setTitleText("Select Time")
            .build()

        timePicker.show(childFragmentManager, "TIME_PICKER")

        timePicker.addOnPositiveButtonClickListener {
            selectedDate.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            selectedDate.set(Calendar.MINUTE, timePicker.minute)

            val now = Calendar.getInstance()
            if (selectedDate.before(now)) {
                Toast.makeText(requireContext(), "Selected time is in the past", Toast.LENGTH_SHORT).show()
            } else {
                val sdf = SimpleDateFormat("dd:MM HH:mm", Locale.getDefault())
                val formattedDateTime = sdf.format(selectedDate.time)

                selectedTime = selectedDate.timeInMillis

                binding.date.setText(formattedDateTime)
            }
        }
    }

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
                binding.totalAmount.visibility = View.VISIBLE
                binding.senderContribution.visibility = View.VISIBLE
                binding.Comment.visibility = View.VISIBLE
                binding.totalAmount.hint = "Enter Total Estimate Fare"
                binding.Comment.hint = "Describe your Journey Details"
                binding.date.text = "Select Date& Time of Journey"
                selectedApps.clear()
                selectedItems.clear()
                selectedGiveItems.clear()
                selectedTime = null
                selectedLocations.clear()
                binding.Comment.hint = "Describe your Journey Details"
                  binding.receiveSearchBox.visibility = View.GONE
                binding.giveSearchBox.visibility = View.GONE
            }
            Endpoints.categories.QuickCommerce,Endpoints.categories.FoodDelivery,Endpoints.categories.ECommerce,Endpoints.categories.Pharmaceuticals->{
                binding.totalAmount.visibility = View.VISIBLE
                binding.senderContribution.visibility = View.VISIBLE
                binding.Comment.visibility = View.VISIBLE
                selectedApps.clear()
                binding.date.text = "Post Expires on"
                selectedItems.clear()
                selectedGiveItems.clear()
                selectedTime = null
                selectedLocations.clear()
                binding.receiveSearchBox.visibility = View.GONE
                binding.giveSearchBox.visibility = View.GONE
            }
            Endpoints.categories.Exchange->{
                selectedApps.clear()
                binding.date.text = "Post Expires on"
                selectedTime = null
                selectedLocations.clear()
                selectedItems.clear()
                selectedGiveItems.clear()
                binding.Comment.visibility = View.GONE
                binding.senderContribution.visibility = View.GONE
                binding.totalAmount.visibility = View.GONE
                binding.giveSearchBox.visibility = View.VISIBLE
                binding.receiveSearchBox.visibility = View.VISIBLE
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
