package com.codingsp.interviewcreationportal.interfces

import com.codingsp.interviewcreationportal.model.Meeting
import com.codingsp.interviewcreationportal.model.User

interface InterviewClickListeners {
    fun onClickItem(position: Int, meeting: Meeting?) {}
    fun onSelectUser(user: User) {}
    fun onClickOk(list: List<User>) {}
}