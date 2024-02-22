package com.example.alarmapplication2.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.alarmapplication2.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ClockFragment : Fragment() {
    // Handler and Runnable for updating time
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_clock, container, false)
        val timeTxt = view.findViewById<TextView>(R.id.timeTxt)
        val dateTxt = view.findViewById<TextView>(R.id.dateTxt)

        // Initialize Handler
        handler = Handler(Looper.getMainLooper())

        // Runnable to update time and date every second
        runnable = Runnable {
            updateTimeAndDate(timeTxt, dateTxt)
            handler.postDelayed(runnable, 1000)
        }

        // Start the Runnable immediately
        handler.postDelayed(runnable, 0)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the Runnable when the Fragment is destroyed
        handler.removeCallbacks(runnable)
    }

    /**
     * Updates the time and date TextViews
     */
    private fun updateTimeAndDate(timeTxt: TextView, dateTxt: TextView) {
        timeTxt.text = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        dateTxt.text = "Hiện tại: " + SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }
}