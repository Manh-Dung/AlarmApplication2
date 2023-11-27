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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarmapplication2.adapter.AlarmAdapter
import com.example.alarmapplication2.databinding.FragmentAlarmBinding
import com.example.alarmapplication2.domain.Alarm
import com.example.alarmapplication2.receiver.AlarmReceiver
import com.example.alarmapplication2.receiver.Constants
import com.example.alarmapplication2.viewmodel.ActFragViewModel
import com.example.alarmapplication2.viewmodel.AlarmViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class AlarmFragment : Fragment() {
    // Binding for the fragment's view
    private var _binding: FragmentAlarmBinding? = null
    private val binding
        get() = _binding!!

    // Components for setting up alarms
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    // ViewModel for managing alarms
    private val alarmViewModel: AlarmViewModel by lazy {
        ViewModelProvider(requireActivity())[AlarmViewModel::class.java]
    }

    // ViewModel for managing the activity and fragment states
    private val actFragViewModel: ActFragViewModel by lazy {
        ViewModelProvider(requireActivity())[ActFragViewModel::class.java]
    }

    // Inflates the fragment's view and initializes the alarm list
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)

        binding.addAlarmBtn.visibility = View.VISIBLE
        alarmLoad(alarmInit())


        return binding.root
    }

    // Sets up the button click listeners and observes the delete layout state
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addAlarmBtn.setOnClickListener {
                showTimePicker()
            }
        }

        actFragViewModel.deleteLayoutOn.observe(requireActivity()) {
            if (it) {
                binding.addAlarmBtn.visibility = View.GONE
                binding.bottomDelete.visibility = View.VISIBLE
            } else {
                binding.addAlarmBtn.visibility = View.VISIBLE
                binding.bottomDelete.visibility = View.GONE

                alarmViewModel.setDeleteCheckAll(false)
            }
        }
    }

    /**
     * Initializes the alarm adapter and sets up the RecyclerView for displaying alarms.
     * The adapter is configured with several listeners for item click, long click, switch check change, and checkbox check change events.
     * These listeners handle updating the alarm, deleting the alarm, enabling/disabling the alarm, and checking/unchecking the alarm for deletion, respectively.
     * The RecyclerView is set with a LinearLayoutManager for vertical scrolling.
     * @return The initialized AlarmAdapter.
     */
    private fun alarmInit(): AlarmAdapter {
        val adapter = AlarmAdapter(
            onItemClickLister = { alarm -> updateAlarm(alarm) },
            onItemLongClickListener = { alarm ->
                actFragViewModel.setDeleteLayoutOn(true)
                deleteAlarm(alarm)
            },
            onSwitchCheckedChangeListener = { alarm, isChecked ->
                alarm.isEnable = isChecked
                alarmViewModel.updateAlarm(alarm)
                if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    setAlarm(alarm)
                } else {
                    cancelAlarm(alarm)
                }
            },
            onCheckBoxCheckedChangeListener = { alarm, isChecked ->
                alarm.deleteCheck = isChecked
                alarmViewModel.updateAlarm(alarm)
            },
            actFragViewModel,
            alarmViewModel,
            requireActivity()
        )

        binding.recyclerViewAlarm.apply {
            setHasFixedSize(true)
            this.adapter = adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        return adapter
    }

    /**
     * Updates the specified alarm with a new time selected from a MaterialTimePicker dialog.
     * The time picker is pre-filled with the current time of the alarm.
     * When the user confirms the new time, the alarm is updated in the ViewModel.
     * @param alarm The alarm to be updated.
     */
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

    /**
     * Prepares the specified alarm for deletion.
     * This function makes the delete button visible, and sets up a click listener on it.
     * When the delete button is clicked, the alarm is deleted from the ViewModel, and the UI is reset.
     * @param alarm The alarm to be deleted.
     */
    private fun deleteAlarm(alarm: Alarm) {
        binding.addAlarmBtn.visibility = View.GONE
        binding.bottomDelete.visibility = View.VISIBLE

        actFragViewModel.checkAll.observe(requireActivity()) {
            if (it) {
                binding.deleteBtn.setOnClickListener {
                    alarmViewModel.deleteAlarm(true)

                    actFragViewModel.setCheckAll(false)

                    binding.addAlarmBtn.visibility = View.VISIBLE
                    binding.bottomDelete.visibility = View.GONE

                    actFragViewModel.setDeleteLayoutOn(false)
                }
            } else {
                binding.deleteBtn.setOnClickListener {
                    alarmViewModel.deleteAlarm(alarm)

                    actFragViewModel.setCheckAll(false)

                    binding.addAlarmBtn.visibility = View.VISIBLE
                    binding.bottomDelete.visibility = View.GONE

                    actFragViewModel.setDeleteLayoutOn(false)
                }
            }
        }
    }
//    binding.deleteBtn.setOnClickListener
//    {
//        if (actFragViewModel.checkAll.value != null && actFragViewModel.checkAll.value = true) {
//            alarmViewModel.deleteAlarm(true)
//        } else {
//            alarmViewModel.deleteAlarm(alarm)
//        }
//
//
//    }
//}

    /**
     * Observes and loads all alarms from the database into the provided adapter.
     */
    private fun alarmLoad(adapter: AlarmAdapter) {
        alarmViewModel.getAllAlarms.observe(requireActivity()) {
            adapter.updateData(it)
        }
    }

    /**
     * Presents a time picker dialog to the user. Upon confirmation, a new alarm is created with the selected time and inserted into the ViewModel.
     */
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
                isEnable = true,
                deleteCheck = false
            )

            alarmViewModel.insertAlarm(alarm)
            Toast.makeText(requireContext(), "Alarm set successfully", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Sets an exact alarm at the time specified in the provided alarm object. If the device does not support exact alarms, the user is prompted to grant the necessary permission.
     */
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
        intent.putExtra("alarm_id", alarm.id)

        pendingIntent =
            PendingIntent.getBroadcast(
                requireActivity(),
                alarm.id!!.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        if (!alarmManager.canScheduleExactAlarms()) {
            val intentTmp = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intentTmp.data = Uri.parse("package:com.example.alarmapplication2")
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
        }
    }

    /**
     * Cancels the alarm specified by the provided alarm object.
     */
    private fun cancelAlarm(alarm: Alarm) {
        alarmManager =
            requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity(), AlarmReceiver::class.java)

        pendingIntent =
            PendingIntent.getBroadcast(
                requireActivity(),
                alarm.id!!.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}