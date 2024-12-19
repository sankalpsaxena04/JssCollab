package com.sandeveloper.jsscolab.presentation.createpost

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sandeveloper.jsscolab.databinding.SelectCategoryBottomsheetBinding
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryBottomSheetFragment(private val dataList: List<String>,private val type:String, private val sendText: SendText, val currentSelected:Int):BottomSheetDialogFragment (),
    CategoryBottomSheetRvAdapter.onClickString{
    lateinit var binding: SelectCategoryBottomsheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SelectCategoryBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text=type.capitalize()
        setRecyclerView(dataList,type)
    }

    fun setRecyclerView(dataList: List<String>, type: String){
        val recyclerView = binding.categoryRv
        val adapter = CategoryBottomSheetRvAdapter(dataList.toMutableList(), type, currentSelected,this@CategoryBottomSheetFragment)
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
        recyclerView.visible()
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.done.visibility = View.GONE
    }

    override fun sendString(text: String,type: String,currentSelected: Int) {
        sendText.stringtext(text,type,currentSelected)
        dismiss()
    }

    interface SendText {
        fun stringtext(text: String,type: String,currentSelected: Int)
    }

}