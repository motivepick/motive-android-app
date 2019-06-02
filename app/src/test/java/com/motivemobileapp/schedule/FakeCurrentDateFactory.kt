package com.motivemobileapp.schedule

import com.motivemobileapp.common.CurrentDateFactory
import java.util.*

class FakeCurrentDateFactory(private val year: Int, private val month: Int, private val dayOfMonth: Int, private val hour: Int, private val minute: Int) :
    CurrentDateFactory {

    override fun now(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        return calendar.time
    }
}
