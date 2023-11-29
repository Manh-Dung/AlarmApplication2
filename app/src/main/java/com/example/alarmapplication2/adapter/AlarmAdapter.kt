package com.example.alarmapplication2.adapter

import android.util.Log
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
import com.example.alarmapplication2.domain.Alarm
import com.example.alarmapplication2.viewmodel.ActFragViewModel
import com.example.alarmapplication2.viewmodel.AlarmViewModel

class AlarmAdapter(
    private val onItemClickLister: (Alarm) -> Unit,
    private val onItemLongClickListener: (Alarm) -> Unit,
    private val onSwitchCheckedChangeListener: (Alarm, Boolean) -> Unit,
    private val onCheckBoxCheckedChangeListener: (Alarm, Boolean) -> Unit,
    private val actFragViewModel: ActFragViewModel,
    private val alarmViewModel: AlarmViewModel,
    lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    // Khởi tạo danh sách rỗng
    private var alarmList = emptyList<Alarm>()

    // ViewHolder chứa các view cần thiết
    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmTimeTxt: TextView = itemView.findViewById(R.id.alarmTimeTxt)
        val repeatTxt: TextView = itemView.findViewById(R.id.repeatTxt)
        val enableAlarmBtn: SwitchCompat = itemView.findViewById(R.id.enableAlarmBtn)
        val alarmLayout: CardView = itemView.findViewById(R.id.alarmLayout)
        val checkDeleteBtn: CheckBox = itemView.findViewById(R.id.checkDeleteBtn)

        fun setCheckDeleteBtn(isChecked: Boolean) {
            checkDeleteBtn.isChecked = isChecked
        }
    }

    // Khởi tạo và quan sát các biến LiveData
    init {
        actFragViewModel.deleteLayoutOn.observe(lifecycleOwner) { notifyDataSetChanged() }
    }

    // Cập nhật dữ liệu
    fun updateData(newItemList: List<Alarm>) {
        alarmList = newItemList
        notifyDataSetChanged()
    }

    // Tạo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.alarm_row, parent, false)
        )
    }

    // Gán dữ liệu cho các view trong ViewHolder
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarmList[position]
        holder.alarmTimeTxt.text = alarm.time
        holder.enableAlarmBtn.isChecked = alarm.isEnable
        holder.checkDeleteBtn.isChecked = alarm.deleteCheck

        setupViews(holder)
        setupListeners(holder, alarm)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    private fun getCheckDeleteAlarm(): Int {
        var countDelete = 0
        for (alarm in alarmList) {
            if (alarm.deleteCheck) {
                countDelete++
            }
        }

        return countDelete
    }

    // Set up views
    private fun setupViews(holder: AlarmViewHolder) {
        if (actFragViewModel.deleteLayoutOn.value == true) {
            holder.checkDeleteBtn.visibility = View.VISIBLE
            holder.enableAlarmBtn.visibility = View.GONE
        } else {
            holder.checkDeleteBtn.visibility = View.GONE
            holder.enableAlarmBtn.visibility = View.VISIBLE
        }


    }

    /**
     * Method to set up listener for enable alarm button, alarm, long click alarm, check box for deletion
     * @param holder The AlarmViewHolder contains the components.
     * @param alarm The selected alarm, or clicked.
     */
    private fun setupListeners(holder: AlarmViewHolder, alarm: Alarm) {
        holder.enableAlarmBtn.setOnCheckedChangeListener { _, isChecked ->
            onSwitchCheckedChangeListener(alarm, isChecked)
            holder.enableAlarmBtn.post { notifyDataSetChanged() }
        }

        if (actFragViewModel.deleteLayoutOn.value != true) {
            holder.alarmLayout.setOnClickListener {
                onItemClickLister(alarm)
            }
        }

        holder.alarmLayout.setOnLongClickListener {
            holder.checkDeleteBtn.visibility = View.VISIBLE
            holder.enableAlarmBtn.visibility = View.GONE

            onItemLongClickListener(alarm)
            holder.alarmLayout.post { notifyDataSetChanged() }

            true
        }

        // su kien xay ra khi nut check box thay doi
        holder.checkDeleteBtn.setOnCheckedChangeListener { _, isChecked ->

            onCheckBoxCheckedChangeListener(alarm, isChecked)
            holder.checkDeleteBtn.post { notifyDataSetChanged() }
        }

//        if (actFragViewModel.checkAll.value == true) {
//            holder.setCheckDeleteBtn(true)
//            actFragViewModel.setCheckAll(false)
//        } else if (actFragViewModel.checkAll.value == false && getCheckDeleteAlarm() != itemCount) {
//            holder.setCheckDeleteBtn(true)
//            actFragViewModel.setCheckAll(false)
//        } else if (actFragViewModel.checkAll.value == false && getCheckDeleteAlarm() == itemCount) {
//
//        }
    }
}
