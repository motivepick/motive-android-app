package com.motivemobileapp

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.motivemobileapp.account.AccountFragment
import com.motivemobileapp.schedule.ScheduleFragment
import com.motivemobileapp.tasks.TasksFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val tasksFragment: TasksFragment = TasksFragment.newInstance()
    private val scheduleFragment: ScheduleFragment = ScheduleFragment.newInstance()
    private val accountFragment: AccountFragment = AccountFragment.newInstance()
    private val fragmentManager: FragmentManager = supportFragmentManager
    private var active: Fragment = tasksFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        fragmentManager.beginTransaction().add(R.id.container, accountFragment, "3").hide(accountFragment).commit()
        fragmentManager.beginTransaction().add(R.id.container, scheduleFragment, "2").hide(scheduleFragment).commit()
        fragmentManager.beginTransaction().add(R.id.container, tasksFragment, "1").commit()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_tasks -> {
                fragmentManager.beginTransaction().hide(active).show(tasksFragment).commit()
                active = tasksFragment
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {
                fragmentManager.beginTransaction().hide(active).show(scheduleFragment).commit()
                active = scheduleFragment
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                fragmentManager.beginTransaction().hide(active).show(accountFragment).commit()
                active = accountFragment
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
