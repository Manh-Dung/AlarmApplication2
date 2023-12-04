package com.example.alarmapplication2.activity

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.alarmapplication2.R
import com.example.alarmapplication2.adapter.ViewPagerAdapter
import com.example.alarmapplication2.databinding.ActivityMainBinding
import com.example.alarmapplication2.receiver.Constants
import com.example.alarmapplication2.viewmodel.ActFragViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val actFragViewModel: ActFragViewModel by lazy {
        ViewModelProvider(this)[ActFragViewModel::class.java]
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

        binding.closeDeleteBtn.setOnClickListener {
            binding.deleteSelectLayout.visibility = View.GONE
            binding.tabLayout.visibility = View.VISIBLE

            actFragViewModel.setDeleteLayoutOn(false)
        }

        var isCheckDelete = false
        actFragViewModel.checkAll.observe(this) {
            isCheckDelete = it
        }

        binding.checkAllBtn.setOnClickListener {
            if (isCheckDelete) {
                actFragViewModel.setCheckAll(false)
            } else {
                actFragViewModel.setCheckAll(true)
            }
        }

        actFragViewModel.deleteLayoutOn.observe(this) {
            if (it) {
                binding.deleteSelectLayout.visibility = View.VISIBLE
                binding.tabLayout.visibility = View.GONE

                actFragViewModel.setCheckAll(false)
            } else {
                binding.deleteSelectLayout.visibility = View.GONE
                binding.tabLayout.visibility = View.VISIBLE

                actFragViewModel.setCheckAll(false)
            }
        }
    }
}
