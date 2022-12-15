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

    fun getYear(timeInMillis: Long): Int {
        return try {
            val simpleDateFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
            return simpleDateFormat.format(timeInMillis).toInt()
        }catch (e:Exception) {
            Calendar.getInstance().time.year
        }
    }

    fun getMonth(timeInMillis: Long): Int {
        return try {
            val simpleDateFormat = SimpleDateFormat("MM", Locale.ENGLISH)
            return simpleDateFormat.format(timeInMillis).toInt() -1
        }catch (e:Exception) {
            Calendar.getInstance().time.month
        }
    }

    fun getDay(timeInMillis: Long): Int {
        return try {
            val simpleDateFormat = SimpleDateFormat("dd", Locale.ENGLISH)
            return simpleDateFormat.format(timeInMillis).toInt()
        }catch (e:Exception) {
            Calendar.getInstance().time.day
        }
    }

    fun getHour(timeInMillis: Long): Int {
        return try {
            val simpleDateFormat = SimpleDateFormat("HH", Locale.ENGLISH)
            return simpleDateFormat.format(timeInMillis).toInt()
        }catch (e:Exception) {
            Calendar.getInstance().time.day
        }
    }

    fun getMinutes(timeInMillis: Long): Int {
        return try {
            val simpleDateFormat = SimpleDateFormat("mm", Locale.ENGLISH)
            return simpleDateFormat.format(timeInMillis).toInt()
        }catch (e:Exception) {
            Calendar.getInstance().time.day
        }
    }

}
