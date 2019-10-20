package com.motivemobileapp

import android.os.Bundle
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v7.app.AppCompatActivity
import com.motivemobileapp.schedule.ScheduleFragment
import com.motivemobileapp.tasks.TasksFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(handleNavigationItemSelected)
        supportFragmentManager.beginTransaction().replace(R.id.container, TasksFragment.newInstance()).commit()
    }

    private val handleNavigationItemSelected = OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_tasks -> {
                supportFragmentManager.beginTransaction().replace(R.id.container, TasksFragment.newInstance()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {
                supportFragmentManager.beginTransaction().replace(R.id.container, ScheduleFragment.newInstance()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
