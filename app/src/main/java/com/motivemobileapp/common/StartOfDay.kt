package com.motivemobileapp.common

import java.util.*

class StartOfDay(private val date: Date) {

    fun toDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun toTime(): Long = toDate().time
}
