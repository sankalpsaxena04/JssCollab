package com.sandeveloper.jsscolab.presentation.Main

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
import androidx.core.view.children
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
import com.sandeveloper.jsscolab.databinding.FragmentEditBinding
import com.sandeveloper.jsscolab.databinding.HostelSelectionBottomsheetBinding
import com.sandeveloper.jsscolab.databinding.SelectCategoryBottomsheetBinding
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.HelperClasses.convertToMillis
import com.sandeveloper.jsscolab.domain.HelperClasses.toOriginalCategories
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.App
import com.sandeveloper.jsscolab.domain.Modules.Post.Filter
import com.sandeveloper.jsscolab.domain.Modules.Post.Posts
import com.sandeveloper.jsscolab.domain.Modules.Post.createPost
import com.sandeveloper.jsscolab.domain.Modules.Post.updatePostRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapEntity
import com.sandeveloper.jsscolab.domain.Modules.swap.createSwapRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.updateSwapRequest
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.visible
import com.sandeveloper.jsscolab.presentation.Main.LocationBottomSheetCheckboxFragment
import com.sandeveloper.jsscolab.presentation.createpost.CategoryBottomSheetFragment
import com.sandeveloper.jsscolab.presentation.createpost.SwapItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditFragment : Fragment(), CategoryBottomSheetFragment.SendText ,LocationBottomSheetCheckboxFragment.SendText{

    private val viewModel: EditViewModel by viewModels()
    private lateinit var binding: FragmentEditBinding
    private lateinit var chipGroup: ChipGroup
    private var selectedTime: Long? = null
    private val selectedLocations = mutableListOf<String>()
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

        val list = mutableListOf<String>()

        binding.category.setOnClickThrottleBounceListener {
            setUpCategoryBottomDialog(list)
        }

        viewModel.getAppsByNameCategoryResponse.observe(viewLifecycleOwner, Observer { apps ->
            updateAppChips(apps)
        })
        binding.recyclerViewSearchResults.visibility = View.GONE
        binding.recyclerViewGiveSearchResults.visibility = View.GONE
        binding.hostels.setOnClickThrottleBounceListener {
            setUpLocationSelect()
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

                        Log.d("Coshop",selectedApps.joinToString(", "))
                        viewModel.updatePost(
                            updatePostRequest(
                                post_id = PrefManager.getOnClickedPost()._id,
                                selectedApps.toList(),
                                binding.category.text.toString().toOriginalCategories(),
                                updatedComment, // Updated comment with journey time
                                binding.senderContribution.text.toString().toInt(),
                                Filter(true, selectedLocations.toList(), listOf()),
                                binding.totalAmount.text.toString().toInt(),
                                selectedTime!!
                            )
                        )

                    }
                    Endpoints.categories.Exchange->{
                        Log.d("Coshop",selectedApps.joinToString(", "))
                        Toast.makeText(requireContext(),selectedLocations.toString(),Toast.LENGTH_SHORT).show()
                        viewModel.updateSwap(
                            updateSwapRequest(
                                swap_id = PrefManager.getOnClickedPost()._id,
                                to_give = selectedGiveItems.map { it._id },
                                to_take = selectedItems.map { it._id },
                                filter = Filter(
                                    my_year = false,
                                    address = selectedLocations.toList(),
                                    branch = emptyList()
                                ),
                                expiration_date = selectedTime!!
                            )
                        )

                    }
                    else ->{
                        Log.d("Coshop",selectedApps.joinToString(", "))
                        Log.d("Coshop",binding.category.text.toString())
                        viewModel.updatePost(
                            updatePostRequest(
                                post_id = PrefManager.getOnClickedPost()._id,
                                selectedApps.toList(),
                                binding.category.text.toString(),
                                binding.Comment.text.toString(),
                                binding.senderContribution.text.toString().toInt(),
                                Filter(true, selectedLocations.toList(), listOf()),
                                binding.totalAmount.text.toString().toInt(),
                                selectedTime!!
                            )
                        )
                    }

                }
            }
        }
        binding.date.setOnClickThrottleBounceListener {
            selectDateTime()
        }

        initialUiSetUp()
        bindObserver()
    }

    private fun initialUiSetUp() {
        val post = PrefManager.getOnClickedPost()
        binding.category.setText(post.category)
        post.filter?.address?.let { selectedLocations.addAll(it) }
        binding.date.text = formatDate(post.expiration_date)
        binding.senderContribution.setText(post.sender_contribution.toString())
        binding.totalAmount.setText(post.total_required_amount.toString())
        binding.Comment.setText(post.comment)
        selectedTime = convertToMillis(post.expiration_date)
        post.apps?.let { selectedApps.addAll(it.map { it._id }) }
        binding.hostels.setText(selectedLocations.joinToString(", "))
        updateCategorySelection(post.category)

    }
    fun formatDate(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormatter = DateTimeFormatter.ofPattern("dd:MM HH:mm")

        val dateTime = LocalDateTime.parse(inputDate, inputFormatter).atOffset(ZoneOffset.UTC)
        return dateTime.format(outputFormatter)
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
        if(binding.category.text.toString().isEmpty()||binding.category.text.toString()=="Select Category"){
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
        binding = FragmentEditBinding.inflate(inflater, container, false)
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
        selectedAdapter = SwapItemAdapter {
            selectedItems.remove(it)
            selectedAdapter.submitList(selectedItems.toList())
        }

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

        giveAdapter = SwapItemAdapter {
            selectedGiveItems.remove(it)
            giveAdapter.submitList(selectedGiveItems.toList())
        }

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


    private fun setUpCategoryBottomDialog(list: MutableList<String>) {

        binding.category.setOnClickListener {

            var index:Int=-1
            if (binding.category.text!="Select Category"){
                index=list.indexOf(binding.category.text.toString())
            }
            val priorityBottomSheet =
                CategoryBottomSheetFragment(list, "Select Category", this,index)
            priorityBottomSheet.show(requireFragmentManager(), "Select Category")
        }

    }
    private fun setUpLocationSelect(){
        val categoryBottomSheet =
            LocationBottomSheetCheckboxFragment(locations, "Hostel", this,selectedLocations)
        categoryBottomSheet.show(requireFragmentManager(), "Hostel")


    }

    private fun updateCategorySelection(category: String) {
        PrefManager.setSelectedCategory(category)
        binding.category.setText(category)
        when(category){
            Endpoints.categories.SharedCab->{
                binding.totalAmount.visibility = View.VISIBLE
                binding.senderContribution.visibility = View.VISIBLE
                binding.Comment.visibility = View.VISIBLE
                binding.hostels.setText("Select locations to collaborate")
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
                binding.selectAppTextview.visibility = View.VISIBLE
                binding.appChipGroup.visible()
                binding.hostels.setText("Select locations to collaborate")
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
                binding.hostels.setText("Select locations to collaborate")
                selectedGiveItems.clear()
                binding.Comment.visibility = View.GONE
                binding.senderContribution.visibility = View.GONE
                binding.totalAmount.visibility = View.GONE
                binding.giveSearchBox.visibility = View.VISIBLE
                binding.receiveSearchBox.visibility = View.VISIBLE
            }
        }
        setUpChipSelect(category)
        binding.appChipGroup.visible()
    }

    private fun setUpChipSelect(selectedCategory: String) {
        chipGroup = binding.appChipGroup
        chipGroup.removeAllViews()

        // Fetch the list of apps for the selected category from the repository
        viewModel.getAppsByCategory(selectedCategory)
    }

    // Update chips when data is fetched
    private fun updateAppChips(apps: List<App>) {
        chipGroup.removeAllViews()
        apps.forEach { app ->
            val chip = LayoutInflater.from(requireContext()).inflate(R.layout.app_chip_layout, chipGroup, false) as Chip
            chip.id = View.generateViewId()
            chip.text = app.name
            chip.isCheckable = true
            chip.tag =  app._id
            chipGroup.addView(chip)
        }

        // Update selected apps with IDs instead of names
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            selectedApps.clear()
            val checkedAppIds = checkedIds.mapNotNull { id ->
                group.findViewById<Chip>(id)?.tag as? String
            }
            selectedApps.addAll(checkedAppIds)
        }
    }
    fun formatTimeMillisToDateTime(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val date = Date(timeInMillis)
        return dateFormat.format(date)
    }

    override fun stringtext(text: String, type: String, currentSelected: Int) {
        if (type=="Select Category"){
            setUpChipSelect(text)
            binding.category.text = text
        }
    }

    override fun stringText(selectedList: List<String>, type: String) {
        if(type=="Hostel"){
            selectedLocations.clear()
            selectedLocations.addAll(selectedList)
            binding.hostels.setText(selectedList.joinToString(", "))
        }
    }

}