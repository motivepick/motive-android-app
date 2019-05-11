package com.motivepick.motive.model

import java.util.*

class Schedule(val week: Map<Date, List<TaskViewItem>>, val overdue: List<TaskViewItem>, val future: List<TaskViewItem>) {

    override fun toString(): String {
        return "Schedule(week=$week, overdue=$overdue, future=$future)"
    }
}
