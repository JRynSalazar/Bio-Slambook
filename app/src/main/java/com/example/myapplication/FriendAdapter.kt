package com.example.myapplication

import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.CustomListBinding

class FriendAdapter(
    private var friends: MutableList<Friend>, // Changed to var for dynamic list updates
    private val onItemClicked: (Friend) -> Unit,
    private val onItemDeleted: (Int) -> Unit,
    private val onItemEdited: (Int) -> Unit
) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    // ViewHolder for managing item views
    inner class FriendViewHolder(private val binding: CustomListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend, position: Int) {
            binding.fullName.text = friend.name
            binding.birthday.text = friend.birthday

            // Load profile picture if available
            friend.profilePicture?.let {
                val imageBytes = Base64.decode(it, Base64.DEFAULT)
                Glide.with(binding.profile.context)
                    .asBitmap()
                    .load(imageBytes)
                    .into(binding.profile)
            } ?: run {
                binding.profile.setImageResource(R.drawable.ic_profile) // Default image
            }

            binding.root.setOnClickListener { onItemClicked(friend) }

            val cardView: CardView = binding.cardView15
            binding.expand.setOnClickListener {
                cardView.visibility = if (cardView.visibility == View.GONE) View.VISIBLE else View.GONE
            }

            // Set delete button functionality
            binding.delete.setOnClickListener {
                onItemDeleted(position)
            }

            // Set edit button functionality
            binding.edit.setOnClickListener {
                onItemEdited(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = CustomListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(friends[position], position)
    }

    override fun getItemCount(): Int = friends.size

    // Update the list dynamically and refresh the RecyclerView
    fun updateList(newList: List<Friend>) {
        friends = newList.toMutableList() // Update the adapter's list
        notifyDataSetChanged() // Refresh the RecyclerView
    }
}
