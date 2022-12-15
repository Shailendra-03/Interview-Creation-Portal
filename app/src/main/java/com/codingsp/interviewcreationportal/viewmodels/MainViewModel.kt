package com.codingsp.interviewcreationportal.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.codingsp.interviewcreationportal.R
import com.codingsp.interviewcreationportal.repositories.Repository
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
    private val application1: Application
): AndroidViewModel(application1) {

    private val repository by lazy {
        Repository(application1)
    }

    private var _meetingsList = MutableStateFlow<ArrayList<Meeting>>(arrayListOf())
    val meetingList = _meetingsList.asStateFlow()


    private var _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

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
                _errorMessage.emit(response.message ?: application1.getString(R.string.some_error_occurred))
            }
        }
    }
}