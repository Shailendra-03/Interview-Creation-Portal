package com.codingsp.interviewcreationportal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meeting(
    var id: String? =null,
    var name: String?= null,
    var startTime: Long?=  null,
    var endTime: Long?= null,
    var invitedUsers: ArrayList<String> = arrayListOf(),
): Parcelable
