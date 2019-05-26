package com.motivepick.motive

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.motivepick.motive.account.AccountFragment
import com.motivepick.motive.schedule.ScheduleFragment
import com.motivepick.motive.tasks.TasksFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val tasksFragment: TasksFragment = TasksFragment.newInstance()
    private val scheduleFragment: ScheduleFragment = ScheduleFragment.newInstance()
    private val accountFragment: AccountFragment = AccountFragment.newInstance()
    private var active: Fragment = tasksFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction().add(R.id.container, accountFragment, "3").hide(accountFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.container, scheduleFragment, "2").hide(scheduleFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.container, tasksFragment, "1").commit()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_tasks -> {
                supportFragmentManager.beginTransaction().hide(active).show(tasksFragment).commit()
                active = tasksFragment
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {
                supportFragmentManager.beginTransaction().hide(active).show(scheduleFragment).commit()
                active = scheduleFragment
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                supportFragmentManager.beginTransaction().hide(active).show(accountFragment).commit()
                active = accountFragment
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
