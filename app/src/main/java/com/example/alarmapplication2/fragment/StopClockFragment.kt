package com.example.alarmapplication2.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarmapplication2.R
import com.example.alarmapplication2.adapter.StopClockAdapter
import com.example.alarmapplication2.databinding.FragmentStopClockBinding
import com.example.alarmapplication2.domain.StopClock
import com.example.alarmapplication2.service.StopClockService
import com.example.alarmapplication2.viewmodel.StopClockViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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


    private fun stopClockLoad(adapter: StopClockAdapter) {
        stopClockViewModel.getAllClocks.observe(requireActivity()) {
            adapter.updateData(it)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
    }

    private fun setupButtons() {
        binding.apply {
            playStopClockBtn.setOnClickListener {
                startTimer()
                playStopClockBtn.visibility = View.GONE
                flagStopClockBtn.visibility = View.VISIBLE
                pauseStopClockBtn.visibility = View.VISIBLE
            }
            flagStopClockBtn.setOnClickListener {
                handleFlagClick()
            }
            pauseStopClockBtn.setOnClickListener {
                startStopTimer()
            }
        }

    }

    private fun handleFlagClick() {
        if (!isPlay) {
            binding.flagStopClockBtn.visibility = View.GONE
            binding.pauseStopClockBtn.visibility = View.GONE
            binding.playStopClockBtn.visibility = View.VISIBLE

            resetTimer()
            stopClockViewModel.deleteClock()
            animateViews(200f, 60f)
        } else {
            animateViews(-500f, 40f)
            setFlag()
        }
    }

    private fun animateViews(translationY: Float, textSize: Float) {
        binding.stopClockTxt.animate().apply {
            duration = 300
            translationY(translationY)
        }.start()

        binding.scrollLayout.animate().apply {
            duration = 300
            translationY(translationY)
        }.start()

        binding.stopClockTxt.textSize = textSize
    }

    private fun setFlag() {
        val currentTime = getTimeStringFromDouble(time)

        val lastTime = stopClockViewModel.getAllClocks.value?.firstOrNull()?.time

        val preTime = if (lastTime != null) {
            getTimeDifference(lastTime, currentTime)
        } else {
            "00:00:00"
        }

        val stopClock = StopClock(
            null,
            preTime,
            currentTime
        )

        stopClockViewModel.insertClock(stopClock)
    }

    private fun startStopTimer() {
        if (isPlay) stopTimer() else startTimer()
    }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.stopClockTxt.text = getTimeStringFromDouble(time)
    }

    private fun startTimer() {
        serviceIntent.putExtra(StopClockService.TIME_EXTRA, time)
        activity?.startService(serviceIntent)

        binding.apply {
            pauseStopClockBtn.setImageResource(R.drawable.pause_ic)
            flagStopClockBtn.setImageResource(R.drawable.flag_ic)
        }

        isPlay = true
    }

    private fun stopTimer() {
        activity?.stopService(serviceIntent)

        binding.apply {
            pauseStopClockBtn.setImageResource(R.drawable.play_ic)
            flagStopClockBtn.setImageResource(R.drawable.stop_ic)
        }

        isPlay = false
    }

    private fun getTimeDifference(startTime: String, endTime: String): String {
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val d1: Date = format.parse(startTime)
        val d2: Date = format.parse(endTime)

        val diff = d2.time - d1.time

        val hours = diff / (60 * 60 * 1000) % 24
        val minutes = diff / (60 * 1000) % 60
        val seconds = diff / 1000 % 60

        return makeTimeString(hours.toInt(), minutes.toInt(), seconds.toInt())
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

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(StopClockService.TIME_EXTRA, 0.0)
            binding.stopClockTxt.text = getTimeStringFromDouble(time)
        }
    }




}