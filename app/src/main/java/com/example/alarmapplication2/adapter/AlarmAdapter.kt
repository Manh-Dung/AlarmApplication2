package com.example.alarmapplication2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapplication2.R
import com.example.alarmapplication2.domain.Alarm

class AlarmAdapter(
    private val onItemClickLister: (Alarm) -> Unit,
    private val onItemLongClickListener: (Alarm) -> Unit,
    private val onItemCheckedChangeListener: (Alarm) -> Unit
) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmTimeTxt: TextView = itemView.findViewById(R.id.alarmTimeTxt)
        val repeatTxt: TextView = itemView.findViewById(R.id.repeatTxt)
        val enableAlarmBtn: SwitchCompat = itemView.findViewById(R.id.enableAlarmBtn)
        val alarmLayout: CardView = itemView.findViewById(R.id.alarmLayout)
        val checkDeleteBtn: CheckBox = itemView.findViewById(R.id.checkDeleteBtn)
    }

    private var alarmList = emptyList<Alarm>()

    fun updateData(newItemList: List<Alarm>) {
        alarmList = newItemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlarmAdapter.AlarmViewHolder {
        return AlarmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.alarm_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlarmAdapter.AlarmViewHolder, position: Int) {
        val alarm = alarmList[position]
        holder.alarmTimeTxt.text = alarm.time
        holder.enableAlarmBtn.isChecked = alarm.isEnable
        holder.enableAlarmBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alarm.isEnable = true
                onItemCheckedChangeListener(alarm)
            } else {
                alarm.isEnable = false
                onItemCheckedChangeListener(alarm)
            }
        }

        holder.alarmLayout.setOnClickListener {
            onItemClickLister(alarm)
        }

        holder.alarmLayout.setOnLongClickListener {
            holder.checkDeleteBtn.visibility = View.VISIBLE
            holder.enableAlarmBtn.visibility = View.GONE
            notifyDataSetChanged()
            onItemLongClickListener(alarm)
            true
        }

        alarm.deleteCheck = holder.checkDeleteBtn.isChecked
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }
}