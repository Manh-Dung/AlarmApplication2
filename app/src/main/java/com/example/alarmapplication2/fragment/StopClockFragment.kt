package com.example.alarmapplication2.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarmapplication2.R
import com.example.alarmapplication2.adapter.AlarmAdapter
import com.example.alarmapplication2.adapter.StopClockAdapter
import com.example.alarmapplication2.databinding.FragmentStopClockBinding
import com.example.alarmapplication2.domain.Alarm
import com.example.alarmapplication2.domain.StopClock
import com.example.alarmapplication2.service.StopClockService
import com.example.alarmapplication2.viewmodel.StopClockViewModel

class StopClockFragment : Fragment() {
    private var _binding: FragmentStopClockBinding? = null
    private val binding
        get() = _binding!!

    private var isPlay = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    private val stopClockViewModel: StopClockViewModel by lazy {
        ViewModelProvider(requireActivity())[StopClockViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopClockBinding.inflate(inflater, container, false)

        serviceIntent = Intent(requireActivity(), StopClockService::class.java)
        activity?.registerReceiver(updateTime, IntentFilter(StopClockService.TIMER_UPDATED))
        stopClockLoad(stopClockInit())

        return binding.root
    }

    private fun stopClockInit(): StopClockAdapter {
        val adapter = StopClockAdapter()

        binding.stopClockRecyclerView.setHasFixedSize(true)
        binding.stopClockRecyclerView.adapter = adapter
        binding.stopClockRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        return adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            playStopClockBtn.setOnClickListener {
                startTimer()
                playStopClockBtn.visibility = View.GONE
                flagStopClockBtn.visibility = View.VISIBLE
                pauseStopClockBtn.visibility = View.VISIBLE
            }

            flagStopClockBtn.setOnClickListener {
                if (!isPlay) {
                    resetTimer()

                    flagStopClockBtn.visibility = View.GONE
                    pauseStopClockBtn.visibility = View.GONE
                    playStopClockBtn.visibility = View.VISIBLE
                } else {
                    setFlag()
                }
            }

            pauseStopClockBtn.setOnClickListener {
                startStopTimer()
            }
        }
    }

    private fun setFlag() {
        // Lấy thời gian hiện tại
        val currentTime = getTimeStringFromDouble(time)

        // Lấy thời gian từ lần dừng cuối cùng từ ViewModel
        val lastTime = stopClockViewModel.getAllClocks().value?.lastOrNull()?.time

        // Tính toán khoảng thời gian từ lần dừng cuối cùng
        val preTime = if (lastTime != null) {
            getTimeDifference(lastTime, currentTime)
        } else {
            "00:00:00" // Nếu không có thời gian dừng cuối cùng, đặt preTime thành 0
        }

        // Tạo một đối tượng StopClock mới với thời gian hiện tại và preTime
        val stopClock = StopClock(
            null,
            preTime,
            currentTime
        )

        // Chèn đối tượng StopClock mới vào ViewModel
        stopClockViewModel.insertClock(stopClock)
    }

    private fun getTimeDifference(startTime: String, endTime: String): String {
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val d1: Date = format.parse(startTime)
        val d2: Date = format.parse(endTime)

        // Tính toán sự khác biệt giữa hai thời điểm
        val diff = d2.time - d1.time

        val hours = diff / (60 * 60 * 1000) % 24
        val minutes = diff / (60 * 1000) % 60
        val seconds = diff / 1000 % 60

        return makeTimeString(hours.toInt(), minutes.toInt(), seconds.toInt())
    }

    private fun startStopTimer() {
        if (isPlay)
            stopTimer()
        else
            startTimer()
    }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.stopClockTxt.text = getTimeStringFromDouble(time)
    }

    private fun startTimer() {
        serviceIntent.putExtra(StopClockService.TIME_EXTRA, time)
        activity?.startService(serviceIntent)
        binding.pauseStopClockBtn.setImageResource(R.drawable.pause_ic)
        binding.flagStopClockBtn.setImageResource(R.drawable.flag_ic)
        isPlay = true
    }

    private fun stopTimer() {
        activity?.stopService(serviceIntent)
        binding.pauseStopClockBtn.setImageResource(R.drawable.play_ic)
        binding.flagStopClockBtn.setImageResource(R.drawable.stop_ic)
        isPlay = false
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(StopClockService.TIME_EXTRA, 0.0)
            binding.stopClockTxt.text = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.toInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", hour, min, sec)

    private fun stopClockLoad(adapter: StopClockAdapter) {
        stopClockViewModel.getAllClocks.observe(requireActivity()) {
            adapter.updateData(it)
        }
    }
}