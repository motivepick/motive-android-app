package com.motivepick.motive

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.motivepick.motive.schedule.ScheduleFragment
import com.motivepick.motive.tasks.TasksFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openFragment(TasksFragment.newInstance())
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_tasks -> {
                openFragment(TasksFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {
                openFragment(ScheduleFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                openFragment(AccountFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
