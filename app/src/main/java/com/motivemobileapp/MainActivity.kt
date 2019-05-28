package com.motivemobileapp

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.motivemobileapp.account.AccountFragment
import com.motivemobileapp.schedule.ScheduleFragment
import com.motivemobileapp.tasks.TasksFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction().add(R.id.container, TasksFragment.newInstance()).commit()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_tasks -> {
                supportFragmentManager.beginTransaction().replace(R.id.container, TasksFragment.newInstance()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {
                supportFragmentManager.beginTransaction().replace(R.id.container, ScheduleFragment.newInstance()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                supportFragmentManager.beginTransaction().replace(R.id.container, AccountFragment.newInstance()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
