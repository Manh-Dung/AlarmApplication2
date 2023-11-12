package com.example.alarmapplication2.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.ColorFilter
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.alarmapplication2.R
import com.example.alarmapplication2.adapter.ViewPagerAdapter
import com.example.alarmapplication2.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.timepicker.MaterialTimePicker
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.alarm_ic)
                1 -> tab.setIcon(R.drawable.clock_ic)
                2 -> tab.setIcon(R.drawable.stop_clock_ic)
                else -> tab.setIcon(R.drawable.count_down_clock_ic)
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabIconColor = ContextCompat.getColor(applicationContext, R.color.blue_icon)
                tab?.icon?.colorFilter = BlendModeColorFilter(tabIconColor, BlendMode.SRC_ATOP)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val tabIconColor = ContextCompat.getColor(applicationContext, R.color.gray_icon)
                tab?.icon?.colorFilter = BlendModeColorFilter(tabIconColor, BlendMode.SRC_ATOP)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}
