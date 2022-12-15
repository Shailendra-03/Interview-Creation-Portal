package com.codingsp.interviewcreationportal.utils

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseConstants {
    val USER_COLLECTION = FirebaseFirestore.getInstance().collection("Users")
    val MEETING_COLLECTION = FirebaseFirestore.getInstance().collection("Meetings")
}