package com.codingsp.interviewcreationportal.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    fun getDate(timeInMillis: Long?): String {
        if (timeInMillis == null) return ""
        return try {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            simpleDateFormat.format(timeInMillis)
        }catch (e: Exception) {
            ""
        }
    }

    fun getTime(timeInMillis: Long?): String {
        if (timeInMillis == null) return ""
        return try {
            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            simpleDateFormat.format(timeInMillis)
        }catch (e:Exception) {
            ""
        }
    }
}
