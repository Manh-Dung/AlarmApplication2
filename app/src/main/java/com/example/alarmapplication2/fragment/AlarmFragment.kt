package com.example.alarmapplication2.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.alarmapplication2.databinding.FragmentAlarmBinding
import com.example.alarmapplication2.receiver.AlarmReceiver
import com.example.alarmapplication2.receiver.Constants
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class AlarmFragment : Fragment() {
    private var _binding: FragmentAlarmBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    companion object {
        @JvmStatic
        fun newInstance() =
            AlarmFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectTimeBtn.setOnClickListener {
            showTimePicker()
        }

        binding.setTimeBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                setAlarm()
            }
        }

        binding.cancelTimeBtn.setOnClickListener {
            cancelAlarm()
        }
    }

    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()

        picker.show(childFragmentManager, Constants.NOTIFICATION_CHANNEL_ID)

        picker.addOnPositiveButtonClickListener {
            if (picker.hour >= 12) {
                binding.selectedTime.text =
                    String.format("%02d", picker.hour - 12) + " : " + String.format(
                        "%02d", picker.minute
                    ) + " PM"
            } else {
                if (picker.hour < 12) {
                    binding.selectedTime.text =
                        String.format("%02d", picker.hour) + " : " + String.format(
                            "%02d", picker.minute
                        ) + " AM"
                }
            }

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setAlarm() {
        alarmManager =
            requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity(), AlarmReceiver::class.java)

        pendingIntent =
            PendingIntent.getBroadcast(
                requireActivity(),
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        if (!alarmManager.canScheduleExactAlarms()) {
            val intentTmp = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intentTmp.data = Uri.parse("package:com.android.application")
            startActivity(intentTmp)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                    pendingIntent
                )
            }

            Toast.makeText(requireContext(), "Alarm set Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelAlarm() {
        alarmManager =
            requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity(), AlarmReceiver::class.java)

        pendingIntent =
            PendingIntent.getBroadcast(
                requireActivity(),
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        alarmManager.cancel(pendingIntent)
        Toast.makeText(requireContext(), "Alarm cancelled", Toast.LENGTH_LONG).show()
    }

}