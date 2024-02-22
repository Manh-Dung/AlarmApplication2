package com.example.alarmapplication2.activities

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
import com.example.alarmapplication2.adapters.ViewPagerAdapter
import com.example.alarmapplication2.databinding.ActivityMainBinding
import com.example.alarmapplication2.viewmodels.AppViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val appViewModel: AppViewModel by lazy {
        ViewModelProvider(this)[AppViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.alarm_ic)
                1 -> tab.setIcon(R.drawable.clock_ic)
                2 -> tab.setIcon(R.drawable.timer_ic)
                else -> tab.setIcon(R.drawable.timelapse_ic)
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabIconColor = ContextCompat.getColor(
                    applicationContext,
                    R.color.blue_icon
                )
                tab?.icon?.colorFilter =
                    BlendModeColorFilter(tabIconColor, BlendMode.SRC_ATOP)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val tabIconColor = ContextCompat.getColor(
                    applicationContext,
                    R.color.white
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    tab?.icon?.colorFilter =
                        BlendModeColorFilter(tabIconColor, BlendMode.SRC_ATOP)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        appViewModel.deleteLayoutOn.observe(this) {
            if (it) {
                binding.deleteSelectLayout.visibility = View.VISIBLE
                binding.tabLayout.visibility = View.GONE
            } else {
                binding.deleteSelectLayout.visibility = View.GONE
                binding.tabLayout.visibility = View.VISIBLE
            }
        }

        appViewModel.countCheckedAlarms.observe(this) {
            binding.showDeleteTxt.text = "Đã chọn $it mục"
        }

        binding.closeDeleteBtn.setOnClickListener {
            binding.deleteSelectLayout.visibility = View.GONE
            binding.tabLayout.visibility = View.VISIBLE

            appViewModel.setDeleteLayoutOn(false)
        }

        binding.checkAllBtn.setOnClickListener {
            appViewModel.setCheckAll(appViewModel.checkAll.value == false)
        }

    }
}
