package com.motivepick.motive.schedule

import android.content.Context
import com.motivepick.motive.R
import java.util.Calendar.*

class WeekFactory(private val context: Context) {

    fun createWeek(): Map<Int, String> {
        return mapOf(
            MONDAY to context.getString(R.string.title_monday),
            TUESDAY to context.getString(R.string.title_tuesday),
            WEDNESDAY to context.getString(R.string.title_wednesday),
            THURSDAY to context.getString(R.string.title_thursday),
            FRIDAY to context.getString(R.string.title_friday),
            SATURDAY to context.getString(R.string.title_saturday),
            SUNDAY to context.getString(R.string.title_sunday)
        )
    }
}
