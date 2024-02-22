package com.example.alarmapplication2.fragments

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.alarmapplication2.R
import com.example.alarmapplication2.databinding.FragmentCountDownClockBinding
import java.util.concurrent.TimeUnit


class CountDownClockFragment : Fragment() {
    private var _binding: FragmentCountDownClockBinding? = null
    private val binding
        get() = _binding!!

    // Timer variables
    private lateinit var countDownTimer: CountDownTimer
    private var timerStatus = TimerStatus.STOPPED
    private var timeCountInMilliSeconds = (1 * 60000).toLong()
    private var remainingTime: Long = 1

    // Enum for timer status
    private enum class TimerStatus {
        STARTED, STOPPED, PAUSED, RESUMED
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            FragmentCountDownClockBinding.inflate(inflater, container, false)

        // Set click listeners for buttons
        binding.playCountDownClockBtn?.setOnClickListener { startStop() }
        binding.stopCountDownClockBtn?.setOnClickListener { startStop() }
        binding.pauseCountDownClockBtn?.setOnClickListener { pauseResumeCountDownTimer() }

        setNumberPickerProperties(binding.hourPicker, 0, 23, 0, 90f)
        setNumberPickerProperties(binding.minutePicker, 0, 59, 0, 90f)
        setNumberPickerProperties(binding.secondPicker, 0, 59, 1, 90f)

        return binding.root
    }

    /**
     * method to set number picker properties
     * @param picker the number picker
     * @param minValue the minimum value of the picker
     * @param maxValue the maximum value of the picker
     * @param value the default value of the picker
     * @param textSize the text size of the picker
     */
    fun setNumberPickerProperties(
        picker: NumberPicker,
        minValue: Int,
        maxValue: Int,
        value: Int,
        textSize: Float,
    ) {
        picker.minValue = minValue
        picker.maxValue = maxValue
        picker.value = value
        picker.textSize = textSize
    }

    private fun startStop() {
        if (timerStatus === TimerStatus.STOPPED) {

            binding.relativeLayout?.visibility = View.VISIBLE
            binding.linearLayoutNumberPickers.visibility = View.GONE
            binding.playCountDownClockBtn?.visibility = View.GONE
            binding.pauseCountDownClockBtn?.visibility = View.VISIBLE
            binding.stopCountDownClockBtn?.visibility = View.VISIBLE

            setTimerValues()
            setProgressBarValues()

            timerStatus = TimerStatus.STARTED

            startCountDownTimer()
        } else {
            binding.relativeLayout?.visibility = View.GONE
            binding.linearLayoutNumberPickers.visibility = View.VISIBLE

            binding.playCountDownClockBtn?.visibility = View.VISIBLE
            binding.pauseCountDownClockBtn?.visibility = View.GONE
            binding.stopCountDownClockBtn?.visibility = View.GONE


            timerStatus = TimerStatus.STOPPED
            stopCountDownTimer()
        }
    }

    /**
     * method to initialize the values for count down timer
     */
    private fun setTimerValues() {
        var time: Long = 0
        time =
            (binding.hourPicker.value * 60 * 60 + binding.minutePicker.value * 60 + binding.secondPicker.value).toLong()
        timeCountInMilliSeconds = time * 1000
    }

    /**
     * method to start count down timer
     */
    private fun startCountDownTimer() {
        countDownTimer =
            object : CountDownTimer(timeCountInMilliSeconds, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.textViewTime.text =
                        hmsTimeFormatter(millisUntilFinished)
                    binding.progressBarCircle.progress =
                        (millisUntilFinished / 1000).toInt()
                    remainingTime = millisUntilFinished

                    if (timerStatus == TimerStatus.RESUMED) {
                        binding.textViewTime.text =
                            hmsTimeFormatter(remainingTime)
                        binding.progressBarCircle.progress =
                            (remainingTime / 1000).toInt()
                    } else if (timerStatus == TimerStatus.PAUSED) {
                        countDownTimer.cancel()
                    }
                }

                override fun onFinish() {
                    binding.textViewTime.text =
                        hmsTimeFormatter(timeCountInMilliSeconds)
                    setProgressBarValues()

                    binding.relativeLayout?.visibility = View.GONE

                    binding.linearLayoutNumberPickers.visibility = View.VISIBLE

                    binding.playCountDownClockBtn?.visibility = View.VISIBLE
                    binding.pauseCountDownClockBtn?.visibility = View.GONE
                    binding.stopCountDownClockBtn?.visibility = View.GONE

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

    private fun pauseResumeCountDownTimer() {
        if (timerStatus == TimerStatus.STARTED || timerStatus == TimerStatus.RESUMED) {
            countDownTimer.cancel()
            timeCountInMilliSeconds = remainingTime
            timerStatus = TimerStatus.PAUSED
            binding.pauseCountDownClockBtn?.setImageResource(R.drawable.play_ic)
        } else if (timerStatus == TimerStatus.PAUSED) {
            binding.pauseCountDownClockBtn?.setImageResource(R.drawable.pause_ic)
            timerStatus = TimerStatus.RESUMED
            startCountDownTimer()
        }
    }

    /**
     * method to set circular progress bar values
     */
    private fun setProgressBarValues() {
        binding.progressBarCircle.max = (timeCountInMilliSeconds / 1000).toInt()
        binding.progressBarCircle.progress =
            (timeCountInMilliSeconds / 1000).toInt()
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