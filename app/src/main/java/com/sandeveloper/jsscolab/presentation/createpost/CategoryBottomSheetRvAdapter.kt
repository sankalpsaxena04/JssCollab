package com.sandeveloper.jsscolab.presentation.createpost

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandeveloper.jsscolab.databinding.CategoryBottomSheetItemBinding
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.setOnClickThrottleBounceListener

class CategoryBottomSheetRvAdapter (private val dataList: MutableList<String>,
private val type:String,
private val currentSelected:Int,
private val onClick: onClickString
) : RecyclerView.Adapter<CategoryBottomSheetRvAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CategoryBottomSheetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (currentSelected!=-1) {
            holder.binding.radioButton.isChecked = position == currentSelected
        }

        holder.binding.textFull.text=dataList[position]

        holder.binding.layout.setOnClickThrottleBounceListener{
            onClick.sendString(dataList[position],type, position)
            Log.d("TAG", "onBindViewHolder: ${dataList[position]}")
        }
        holder.binding.radioButton.setOnClickThrottleBounceListener{
            onClick.sendString(dataList[position],type, position)
            Log.d("TAG", "onBindViewHolder: ${dataList[position]}")
        }
    }

    inner class ViewHolder(val binding: CategoryBottomSheetItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface onClickString {
        fun sendString(text: String,type: String, currentSelected: Int)
    }

}
