package com.sandeveloper.jsscolab.presentation.Main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.PostListItemBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.setImageToCircleImageView
import com.sandeveloper.jsscolab.domain.Modules.Post.postUnit
import com.sandeveloper.jsscolab.domain.Modules.PostForView

class PostAdapter(private val post:List<PostForView>):RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    class PostViewHolder(val binding:PostListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostForView){
            val percentage = if(post.senderContribution.toInt()<post.totalRequiredAmount.toInt()){(post.senderContribution.toFloat() / post.totalRequiredAmount.toFloat()) * 100}else{100f}
            binding.creatorName.text = post.fullName
            binding.appName.text = post.appName
            binding.contributionLabel.text = "₹${post.senderContribution}"
            binding.totalAmount.text = "₹${post.totalRequiredAmount}"
            binding.postDescription.text = post.comment
            binding.contributionProgress.progress = percentage.toInt()
            Glide.with(binding.root.context).load(post.appphoto).placeholder(R.drawable.placeholder).into(binding.appBackgroundBlur)
            Glide.with(binding.root.context).load(post.userphoto).placeholder(R.drawable.profile_pic_placeholder).into(binding.creatorPhoto)

            binding.contributionProgress.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    adjustTagPosition(percentage)

                    binding.contributionProgress.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })


        }
        private fun adjustTagPosition(percentage: Float) {
            val progressBarWidth = binding.contributionProgress.width - binding.contributionProgress.paddingStart - binding.contributionProgress.paddingEnd

            val progressPosition = (progressBarWidth * (percentage / 100f)).toInt()

            val labelWidth = binding.contributionLabel.width
            val centeredPosition = progressPosition - (labelWidth / 2)

            val layoutParams = binding.contributionLabel.layoutParams as LinearLayout.LayoutParams
            layoutParams.marginStart = centeredPosition.coerceIn(0, progressBarWidth - labelWidth)
            binding.contributionLabel.layoutParams = layoutParams
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return post.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(post[position])
    }

}