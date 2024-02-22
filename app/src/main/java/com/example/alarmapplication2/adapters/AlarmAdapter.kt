package com.example.alarmapplication2.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapplication2.R
import com.example.alarmapplication2.models.Alarm
import com.example.alarmapplication2.viewmodels.AlarmViewModel
import com.example.alarmapplication2.viewmodels.AppViewModel

class AlarmAdapter(
    private val onItemClickLister: (Alarm) -> Unit,
    private val onItemLongClickListener: (Alarm) -> Unit,
    private val onSwitchCheckedChangeListener: (Alarm, Boolean) -> Unit,
    private val appViewModel: AppViewModel,
    private val alarmViewModel: AlarmViewModel,
    private val lifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    private var alarmList = mutableListOf<Alarm>()
    private var selectedAlarms = mutableListOf<Alarm>()

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmTimeTxt: TextView = itemView.findViewById(R.id.alarmTimeTxt)
        val repeatTxt: TextView = itemView.findViewById(R.id.repeatTxt)
        val enableAlarmBtn: SwitchCompat =
            itemView.findViewById(R.id.enableAlarmBtn)
        val alarmLayout: CardView = itemView.findViewById(R.id.alarmLayout)
        val checkDeleteBtn: CheckBox =
            itemView.findViewById(R.id.checkDeleteBtn)
    }

    init {
        appViewModel.deleteLayoutOn.observe(lifecycleOwner) { notifyDataSetChanged() }
        appViewModel.checkAll.observe(lifecycleOwner) { notifyDataSetChanged() }
    }

    fun updateData(newItemList: MutableList<Alarm>) {
        alarmList = newItemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AlarmViewHolder {
        return AlarmViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.alarm_row, parent, false)
        )
    }

    /**
     * Method to bind the view holder with the data.
     * @param holder The AlarmViewHolder contains the components.
     * @param position The position of the item in the list.
     * @return The view holder with the data.
     */
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarmList[position]
        holder.alarmTimeTxt.text = alarm.time
        holder.enableAlarmBtn.isChecked = alarm.isEnable
        holder.checkDeleteBtn.isChecked = alarm.isChecked

        setupViews(holder, alarm)
        setupListeners(holder, alarm)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    private fun setupViews(holder: AlarmViewHolder, alarm: Alarm) {
        appViewModel.deleteLayoutOn.observe(lifecycleOwner) {
            if (it) {
                holder.checkDeleteBtn.visibility = View.VISIBLE
                holder.enableAlarmBtn.visibility = View.GONE

                holder.alarmLayout.isEnabled = false
            } else if (!it) {
                holder.checkDeleteBtn.visibility = View.GONE
                holder.enableAlarmBtn.visibility = View.VISIBLE

                    holder.alarmLayout.isEnabled = true
            }
        }

        appViewModel.checkAll.observe(lifecycleOwner) {
            if (it) {
                alarm.isChecked = true
                alarmViewModel.updateAlarm(alarm)
                holder.checkDeleteBtn.post { notifyDataSetChanged() }
            } else {
                alarm.isChecked = false
                alarmViewModel.updateAlarm(alarm)
                holder.checkDeleteBtn.post { notifyDataSetChanged() }
            }
        }
    }

    /**
     * Method to set up listener for enable alarm button, alarm, long click alarm, check box for deletion
     * @param holder The AlarmViewHolder contains the components.
     * @param alarm The selected alarm, or clicked.
     */
    private fun setupListeners(holder: AlarmViewHolder, alarm: Alarm) {
        holder.enableAlarmBtn.setOnClickListener {
            alarm.isEnable = !alarm.isEnable
            alarmViewModel.updateAlarm(alarm)
            holder.enableAlarmBtn.post { notifyDataSetChanged() }
        }

        if (appViewModel.deleteLayoutOn.value != true) {
            holder.alarmLayout.setOnClickListener {
                onItemClickLister(alarm)
            }
        }

        holder.alarmLayout.setOnLongClickListener {
            holder.checkDeleteBtn.visibility = View.VISIBLE
            holder.enableAlarmBtn.visibility = View.GONE

            appViewModel.setDeleteLayoutOn(true)

            onItemLongClickListener(alarm)
            holder.alarmLayout.post { notifyDataSetChanged() }
        }

        holder.checkDeleteBtn.setOnClickListener {
            alarm.isChecked = !alarm.isChecked
            alarmViewModel.updateAlarm(alarm)

            appViewModel.setCountCheckedAlarms(
                alarmViewModel
                    .countCheckedAlarms.value ?: 0
            )
            holder.checkDeleteBtn.post { notifyDataSetChanged() }
        }

    }
}
