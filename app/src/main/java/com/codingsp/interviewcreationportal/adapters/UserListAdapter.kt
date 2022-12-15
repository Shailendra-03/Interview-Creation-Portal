package com.codingsp.interviewcreationportal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codingsp.interviewcreationportal.R
import com.codingsp.interviewcreationportal.databinding.ItemSelectUserBinding
import com.codingsp.interviewcreationportal.model.User

class UserListAdapter(
    var list: List<User>,
    val selectedUser: ArrayList<String>
) : RecyclerView.Adapter<UserListAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ItemSelectUserBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var user: User? = null

        fun setData(item: User) {
            user = item
            binding.tvName.text = item.name ?: ""
            binding.checkbox.isChecked = selectedUser.contains(item.id)
            binding.checkbox.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.checkbox -> {
                    updateSelectedUser()
                }
            }
        }

        private fun updateSelectedUser() {
            if (selectedUser.contains(user?.id)) {
                selectedUser.remove(user?.id)
            } else {
                user?.id?.let {
                    selectedUser.add(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemSelectUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setData(list: List<User>) {
        this.list = list
        notifyDataSetChanged()
    }
}