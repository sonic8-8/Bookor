package com.smhrd.bookor.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.smhrd.bookor.R
import java.util.*

class AlarmmainActivity : AppCompatActivity() {

    private val REQUEST_CODE_EXACT_ALARM_PERMISSION = 1234

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarmmain)  // Correct layout file

        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val setAlarmButton = findViewById<Button>(R.id.setAlarmButton)

        setAlarmButton?.setOnClickListener {  // Safe call to handle possible null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!canScheduleExactAlarms()) {
                    requestExactAlarmPermission()
                    return@setOnClickListener
                }
            }

            setAlarm(timePicker)
        }
    }

    private fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
        }
    }

    private fun setAlarm(timePicker: TimePicker) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, timePicker.hour)
            set(Calendar.MINUTE, timePicker.minute)
            set(Calendar.SECOND, 0)
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}
