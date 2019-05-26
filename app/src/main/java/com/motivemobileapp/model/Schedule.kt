package com.motivemobileapp.model

import java.util.*

class Schedule(val week: Map<Date, List<Task>>, val overdue: List<Task>, val future: List<Task>) {

    override fun toString(): String {
        return "Schedule(week=$week, overdue=$overdue, future=$future)"
    }
}
