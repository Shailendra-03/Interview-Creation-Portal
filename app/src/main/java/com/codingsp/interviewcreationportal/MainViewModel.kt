package com.codingsp.interviewcreationportal

import android.app.Application
import androidx.lifecycle.*
import com.codingsp.interviewcreationportal.model.Meeting
import com.codingsp.interviewcreationportal.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    application1: Application
): AndroidViewModel(application1) {

    private val repository by lazy {
        Repository()
    }

    private var _meetingsList = MutableStateFlow<ArrayList<Meeting>>(arrayListOf())
    val meetingList = _meetingsList.asStateFlow()


    private var _errormessage = MutableSharedFlow<String>()
    val errorMessage = _errormessage.asSharedFlow()

    fun getInterviewsList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getInterviewList()
            withContext(Dispatchers.Main) {
                handleResponseData(response)
            }
        }
    }

    private suspend fun handleResponseData(response: Resource<ArrayList<Meeting>>) {
        when(response) {
            is Resource.Success -> {
                _meetingsList.value = response.data ?: arrayListOf()
            }
            is Resource.Error -> {
                _errormessage.emit(response.message ?: "Some Error Occurred")
            }
        }
    }
}