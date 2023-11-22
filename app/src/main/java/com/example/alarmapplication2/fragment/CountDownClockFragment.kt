package com.example.alarmapplication2.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.alarmapplication2.R
import com.example.alarmapplication2.databinding.FragmentCountDownClockBinding
import java.util.concurrent.TimeUnit


class CountDownClockFragment : Fragment() {
    // View binding
    private var _binding: FragmentCountDownClockBinding? = null
    private val binding
        get() = _binding!!

    // Timer variables
    private lateinit var countDownTimer: CountDownTimer
    private var timerStatus = TimerStatus.STOPPED
    private var timeCountInMilliSeconds = (1 * 60000).toLong()

    // Enum for timer status
    private enum class TimerStatus {
        STARTED, STOPPED
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountDownClockBinding.inflate(inflater, container, false)

        // Set click listeners for buttons
        binding.imageViewReset.setOnClickListener { reset() }
        binding.imageViewStartStop.setOnClickListener { startStop() }

        return binding.root
    }

    private fun reset() {
        stopCountDownTimer()
        startCountDownTimer()
    }

    private fun startStop() {
        if (timerStatus === TimerStatus.STOPPED) {

            // call to initialize the timer values
            setTimerValues()
            // call to initialize the progress bar values
            setProgressBarValues()
            // showing the reset icon
            binding.imageViewReset.visibility = View.VISIBLE
            // changing play icon to stop icon
            binding.imageViewStartStop.setImageResource(R.drawable.stop_count_down_clock_ic)
            // making edit text not editable
            binding.editTextMinute.isEnabled = false
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED
            // call to start the count down timer
            startCountDownTimer()
        } else {

            // hiding the reset icon
            binding.imageViewReset.visibility = View.GONE
            // changing stop icon to start icon
            binding.imageViewStartStop.setImageResource(R.drawable.start_ic)
            // making edit text editable
            binding.editTextMinute.isEnabled = true
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED
            stopCountDownTimer()
        }
    }

    /**
     * method to initialize the values for count down timer
     */
    private fun setTimerValues() {
        var time: Long = 0
        if (binding.editTextMinute.text.toString().isNotEmpty()) {
            time = binding.editTextMinute.text.toString().trim().toLong()
        } else {
            Toast.makeText(
                requireActivity(),
                "Please Enter Minutes...",
                Toast.LENGTH_LONG
            ).show()
        }
        timeCountInMilliSeconds = time * 60 * 1000
    }

    /**
     * method to start count down timer
     */
    private fun startCountDownTimer() {
        countDownTimer = object : CountDownTimer(timeCountInMilliSeconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.textViewTime.text = hmsTimeFormatter(millisUntilFinished)
                binding.progressBarCircle.progress = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                binding.textViewTime.text = hmsTimeFormatter(timeCountInMilliSeconds)
                // call to initialize the progress bar values
                setProgressBarValues()
                // hiding the reset icon
                binding.imageViewReset.visibility = View.GONE
                // changing stop icon to start icon
                binding.imageViewStartStop.setImageResource(R.drawable.start_ic)
                // making edit text editable
                binding.editTextMinute.isEnabled = true
                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED
            }
        }.start()
        countDownTimer.start()
    }

    /**
     * method to stop count down timer
     */
    private fun stopCountDownTimer() {
        countDownTimer.cancel()
    }

    /**
     * method to set circular progress bar values
     */
    private fun setProgressBarValues() {
        binding.progressBarCircle.max = (timeCountInMilliSeconds / 1000).toInt()
        binding.progressBarCircle.progress = (timeCountInMilliSeconds / 1000).toInt()
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private fun hmsTimeFormatter(milliSeconds: Long): String {
        return String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(milliSeconds),
            TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    milliSeconds
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    milliSeconds
                )
            )
        )
    }
}