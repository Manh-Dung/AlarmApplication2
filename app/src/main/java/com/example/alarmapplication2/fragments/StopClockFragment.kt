package com.example.alarmapplication2.fragments

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
import com.example.alarmapplication2.adapters.StopClockAdapter
import com.example.alarmapplication2.databinding.FragmentStopClockBinding
import com.example.alarmapplication2.models.StopClock
import com.example.alarmapplication2.services.StopClockService
import com.example.alarmapplication2.viewmodels.StopClockViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class StopClockFragment : Fragment() {
    // Binding for the fragment's view
    private var _binding: FragmentStopClockBinding? = null
    private val binding
        get() = _binding!!

    // Flag to check if the stop clock is playing
    private var isPlay = false

    // Intent for the stop clock service
    private lateinit var serviceIntent: Intent

    // The current time of the stop clock
    private var time = 0.0

    // ViewModel for managing stop clocks
    private val stopClockViewModel: StopClockViewModel by lazy {
        ViewModelProvider(requireActivity())[StopClockViewModel::class.java]
    }

    // Inflates the fragment's view, initializes the stop clock service and loads the stop clocks
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStopClockBinding.inflate(inflater, container, false)

        serviceIntent = Intent(requireActivity(), StopClockService::class.java)
        activity?.registerReceiver(
            updateTime,
            IntentFilter(StopClockService.TIMER_UPDATED)
        )

        stopClockLoad(stopClockInit())

        return binding.root
    }

    /**
     * Sets up the button click listeners after the view has been created.
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
    }

    /**
     * Initializes the StopClockAdapter and sets up the RecyclerView for displaying stop clocks.
     * @return The initialized StopClockAdapter.
     */
    private fun stopClockInit(): StopClockAdapter {
        val adapter = StopClockAdapter()

        binding.stopClockRecyclerView.setHasFixedSize(true)
        binding.stopClockRecyclerView.adapter = adapter
        binding.stopClockRecyclerView.layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        return adapter
    }

    /**
     * Observes and loads all stop clocks from the ViewModel into the provided adapter.
     * @param adapter The StopClockAdapter to update with the observed data.
     */
    private fun stopClockLoad(adapter: StopClockAdapter) {
        stopClockViewModel.getAllClocks.observe(requireActivity()) {
            adapter.updateData(it)
        }
    }

    /**
     * Sets up the click listeners for the play, flag, and pause buttons.
     */
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

    /**
     * Handles the click event for the flag button.
     * If the stop clock is not playing, it resets the timer and deletes the clock.
     * If the stop clock is playing, it sets a flag.
     */
    private fun handleFlagClick() {
        if (!isPlay) {
            binding.flagStopClockBtn.visibility = View.GONE
            binding.pauseStopClockBtn.visibility = View.GONE
            binding.playStopClockBtn.visibility = View.VISIBLE

            resetTimer()
            stopClockViewModel.deleteAll()
            animateViews(200f, 60f)
        } else {
            animateViews(-500f, 40f)
            setFlag()
        }
    }

    /**
     * Animates the stop clock text and scroll layout views by translating them vertically and changing the text size of the stop clock text.
     * @param translationY The amount of vertical translation.
     * @param textSize The new text size.
     */
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

    /**
     * Sets a flag at the current time and inserts a new stop clock into the ViewModel.
     */
    private fun setFlag() {
        val currentTime = getTimeStringFromDouble(time)

        val lastTime =
            stopClockViewModel.getAllClocks.value?.firstOrNull()?.time

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

    /**
     * Starts or stops the timer based on its current state.
     */
    private fun startStopTimer() {
        if (isPlay) stopTimer() else startTimer()
    }


    /**
     * Resets the timer by stopping it and setting the time to 0.
     */
    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.stopClockTxt.text = getTimeStringFromDouble(time)
    }

    /**
     * Starts the timer and updates the UI to reflect this state.
     */
    private fun startTimer() {
        serviceIntent.putExtra(StopClockService.TIME_EXTRA, time)
        activity?.startService(serviceIntent)

        binding.apply {
            pauseStopClockBtn.setImageResource(R.drawable.pause_ic)
            flagStopClockBtn.setImageResource(R.drawable.flag_ic)
        }

        isPlay = true
    }

    /**
     * Stops the timer and updates the UI to reflect this state.
     */
    private fun stopTimer() {
        activity?.stopService(serviceIntent)

        binding.apply {
            pauseStopClockBtn.setImageResource(R.drawable.play_ic)
            flagStopClockBtn.setImageResource(R.drawable.stop_ic)
        }

        isPlay = false
    }

    /**
     * Calculates the time difference between two time strings.
     * @param startTime The start time as a string in the format "HH:mm:ss".
     * @param endTime The end time as a string in the format "HH:mm:ss".
     * @return The time difference as a string in the format "HH:mm:ss".
     */
    private fun getTimeDifference(startTime: String, endTime: String): String {
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val diff = format.parse(endTime).time - format.parse(startTime).time
        val hours = diff / (60 * 60 * 1000) % 24
        val minutes = diff / (60 * 1000) % 60
        val seconds = diff / 1000 % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    /**
     * Converts a time value from a double to a string in the format "HH:mm:ss".
     * @param time The time value as a double.
     * @return The time value as a string in the format "HH:mm:ss".
     */
    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.toInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    /**
     * Formats a time value into a string in the format "HH:mm:ss".
     * @param hour The hours component of the time.
     * @param min The minutes component of the time.
     * @param sec The seconds component of the time.
     * @return The time value as a string in the format "HH:mm:ss".
     */
    private fun makeTimeString(hour: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", hour, min, sec)

    /**
     * BroadcastReceiver that updates the stop clock text with the current time when it receives an update from the stop clock service.
     */
    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(StopClockService.TIME_EXTRA, 0.0)
            binding.stopClockTxt.text = getTimeStringFromDouble(time)
        }
    }
}