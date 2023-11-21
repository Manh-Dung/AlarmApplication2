package com.example.alarmapplication2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapplication2.R
import com.example.alarmapplication2.domain.Alarm
import com.example.alarmapplication2.domain.StopClock

class StopClockAdapter : RecyclerView.Adapter<StopClockAdapter.StopClockViewHolder>() {
    private var stopClockList = emptyList<StopClock>()

    class StopClockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stopClockLayout: LinearLayout = itemView.findViewById(R.id.stopClockLayout)
        val stopClockIdTxt: TextView = itemView.findViewById(R.id.stopClockIdTxt)
        val preTimeTxt: TextView = itemView.findViewById(R.id.preTimeTxt)
        val stopTimeTxt: TextView = itemView.findViewById(R.id.stopTimeTxt)
    }

    fun updateData(newItemList: List<StopClock>) {
        stopClockList = newItemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StopClockAdapter.StopClockViewHolder {
        return StopClockAdapter.StopClockViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.stop_clock_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return stopClockList.size
    }

    override fun onBindViewHolder(holder: StopClockViewHolder, position: Int) {
        val stopClock = stopClockList[position]
        holder.stopClockIdTxt.text = stopClock.id.toString()
        holder.preTimeTxt.text = stopClock.preTime
        holder.stopTimeTxt.text = stopClock.time
    }
}