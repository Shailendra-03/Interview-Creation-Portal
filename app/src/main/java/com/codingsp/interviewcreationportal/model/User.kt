package com.codingsp.interviewcreationportal.model

data class User(
    val id: String?= null,
    val name: String?= null,
    val email: String?= null,
    val meetings: List<String> = arrayListOf()
)
