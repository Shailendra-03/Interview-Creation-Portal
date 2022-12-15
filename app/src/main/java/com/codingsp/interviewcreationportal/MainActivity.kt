package com.codingsp.interviewcreationportal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingsp.interviewcreationportal.adapters.ScheduledInterviewsAdapter
import com.codingsp.interviewcreationportal.databinding.ActivityMainBinding
import com.codingsp.interviewcreationportal.interfces.InterviewClickListeners
import com.codingsp.interviewcreationportal.model.Meeting
import com.codingsp.interviewcreationportal.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity(), InterviewClickListeners, View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ScheduledInterviewsAdapter
    private var currentPosition = -1
    private val mainViewModel: MainViewModel by viewModels()

    private val meetingResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK) {
            val data = it?.data?.extras?.getParcelable(Constants.MEETING_EXTRA) as Meeting?
            if(data != null && currentPosition != -1) {
                adapter.updateItem(currentPosition, data)
                currentPosition = -1
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpAdapter()
        fetchData()
        observeLifecycleEvents()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.fabAddMeeting.setOnClickListener(this)
    }

    private fun observeLifecycleEvents() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.meetingList.collectLatest {
                binding.progressBar.isVisible = false
                adapter.setList(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            mainViewModel.errorMessage.collectLatest {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun fetchData() {
        mainViewModel.getInterviewsList()
    }

    private fun setUpAdapter() {
        adapter = ScheduledInterviewsAdapter(this,this, arrayListOf())
        binding.rvInterviews.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = this@MainActivity.adapter
        }
    }

    override fun onClickItem(position: Int, meeting: Meeting?) {
        currentPosition = position
        val intent = Intent(this, MeetingActivity::class.java).apply {
            putExtra(Constants.MEETING_EXTRA, meeting)
        }
        meetingResultLauncher.launch(intent)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.fabAddMeeting -> {
                startActivity(Intent(this, MeetingActivity::class.java))
            }
        }
    }
}