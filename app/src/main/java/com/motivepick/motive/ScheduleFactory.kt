package com.motivepick.motive

import com.motivepick.motive.common.CurrentDateFactory
import com.motivepick.motive.model.Schedule
import com.motivepick.motive.model.TaskViewItem
import java.util.*
import kotlin.collections.LinkedHashMap

class ScheduleFactory(private val currentDateFactory: CurrentDateFactory) {

    fun scheduleFor(tasks: List<TaskViewItem>): Schedule {
        val now = currentDateFactory.now()
        val tasksWithDueDate = tasks.filter { it.dueDate != null }
        val week: MutableMap<Date, List<TaskViewItem>> = week(now)
        for (dayOfWeek in week.keys) {
            val tasksOfTheDay = tasksWithDueDate.filter { areTheSameDay(dayOfWeek, it.dueDate!!) }
            week[dayOfWeek] = tasksOfTheDay
        }
        val startOfToday = startOfDay(now)
        val overdue = tasksWithDueDate.filter { it.dueDate!!.before(startOfToday) }

        val startOfFuture = plusDays(startOfToday, 7)

        val firstFutureTaskOrNull = tasksWithDueDate.asSequence()
            .filter { it.dueDate!!.after(startOfFuture) }
            .sortedBy { it.dueDate }
            .firstOrNull()

        return if (firstFutureTaskOrNull == null) {
            Schedule(week, overdue, listOf())
        } else {
            val futureTasks = tasksWithDueDate.filter { areTheSameDay(firstFutureTaskOrNull.dueDate!!, it.dueDate!!) }
            Schedule(week, overdue, futureTasks)
        }
    }

    private fun areTheSameDay(first: Date, second: Date): Boolean {
        val firstCalendar = Calendar.getInstance()
        firstCalendar.time = first
        val secondCalendar = Calendar.getInstance()
        secondCalendar.time = second
        return firstCalendar.get(Calendar.DAY_OF_YEAR) == secondCalendar.get(Calendar.DAY_OF_YEAR)
                && firstCalendar.get(Calendar.YEAR) == secondCalendar.get(Calendar.YEAR)
    }

    private fun week(now: Date): MutableMap<Date, List<TaskViewItem>> {
        val schedule: MutableMap<Date, List<TaskViewItem>> = LinkedHashMap()
        val endOfToday = endOfDay(now)
        for (i in 0..6) {
            schedule[plusDays(endOfToday, i)] = ArrayList()
        }
        return schedule
    }

    private fun plusDays(date: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, days)
        return calendar.time
    }

    private fun startOfDay(now: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = now
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    private fun endOfDay(now: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = now
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }
}
