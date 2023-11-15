package com.example.alarmapplication2.adapter

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

class AlarmAdapter(
    private val onItemClickLister: (Alarm) -> Unit,
    private val onItemLongClickListener: (Alarm) -> Unit,
    private val onItemCheckedChangeListener: (Alarm) -> Unit,
    private val model: ActFragViewModel,
    private val lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {
    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmTimeTxt: TextView = itemView.findViewById(R.id.alarmTimeTxt)
        val repeatTxt: TextView = itemView.findViewById(R.id.repeatTxt)
        val enableAlarmBtn: SwitchCompat = itemView.findViewById(R.id.enableAlarmBtn)
        val alarmLayout: CardView = itemView.findViewById(R.id.alarmLayout)
        val checkDeleteBtn: CheckBox = itemView.findViewById(R.id.checkDeleteBtn)
    }

    init {
        model.deleteLayoutOn.observe(lifecycleOwner) {
            notifyDataSetChanged()
        }

        model.checkAll.observe(lifecycleOwner) {
            notifyDataSetChanged()
        }
    }

    private var alarmList = emptyList<Alarm>()

    fun updateData(newItemList: List<Alarm>) {
        alarmList = newItemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlarmViewHolder {
        return AlarmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.alarm_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        if (model.deleteLayoutOn.value == true) {
            holder.checkDeleteBtn.visibility = View.VISIBLE
            holder.enableAlarmBtn.visibility = View.GONE
        } else {
            holder.checkDeleteBtn.visibility = View.GONE
            holder.enableAlarmBtn.visibility = View.VISIBLE
        }

        holder.checkDeleteBtn.isChecked = model.checkAll.value == true

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

        holder.checkDeleteBtn.setOnCheckedChangeListener { _, _ ->
            alarm.deleteCheck = holder.checkDeleteBtn.isChecked
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }
}