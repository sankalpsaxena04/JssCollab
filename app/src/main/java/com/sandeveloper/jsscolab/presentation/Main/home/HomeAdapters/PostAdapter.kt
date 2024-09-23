package com.sandeveloper.jsscolab.presentation.Main.home.HomeAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.databinding.PostListItemBinding
import com.sandeveloper.jsscolab.domain.Modules.Post.Posts
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class PostAdapter(private val onPostClicked: (Posts) -> Unit) : ListAdapter<Posts, PostAdapter.PostViewHolder>(
    ComparatorDiffUtil()
) {
    class PostViewHolder(val binding: PostListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Posts, onPostClicked: (Posts) -> Unit) {
            val percentage = if (post.sender_contribution < post.total_required_amount) {
                (post.sender_contribution.toFloat() / post.total_required_amount.toFloat()) * 100
            } else {
                100f
            }

            // Set binding data based on `Posts`
            binding.creatorName.text = post.sender.full_name
            binding.categoryName.text = post.category
            binding.expirationDate.text = getExpirationTime(post.expiration_date)
            binding.contributionLabel.text = "₹${post.sender_contribution}"
            binding.totalAmount.text = "₹${post.total_required_amount}"
            binding.postDescription.text = post.comment
            binding.contributionProgress.progress = percentage.toInt()

            // Load images using Glide
            Glide.with(binding.root.context)
                .load(post.sender.photo?.secure_url)
                .placeholder(R.drawable.profile_pic_placeholder)
                .into(binding.creatorPhoto)

            // Assuming contentIcon is still a placeholder image, replace with actual data if needed
            Glide.with(binding.root.context)
                .load("https://analyticsindiamag.com/wp-content/uploads/2023/09/zomato-1200x600-1.jpg")
                .placeholder(R.drawable.placeholder)
                .into(binding.contentIcon)

            // Handle progress bar tag positioning
            binding.contributionProgress.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    adjustTagPosition(percentage)
                    binding.contributionProgress.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })

            // Handle item click to save the post and open the new activity
            binding.root.setOnClickListener {
                onPostClicked(post)
            }
        }

        private fun adjustTagPosition(percentage: Float) {
            val progressBarWidth = binding.contributionProgress.width
            val progressPosition = (progressBarWidth * (percentage / 100f)).toInt()
            val labelWidth = binding.contributionCard.width
            val centeredPosition = if (percentage < 50) {
                progressPosition - (labelWidth / 2) + 5
            } else if (percentage == 100f) {
                progressPosition - (labelWidth / 2) - 5
            } else {
                progressPosition - (labelWidth / 2)
            }

            val layoutParams = binding.contributionCard.layoutParams as LinearLayout.LayoutParams
            layoutParams.marginStart = centeredPosition.coerceIn(0, progressBarWidth - labelWidth)
            binding.contributionCard.layoutParams = layoutParams
        }

        fun getExpirationTime(isoString: String): String {
            val dateTime = LocalDateTime.parse(isoString, DateTimeFormatter.ISO_DATE_TIME)
            val now = LocalDateTime.now(ZoneOffset.UTC)
            val duration = Duration.between(now, dateTime)

            return when {
                duration.toDays() > 0 -> "Expires in ${duration.toDays()} day(s)"
                duration.toHours() > 0 -> "Expires in ${duration.toHours()} hour(s)"
                duration.toMinutes() > 0 -> "Expires in ${duration.toMinutes()} minute(s)"
                else -> "Expired"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Posts>() {
        override fun areItemsTheSame(oldItem: Posts, newItem: Posts): Boolean {
            return oldItem._id == newItem._id // Assuming `_id` is a unique identifier for the post
        }

        override fun areContentsTheSame(oldItem: Posts, newItem: Posts): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position), onPostClicked)
    }
}
