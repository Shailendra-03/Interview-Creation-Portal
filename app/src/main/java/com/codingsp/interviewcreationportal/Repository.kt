package com.codingsp.interviewcreationportal

import android.icu.util.Freezable
import com.codingsp.interviewcreationportal.model.Meeting
import com.codingsp.interviewcreationportal.model.User
import com.codingsp.interviewcreationportal.utils.FirebaseConstants
import com.codingsp.interviewcreationportal.utils.Resource
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

class Repository {

    suspend fun getUsersList(): Resource<ArrayList<User>> {
        return try {
            val docRef = FirebaseConstants.USER_COLLECTION.get().await()
            val userList = docRef.toObjects(User::class.java) as ArrayList<User>
            Resource.Success(userList)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Some Error Occurred")
        }
    }

    suspend fun getInterviewList(): Resource<ArrayList<Meeting>> {
        return try {
            val docRef = FirebaseConstants.MEETING_COLLECTION.get().await()
            val recipeList = docRef.toObjects(Meeting::class.java) as ArrayList<Meeting>
            Resource.Success(recipeList)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Some Error Occurred")
        }
    }

    suspend fun updateMeeting(meeting: Meeting): Resource<String> {
        if (meeting.invitedUsers.size < 2) return Resource.Error("Please select atleast two users")
        if (meeting.startTime!! >= meeting.endTime!!) return Resource.Error("Start Time can not be greater than End time")
        var meetings: List<Meeting> = emptyList()
        try {
            val docRef = FirebaseConstants.MEETING_COLLECTION.whereArrayContainsAny(
                "invitedUsers",
                meeting.invitedUsers
            ).get().await()
            meetings = docRef.toObjects(Meeting::class.java)
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Some Error Occurred")
        }
        for (i in meetings) {
            if (meeting.id == i.id) continue
            if ((meeting.startTime!! >= i.startTime!! && meeting.startTime!! < i.endTime!!) ||
                meeting.endTime!! >= i.startTime!! && meeting.endTime!! < i.endTime!!
            ) {
                return Resource.Error("One of the participants has another meeting scheduled. Please select other time slots")
            }
        }
        if (meeting.id == null) {
            val docRef = FirebaseConstants.MEETING_COLLECTION.document()
            meeting.id = docRef.id
        }
        return uploadMeetingToFirestore(meeting)

    }

    private suspend fun uploadMeetingToFirestore(meeting: Meeting): Resource<String> {
        try {
            FirebaseConstants.MEETING_COLLECTION.document(meeting.id!!).set(meeting).await()
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Some Error Occurred")
        }
        try {
            for (userid in meeting.invitedUsers) {
                FirebaseConstants.USER_COLLECTION.document(userid)
                    .update("meetings", FieldValue.arrayUnion(meeting.id)).await()
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Some Error Occurred")
        }
        return Resource.Success("Meeting Scheduled Successfully")
    }
}