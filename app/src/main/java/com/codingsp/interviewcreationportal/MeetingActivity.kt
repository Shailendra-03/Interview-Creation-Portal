package com.codingsp.interviewcreationportal

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingsp.interviewcreationportal.adapters.UserListAdapter
import com.codingsp.interviewcreationportal.databinding.ActivityMeetingBinding
import com.codingsp.interviewcreationportal.interfces.InterviewClickListeners
import com.codingsp.interviewcreationportal.model.Meeting
import com.codingsp.interviewcreationportal.utils.Constants
import com.codingsp.interviewcreationportal.utils.Resource
import com.codingsp.interviewcreationportal.utils.TimeUtils
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MeetingActivity : AppCompatActivity(), InterviewClickListeners, View.OnClickListener,
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityMeetingBinding
    private var meeting: Meeting? = null
    private val meetingViewModel: MeetingViewModel by viewModels()
    private lateinit var adapter: UserListAdapter
    private var isStartTime = true
    private var startDatePicker: DatePicker? = null
    private var endDatePicker: DatePicker? = null
    private var startTimePicker: TimePicker? = null
    private var endTimePicker: TimePicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleIntent()
        setUpClickListeners()
        observeLifecycleEvents()
        setUpAdapter()
        meetingViewModel.getUserList()
    }

    private fun setUpAdapter() {
        adapter = UserListAdapter(arrayListOf(), meeting?.invitedUsers ?: arrayListOf())
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@MeetingActivity)
            adapter = this@MeetingActivity.adapter
        }
    }

    private fun setUpClickListeners() {
        binding.tvChooseStartDate.setOnClickListener(this)
        binding.tvChooseEndDate.setOnClickListener(this)
        binding.tvChooseStartTime.setOnClickListener(this)
        binding.tvChooseEndTime.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
    }

    private fun observeLifecycleEvents() {
        meetingViewModel.result.observe(this) {
            when (it) {
                is Resource.Success -> {
                    showSnackBar(it.message)
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(Constants.MEETING_EXTRA, meeting)
                    })
                    finish()
                }
                is Resource.Error -> {
                    showSnackBar(it.message)
                }
            }
        }
        meetingViewModel.userList.observe(this) {
            adapter.setData(it)
        }
    }

    private fun handleIntent() {
        if (intent.hasExtra(Constants.MEETING_EXTRA)) {
            meeting = intent.getParcelableExtra(Constants.MEETING_EXTRA)
            updateViews()
        }
    }

    private fun updateViews() {
        meeting?.let {
            binding.tietName.setText(it.name)
            binding.tvChooseStartDate.text = TimeUtils.getDate(it.startTime)
            binding.tvChooseStartTime.text = TimeUtils.getTime(it.startTime)
            binding.tvChooseEndTime.text = TimeUtils.getTime(it.endTime)
            binding.tvChooseEndDate.text = TimeUtils.getDate(it.endTime)
            binding.btnSubmit.text = getString(R.string.update)
            binding.tvTitle.text = getString(R.string.update_meeting)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvChooseStartDate -> {
                isStartTime = true
                getDatePickerDialog().show()
            }
            R.id.tvChooseStartTime -> {
                isStartTime = true
                getTimePickerDialog().show()
            }
            R.id.tvChooseEndDate -> {
                if ((startDatePicker == null || startTimePicker == null) && meeting == null) {
                    return
                }
                isStartTime = false
                getDatePickerDialog().show()
            }
            R.id.tvChooseEndTime -> {
                if ((startDatePicker == null || startTimePicker == null) && meeting == null) {
                    showSnackBar(getString(R.string.please_select_start_date_and_time_first))
                    return
                }
                isStartTime = false
                getTimePickerDialog().show()
            }
            R.id.btnSubmit -> {
                checkForBasicConditionsAndContinue()
            }
        }
    }

    private fun checkForBasicConditionsAndContinue() {
        if (binding.tietName.text.toString().isEmpty()) {
            showSnackBar(getString(R.string.please_enter_a_meeting_name))
        }
        if ((startDatePicker == null || startTimePicker == null) && meeting == null) {
            showSnackBar(getString(R.string.please_select_start_date_and_time))
            return
        }
        if ((endDatePicker == null || endTimePicker == null) && meeting == null) {
            showSnackBar(getString(R.string.please_select_end_date_and_time))
            return
        }
        val calendar = Calendar.getInstance()
        calendar.set(
            startDatePicker!!.year,
            startDatePicker!!.month,
            startDatePicker!!.dayOfMonth,
            startTimePicker!!.hour,
            startTimePicker!!.minute,
            0
        )
        val startTime = calendar.time.time
        calendar.set(
            startDatePicker!!.year,
            startDatePicker!!.month,
            startDatePicker!!.dayOfMonth,
            endTimePicker!!.hour,
            endTimePicker!!.minute,
            0
        )
        val endTime = calendar.time.time
        meeting = Meeting(
            meeting?.id,
            binding.tietName.text.toString(),
            startTime,
            endTime,
            adapter.selectedUser
        )
        meetingViewModel.updateMeeting(meeting!!)
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(datePicker: DatePicker?, year: Int, monthOfyear: Int, day: Int) {
        if (isStartTime) {
            binding.tvChooseStartDate.text = "$day/${monthOfyear + 1}/$year"
            startDatePicker = datePicker
            if (endDatePicker == null && meeting == null) {
                endDatePicker = datePicker
                binding.tvChooseEndDate.text = "$day/${monthOfyear + 1}/$year"
            }
        } else {
            endDatePicker = datePicker
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(timePicker: TimePicker?, hour: Int, minute: Int) {
        if (isStartTime) {
            binding.tvChooseStartTime.text = "$hour:$minute"
            startTimePicker = timePicker
        } else {
            binding.tvChooseEndTime.text = "$hour:$minute"
            endTimePicker = timePicker
        }
    }

    private fun showSnackBar(message: String?) {
        Snackbar.make(
            binding.root,
            message ?: getString(R.string.some_error_occurred),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun getDatePickerDialog(): DatePickerDialog {
        return DatePickerDialog(
            this,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun getTimePickerDialog(): TimePickerDialog {
        return TimePickerDialog(
            this,
            this,
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            Calendar.getInstance().get(Calendar.MINUTE),
            true
        )
    }
}