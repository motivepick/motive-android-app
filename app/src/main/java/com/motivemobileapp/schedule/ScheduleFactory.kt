package com.motivemobileapp.schedule

import com.motivemobileapp.common.CurrentDateFactory
import com.motivemobileapp.common.StartOfDay
import com.motivemobileapp.model.Schedule
import com.motivemobileapp.model.Task
import java.util.*
import kotlin.collections.LinkedHashMap

class ScheduleFactory(private val currentDateFactory: CurrentDateFactory) {

    fun scheduleFor(tasks: List<Task>): Schedule {
        val now = currentDateFactory.now()
        val tasksWithDueDate = tasks.filter { it.dueDate != null }
        val week: MutableMap<Date, List<Task>> = weekWithoutTasks(now)
        for (dayOfWeek in week.keys) {
            val tasksOfTheDay = tasksWithDueDate.filter { areTheSameDay(dayOfWeek, it.dueDate!!) }
            week[dayOfWeek] = tasksOfTheDay
        }
        val startOfToday = StartOfDay(now).toDate()
        val overdue = tasksWithDueDate.filter { it.dueDate!!.before(startOfToday) }

        val startOfFuture = plusDays(startOfToday, 7)

        val firstFutureTaskOrNull = tasksWithDueDate.asSequence()
            .filter { it.dueDate!!.after(startOfFuture) }
            .sortedBy { it.dueDate }
            .firstOrNull()

        return if (firstFutureTaskOrNull == null) {
            Schedule(week, overdue, emptyList())
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

    private fun weekWithoutTasks(now: Date): MutableMap<Date, List<Task>> {
        val week: MutableMap<Date, List<Task>> = LinkedHashMap()
        val endOfToday = endOfDay(now)
        for (i in 0..6) {
            week[plusDays(endOfToday, i)] = ArrayList()
        }
        return week
    }

    private fun plusDays(date: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, days)
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
