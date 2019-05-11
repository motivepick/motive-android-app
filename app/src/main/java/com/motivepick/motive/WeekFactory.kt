package com.motivepick.motive

import android.content.Context
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
