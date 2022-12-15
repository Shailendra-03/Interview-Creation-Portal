package com.codingsp.interviewcreationportal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codingsp.interviewcreationportal.R
import com.codingsp.interviewcreationportal.databinding.ItemInterviewsBinding
import com.codingsp.interviewcreationportal.interfces.InterviewClickListeners
import com.codingsp.interviewcreationportal.model.Meeting
import com.codingsp.interviewcreationportal.utils.TimeUtils
import java.sql.Time

class ScheduledInterviewsAdapter(
    private val context: Context,
    private val listener: InterviewClickListeners,
    private var list: ArrayList<Meeting>
): RecyclerView.Adapter<ScheduledInterviewsAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemInterviewsBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var meeting: Meeting? = null

        fun setDataOnView(item: Meeting) {
            meeting = item
            val startDate = TimeUtils.getDate(item.startTime)
            val startTime = TimeUtils.getTime(item.startTime)
            val endDate = TimeUtils.getDate(item.endTime)
            val endTime = TimeUtils.getTime(item.endTime)
            binding.tvName.text = item.name ?: ""
            binding.tvStartTime.text = String.format(context.getString(R.string.start_time), "$startDate - $startTime")
            binding.tvEndTime.text = String.format(context.getString(R.string.end_time), "$endDate - $endTime")
            binding.tvNumberOfParticipants.text = String.format(context.getString(R.string.number_of_participants), item.invitedUsers.size)
            binding.itemInterview.setOnClickListener(this)
            binding.tvView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            when(view?.id) {
                R.id.itemInterview, R.id.tvView -> {
                    listener.onClickItem(adapterPosition, meeting)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemInterviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setDataOnView(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setList(list: ArrayList<Meeting>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, meeting: Meeting) {
        if(position < list.size && position != -1) {
            list[position] = meeting
            notifyItemChanged(position)
        } else {
            list.add(meeting)
            notifyItemInserted(list.size)
        }
    }
}