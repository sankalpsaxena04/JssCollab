package com.sandeveloper.jsscolab.presentation.Main.home.HomeAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.PostListItemBinding
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.HelperClasses.getExpirationTime
import com.sandeveloper.jsscolab.domain.Modules.Post.Posts
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.isNull
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class PostAdapter(private val onPostClicked: (Posts) -> Unit, private val onPostLongClicked: (Posts) -> Unit) : ListAdapter<Posts, PostAdapter.PostViewHolder>(
    ComparatorDiffUtil()
) {
    class PostViewHolder(val binding: PostListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Posts, onPostClicked: (Posts) -> Unit, onPostLongClicked: (Posts) -> Unit) {
            val percentage = if (post.sender_contribution < post.total_required_amount) {
                (post.sender_contribution.toFloat() / post.total_required_amount.toFloat()) * 100
            } else {
                100f
            }

            binding.creatorName.text = post.sender?.full_name ?: PrefManager.getcurrentUserdetails().full_name
            binding.categoryName.text = post.category

            if (post.expiration_date == "0") {
                binding.expDLayout.visibility = View.GONE
            } else {
                binding.expirationDate.text = getExpirationTime(post.expiration_date)
            }

            binding.contributionLabel.text = "₹${post.sender_contribution}"
            binding.totalAmount.text = "₹${post.total_required_amount}"
            binding.postDescription.text = post.comment

            if (post.category == Endpoints.categories.Exchange) {
                binding.progressLayout.visibility = View.GONE
            } else {
                binding.contributionProgress.progress = percentage.toInt()
            }

            // Load images using Glide
            Glide.with(binding.root.context)
                .load(post.sender?.photo?.secure_url ?: "")
                .placeholder(R.drawable.profile_pic_placeholder)
                .into(binding.creatorPhoto)

            // Assuming contentIcon is still a placeholder image, replace with actual data if needed
            Glide.with(binding.root.context)
                .load("https://analyticsindiamag.com/wp-content/uploads/2023/09/zomato-1200x600-1.jpg")
                .placeholder(R.drawable.placeholder)
                .into(binding.contentIcon)

            // Handle item click
            binding.root.setOnClickListener {
                onPostClicked(post)
            }

            // Handle item long-click
            binding.root.setOnLongClickListener {
                onPostLongClicked(post)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position), onPostClicked, onPostLongClicked)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Posts>() {
        override fun areItemsTheSame(oldItem: Posts, newItem: Posts): Boolean {
            return oldItem._id == newItem._id // Assuming `_id` is a unique identifier for the post
        }

        override fun areContentsTheSame(oldItem: Posts, newItem: Posts): Boolean {
            return oldItem == newItem
        }
    }
}
