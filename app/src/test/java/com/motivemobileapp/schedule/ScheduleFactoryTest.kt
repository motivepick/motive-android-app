package com.motivemobileapp.schedule

import com.motivemobileapp.model.Task
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ScheduleFactoryTest {

    @Test
    fun shouldCreateExpectedSchedule() {
        val clock = FakeCurrentDateFactory(2018, 11, 25, 11, 32)
        val yesterday = date(2018, 11, 24, 23, 59)
        val tomorrow = date(2018, 11, 26, 17, 35)
        val december2nd = date(2018, 12, 2, 17, 35)
        val anotherDecember2nd = date(2018, 12, 2, 17, 35)
        val december3rd = date(2018, 12, 3, 17, 35)
        val dayAfterTomorrow = date(2018, 11, 27, 23, 59)
        val tasks = listOf(task(dayAfterTomorrow), task(tomorrow), task(dayAfterTomorrow), task(december2nd), task(december3rd), task(anotherDecember2nd), task(yesterday))
        val schedule = ScheduleFactory(clock).scheduleFor(tasks)
        assertEquals(1, schedule.overdue.size)
        assertEquals(2, schedule.future.size)
        assertEquals(1, schedule.week.getValue(endOfDay(tomorrow)).size)
    }

    private fun task(dueDate: Date): Task {
        return Task(1665, "Cleanup database", "Remove obsolete data", dueDate, false)
    }

    private fun date(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
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
