package com.codingsp.interviewcreationportal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codingsp.interviewcreationportal.model.Meeting
import com.codingsp.interviewcreationportal.model.User
import com.codingsp.interviewcreationportal.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class MeetingViewModel(application1: Application): AndroidViewModel(application1) {

    private val repository by lazy {
        Repository()
    }

    private var _result = MutableLiveData<Resource<String>>()
    val result: LiveData<Resource<String>> get() = _result

    private var _userList = MutableLiveData<ArrayList<User>>(arrayListOf())
    val userList : LiveData<ArrayList<User>> get() = _userList

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> get() = _errorMessage

    fun updateMeeting(meeting: Meeting) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.updateMeeting(meeting)
            withContext(Dispatchers.Main) {
                _result.value = response
            }
        }
    }

    fun getUserList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getUsersList()
            withContext(Dispatchers.Main) {
                handleResponseData(response)
            }
        }
    }

    private fun handleResponseData(response: Resource<ArrayList<User>>) {
        when(response) {
            is Resource.Success -> {
                _userList.value = response.data ?: arrayListOf()
            }
            is Resource.Error -> {
                _errorMessage.value = response.message ?: "Some Error Occurred"
            }
        }
    }
}