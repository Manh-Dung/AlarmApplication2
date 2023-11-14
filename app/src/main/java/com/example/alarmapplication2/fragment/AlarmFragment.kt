package com.example.alarmapplication2.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarmapplication2.adapter.AlarmAdapter
import com.example.alarmapplication2.databinding.FragmentAlarmBinding
import com.example.alarmapplication2.domain.Alarm
import com.example.alarmapplication2.receiver.AlarmReceiver
import com.example.alarmapplication2.receiver.Constants
import com.example.alarmapplication2.viewmodel.AlarmViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class AlarmFragment : Fragment() {
    interface OnButtonPressListener {
        fun onButtonPressed(msg: String)
        fun onActivityCommand(deleteCheck: Boolean)
    }
    private var onButtonPressListener: OnButtonPressListener? = null

    private var _binding: FragmentAlarmBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    private val alarmViewModel: AlarmViewModel by lazy {
        ViewModelProvider(requireActivity())[AlarmViewModel::class.java]
    }

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

        binding.addAlarmBtn.visibility = View.VISIBLE
        alarmLoad(alarmInit())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addAlarmBtn.setOnClickListener {
                showTimePicker()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnButtonPressListener) {
            onButtonPressListener = context
        } else {
            throw ClassCastException("$context must implement OnButtonPressListener")
        }
    }

    private fun alarmInit(): AlarmAdapter {
        val adapter = AlarmAdapter(
            onItemClickLister = { alarm ->
                updateAlarm(alarm)
            },
            onItemLongClickListener = { alarm ->
                deleteAlarm(alarm)
            },
            onItemCheckedChangeListener = { alarm ->
                if (alarm.isEnable) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        setAlarm(alarm)
                    }
                } else {
                    cancelAlarm()
                }
            }
        )

        binding.recyclerViewAlarm.setHasFixedSize(true)
        binding.recyclerViewAlarm.adapter = adapter
        binding.recyclerViewAlarm.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        return adapter
    }

    private fun updateAlarm(alarm: Alarm) {
        val tmp = alarm.time.split(":")

        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(tmp[0].toInt())
            .setMinute(tmp[1].toInt())
            .setTitleText(alarm.time)
            .build()

        picker.addOnPositiveButtonClickListener {
            alarm.time = String.format("%02d", picker.hour) + ":" + String.format(
                "%02d", picker.minute
            )

            alarmViewModel.updateAlarm(alarm)
        }

        picker.show(childFragmentManager, Constants.NOTIFICATION_CHANNEL_ID)
    }

    private fun deleteAlarm(alarm: Alarm) {
        binding.addAlarmBtn.visibility = View.GONE
        binding.bottomDelete.visibility = View.VISIBLE

        binding.deleteBtn.setOnClickListener {
            alarmViewModel.deleteAlarm(alarm)

            binding.addAlarmBtn.visibility = View.VISIBLE
            binding.bottomDelete.visibility = View.GONE
        }
    }

    private fun alarmLoad(adapter: AlarmAdapter) {
        alarmViewModel.getAllAlarms.observe(requireActivity()) {
            adapter.updateData(it)
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
            val pickerTime = String.format("%02d", picker.hour) + ":" + String.format(
                "%02d", picker.minute
            )

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

            val alarm = Alarm(
                null,
                pickerTime,
                true,
                false
            )

            alarmViewModel.insertAlarm(alarm)
            Toast.makeText(requireContext(), "Alarm set successfully", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setAlarm(alarm: Alarm) {
        val tmp = alarm.time.split(":")

        calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = tmp[0].toInt()
        calendar[Calendar.MINUTE] = tmp[1].toInt()
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

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