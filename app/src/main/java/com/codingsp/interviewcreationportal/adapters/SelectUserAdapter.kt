package com.codingsp.interviewcreationportal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codingsp.interviewcreationportal.R
import com.codingsp.interviewcreationportal.databinding.ItemSelectUserBinding
import com.codingsp.interviewcreationportal.interfces.InterviewClickListeners
import com.codingsp.interviewcreationportal.model.User

class SelectUserAdapter(
    private val listeners: InterviewClickListeners,
    private var list: List<User>
): RecyclerView.Adapter<SelectUserAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemSelectUserBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        private var user: User? = null
        fun setDataOnView(item: User) {
            user = item
            binding.tvName.text = item.name
            binding.checkbox.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            when(view?.id) {
                R.id.checkbox -> {
                    user?.let {
                        listeners.onSelectUser(it)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSelectUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setDataOnView(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setData(list: List<User>) {
        this.list  = list
        notifyDataSetChanged()
    }
}