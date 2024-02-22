package com.example.alarmapplication2.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.alarmapplication2.fragments.AlarmFragment
import com.example.alarmapplication2.fragments.ClockFragment
import com.example.alarmapplication2.fragments.CountDownClockFragment
import com.example.alarmapplication2.fragments.StopClockFragment

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