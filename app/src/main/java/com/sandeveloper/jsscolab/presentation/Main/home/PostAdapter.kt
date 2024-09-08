package com.sandeveloper.jsscolab.presentation.Main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandeveloper.jsscolab.databinding.PostListItemBinding
import com.sandeveloper.jsscolab.domain.HelperClasses.setImageToCircleImageView
import com.sandeveloper.jsscolab.domain.Modules.PostForView

class PostAdapter(private val post:List<PostForView>):RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    class PostViewHolder(val binding:PostListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostForView){
            binding.tvName.text = post.fullName
            binding.tvAppName.text = "Ordered From: ${post.appName}"
            binding.tvMyContribution.text = "My Contribution: ₹${post.senderContribution}"
            binding.tvTotalAmount.text = "Required Amount: ₹${post.totalRequiredAmount}"
            binding.tvReviewText.text = post.comment
            setImageToCircleImageView(post.photo,binding.imgUser)
            binding.ratingBar.text = post.rating.toString()

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