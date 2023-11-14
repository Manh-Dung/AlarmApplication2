package com.example.alarmapplication2.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.alarmapplication2.R
import com.example.alarmapplication2.adapter.ViewPagerAdapter
import com.example.alarmapplication2.databinding.ActivityMainBinding
import com.example.alarmapplication2.fragment.AlarmFragment
import com.example.alarmapplication2.fragment.ClockFragment
import com.example.alarmapplication2.fragment.CountDownClockFragment
import com.example.alarmapplication2.fragment.StopClockFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.timepicker.MaterialTimePicker
import java.util.Calendar

class MainActivity : AppCompatActivity(), AlarmFragment.OnButtonPressListener {
    private lateinit var binding: ActivityMainBinding
    private val fragments = listOf(
        AlarmFragment(),
        ClockFragment(),
        StopClockFragment(),
        CountDownClockFragment()
    )

    fun getFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.alarm_ic)
                1 -> tab.setIcon(R.drawable.clock_ic)
                2 -> tab.setIcon(R.drawable.timer_ic)
                else -> tab.setIcon(R.drawable.timelapse_ic)
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabIconColor = ContextCompat.getColor(applicationContext, R.color.blue_icon)
                tab?.icon?.colorFilter = BlendModeColorFilter(tabIconColor, BlendMode.SRC_ATOP)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val tabIconColor = ContextCompat.getColor(applicationContext, R.color.gray_icon)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    tab?.icon?.colorFilter = BlendModeColorFilter(tabIconColor, BlendMode.SRC_ATOP)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.closeDeleteBtn.setOnClickListener{
            binding.deleteSelectLayout.visibility = View.GONE
        }

        binding.checkAllBtn.setOnClickListener {
            // Mai dung viewModel
            onActivityCommand(true)
        }



    }

    override fun onButtonPressed(msg: String) {

    }

    override fun onActivityCommand(deleteCheck: Boolean) {

    }
}
