package com.example.alarmapplication2.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.alarmapplication2.fragment.AlarmFragment
import com.example.alarmapplication2.fragment.ClockFragment
import com.example.alarmapplication2.fragment.CountDownClockFragment
import com.example.alarmapplication2.fragment.StopClockFragment

class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AlarmFragment()
            1 -> StopClockFragment()
            2 -> ClockFragment()
            else -> CountDownClockFragment()
        }
    }
}