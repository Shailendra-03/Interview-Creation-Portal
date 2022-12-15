package com.codingsp.interviewcreationportal.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingsp.interviewcreationportal.viewmodels.MeetingViewModel
import com.codingsp.interviewcreationportal.R
import com.codingsp.interviewcreationportal.adapters.SelectUserAdapter
import com.codingsp.interviewcreationportal.databinding.DialogSelectUserListBinding
import com.codingsp.interviewcreationportal.interfces.InterviewClickListeners
import com.codingsp.interviewcreationportal.model.User
import com.google.android.material.snackbar.Snackbar

class SelectUserListDialog: DialogFragment(),InterviewClickListeners, View.OnClickListener {

    private lateinit var binding: DialogSelectUserListBinding
    private val meetingViewModel : MeetingViewModel by activityViewModels()
    private lateinit var adapter: SelectUserAdapter
    private val selectedUserList : ArrayList<User> = arrayListOf()
    private lateinit var listeners: InterviewClickListeners

    companion object {
        const val TAG = "SelectUserListDialog"
        fun newInstance(bundle: Bundle, listeners: InterviewClickListeners) = SelectUserListDialog().apply {
            this.listeners= listeners
            arguments = bundle
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSelectUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        initViews()
        observeLifecycleEvents()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    private fun initViews() {
        binding.progressBar.isVisible = true
        binding.recyclerView.isVisible = false
        binding.tvOk.isVisible  = false
        binding.tvOk.setOnClickListener(this)
        meetingViewModel.getUserList()
    }

    private fun setUpAdapter() {
        adapter = SelectUserAdapter(this, arrayListOf())
        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=this@SelectUserListDialog.adapter
        }
    }

    private fun observeLifecycleEvents() {
        meetingViewModel.userList.observe(viewLifecycleOwner) {
            binding.recyclerView.isVisible = true
            binding.tvOk.isVisible = true
            binding.progressBar.isVisible = false
            adapter.setData(it)
        }
        meetingViewModel.errorMessage.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            dismiss()
        }
    }

    override fun onSelectUser(user: User) {
        if(selectedUserList.contains(user)) {
            selectedUserList.remove(user)
        }else {
            selectedUserList.add(user)
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.tvOk -> {
                listeners.onClickOk(selectedUserList)
                dismiss()
            }
        }
    }
}