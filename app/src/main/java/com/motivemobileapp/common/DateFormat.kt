package com.motivemobileapp.common

import java.text.SimpleDateFormat
import java.util.*

object DateFormat {

    fun format(date: Date): String = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(date)
}
