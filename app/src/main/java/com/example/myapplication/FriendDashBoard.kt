package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityFriendDashBoardBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FriendDashBoard : AppCompatActivity() {

    private lateinit var binding: ActivityFriendDashBoardBinding
    private lateinit var adapter: FriendAdapter
    private val friends = mutableListOf<Friend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadFriends()

        binding.search.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.searchCard.bringToFront()
                binding.searchCard.invalidate()
                binding.search.setIconified(false)
            }

    }


        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // Not using submit functionality
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterFriends(newText)
                return true
            }
        })


        binding.addList.setOnClickListener {
            val intent = Intent(this, EditFriendDetails::class.java)
            startActivityForResult(intent, REQUEST_ADD_FRIEND)
        }
    }



    private fun loadFriends() {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val friendsJson = sharedPreferences.getString("friends_list", "[]")
        val listType = object : TypeToken<MutableList<Friend>>() {}.type
        val savedFriends: MutableList<Friend> = Gson().fromJson(friendsJson, listType) ?: mutableListOf()
        friends.addAll(savedFriends)
    }
    private fun setupRecyclerView() {
        adapter = FriendAdapter(
            friends,
            onItemClicked = { friend ->
                val intent = Intent(this, AddFriendList::class.java)
                intent.putExtra("friend_data", friend)
                startActivityForResult(intent, REQUEST_EDIT_FRIEND)
            },
            onItemDeleted = { position -> deleteFriend(position) },
            onItemEdited = { position -> editFriend(position) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FriendDashBoard)
            adapter = this@FriendDashBoard.adapter
        }
    }


    private fun saveFriends() {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        sharedPreferences.edit().putString("friends_list", Gson().toJson(friends)).apply()
    }

    private fun deleteFriend(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete this friend?")


        builder.setPositiveButton("Yes") { _, _ ->
            friends.removeAt(position)
            saveFriends()
            adapter.notifyItemRemoved(position)
            Toast.makeText(this, "Friend deleted", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun editFriend(position: Int) {
        val intent = Intent(this, EditFriendDetails::class.java)
        intent.putExtra("friend_data", friends[position])
        startActivityForResult(intent, REQUEST_EDIT_FRIEND)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val updatedFriend = data?.getParcelableExtra<Friend>("updated_friend")
            if (updatedFriend != null) {
                if (requestCode == REQUEST_ADD_FRIEND) {
                    friends.add(updatedFriend)
                    saveFriends()
                    adapter.notifyItemInserted(friends.size - 1)
                } else if (requestCode == REQUEST_EDIT_FRIEND) {
                    val position = friends.indexOfFirst { it.name == updatedFriend.name }
                    if (position != -1) {
                        friends[position] = updatedFriend
                        saveFriends()
                        adapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUEST_ADD_FRIEND = 1
        private const val REQUEST_EDIT_FRIEND = 2
    }
    private fun filterFriends(query: String?) {
        val filteredList = if (!TextUtils.isEmpty(query)) {
            friends.filter {
                it.name!!.contains(query!!, ignoreCase = true) ||
                        it.birthday!!.contains(query, ignoreCase = true)
            }
        } else {
            friends // Show all items if query is empty
        }

        adapter.updateList(filteredList)
    }
}
