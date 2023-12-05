package com.example.alarmapplication2.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
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
import com.example.alarmapplication2.receiver.Constants
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
    private var alarmList = mutableListOf<Alarm>()
    private var selectedAlarms = mutableListOf<Alarm>()
    private var isSelected = false
    private var allSelected = false

    private var isAllChecked = false

    @SuppressLint("NotifyDataSetChanged")
    fun checkAll() {
        // Đảo ngược giá trị của biến isAllChecked
        isAllChecked = !isAllChecked
        // Gán giá trị của biến isAllChecked cho item.isChecked của mỗi item
        for (alarm in alarmList) {
            alarm.isChecked = isAllChecked
        }
        notifyDataSetChanged()
    }

    // ViewHolder chứa các view cần thiết
    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmTimeTxt: TextView = itemView.findViewById(R.id.alarmTimeTxt)
        val repeatTxt: TextView = itemView.findViewById(R.id.repeatTxt)
        val enableAlarmBtn: SwitchCompat = itemView.findViewById(R.id.enableAlarmBtn)
        val alarmLayout: CardView = itemView.findViewById(R.id.alarmLayout)
        val checkDeleteBtn: CheckBox = itemView.findViewById(R.id.checkDeleteBtn)
    }

    // Khởi tạo và quan sát các biến LiveData
    init {
        actFragViewModel.deleteLayoutOn.observe(lifecycleOwner) { Handler(Looper.getMainLooper()).post { notifyDataSetChanged() } }
        actFragViewModel.checkAll.observe(lifecycleOwner) { Handler(Looper.getMainLooper()).post { notifyDataSetChanged() } }
    }

    // Cập nhật dữ liệu
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItemList: MutableList<Alarm>) {
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
        holder.checkDeleteBtn.isChecked = alarm.isChecked

        setupViews(holder)
        setupListeners(holder, alarm)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

//    private fun getCheckDeleteAlarm(): Int {
//        var countDelete = 0
//        for (alarm in alarmList) {
//            if (alarm.isChecked) {
//                countDelete++
//            }
//        }

//        return countDelete
//    }

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
        }

        if (actFragViewModel.deleteLayoutOn.value != true) {
            holder.alarmLayout.setOnClickListener {
                onItemClickLister(alarm)
            }

            selectedAlarms.clear()
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

            isSelected = true
            if (selectedAlarms.contains(alarm)) {
                selectedAlarms.remove(alarm)
                holder.checkDeleteBtn.isChecked = false
            } else {
                selectedAlarms.add(alarm)
                holder.checkDeleteBtn.isChecked = true
            }

            if (selectedAlarms.size == 0) isSelected = false

            onCheckBoxCheckedChangeListener(alarm, isChecked)
            holder.checkDeleteBtn.post { notifyDataSetChanged() }
        }

//        holder.checkDeleteBtn.setOnClickListener {
//            alarm.isChecked = holder.checkDeleteBtn.isChecked
//            alarmViewModel.updateAlarm(alarm)
//
//            isAllChecked = alarmList.all { it.isChecked }
//        }




//        if (actFragViewModel.checkAll.value == true) {
//            holder.setCheckDeleteBtn(true)
//            actFragViewModel.setCheckAll(false)
//        } else if (actFragViewModel.checkAll.value == false && getCheckDeleteAlarm() != itemCount) {
//            holder.setCheckDeleteBtn(true)
//            actFragViewModel.setCheckAll(false)
//        } else if (actFragViewModel.checkAll.value == false && getCheckDeleteAlarm() == itemCount) {
//
//        }

        // su kien xay ra khi nut check box thay doi
//        holder.checkDeleteBtn.setOnCheckedChangeListener { _, _ ->
//            isSelected = true
//            if (selectedAlarms.contains(alarm)) {
//                selectedAlarms.remove(alarm)
//                holder.checkDeleteBtn.isChecked = false
//            } else {
//                selectedAlarms.add(alarm)
//                holder.checkDeleteBtn.isChecked = true
//            }
//
//            if (selectedAlarms.size == 0) isSelected = false
//
//            actFragViewModel.setCheckAll(selectedAlarms.size)
//            onCheckBoxCheckedChangeListener(selectedAlarms)
//        }
//
//        if (actFragViewModel.checkAll.value == Constants.CHECK_DELETE_ON_CLICK) {
//            val alarmSet = alarmList.toSet()
//            val selectedAlarmSet = selectedAlarms.toMutableSet()
//
//            if (alarmSet == selectedAlarmSet) {
//                selectedAlarms.clear()
//            } else {
//                selectedAlarmSet.addAll(alarmList)
//                selectedAlarms = selectedAlarmSet.toMutableList()
//            }
//
//            actFragViewModel.setCheckAll(selectedAlarms.size)
//            onCheckBoxCheckedChangeListener(selectedAlarms)
//        }

        if (actFragViewModel.checkAll.value == Constants.CHECK_DELETE_ON_CLICK) {
            val alarmSet = alarmList.toSet()
            val selectedAlarmSet = selectedAlarms.toMutableSet()

            if (alarmSet == selectedAlarmSet) {
                selectedAlarms.clear()
            } else {
                selectedAlarmSet.addAll(alarmList)
                selectedAlarms = selectedAlarmSet.toMutableList()
            }

            actFragViewModel.setCheckAll(selectedAlarms.size)
            onCheckBoxCheckedChangeListener(selectedAlarms)
        }
    }
}
