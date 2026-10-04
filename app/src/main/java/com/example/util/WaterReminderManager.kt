package com.example.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import java.util.Calendar

class WaterReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences(WaterReminderManager.PREFS_NAME, Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean(WaterReminderManager.KEY_REMINDERS_ENABLED, false)
        if (!isEnabled) return

        showWaterNotification(context)
    }

    private fun showWaterNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "water_reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Vízfogyasztás emlékeztető",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Értesítések az optimális napi folyadékbevitel eléréséhez"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val tips = listOf(
            "Igyál meg egy pohár (2.5 dl) friss vizet a megfelelő hidratáltságért! 💧",
            "A rendszeres vízfogyasztás pörgeti az anyagcserét és segít a fogyásban! 🥤",
            "Tarts egy kis szünetet és igyál egy pohár vizet! A szervezeted meghálálja. ✨",
            "Ne várd meg míg megszomjazol – hidratálj folyamatosan a nap folyamán! 🌊"
        )
        val selectedTip = tips.random()

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("💧 Ideje inni egy pohár vizet!")
            .setContentText(selectedTip)
            .setStyle(NotificationCompat.BigTextStyle().bigText(selectedTip))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}

object WaterReminderManager {
    const val PREFS_NAME = "water_reminder_prefs"
    const val KEY_REMINDERS_ENABLED = "reminders_enabled"
    const val KEY_INTERVAL_HOURS = "interval_hours"
    const val KEY_START_HOUR = "start_hour"
    const val KEY_END_HOUR = "end_hour"

    fun isReminderEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_REMINDERS_ENABLED, false)
    }

    fun getIntervalHours(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_INTERVAL_HOURS, 2)
    }

    fun getStartHour(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_START_HOUR, 8)
    }

    fun getEndHour(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_END_HOUR, 21)
    }

    fun saveSettings(
        context: Context,
        enabled: Boolean,
        intervalHours: Int,
        startHour: Int,
        endHour: Int
    ) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_REMINDERS_ENABLED, enabled)
            .putInt(KEY_INTERVAL_HOURS, intervalHours)
            .putInt(KEY_START_HOUR, startHour)
            .putInt(KEY_END_HOUR, endHour)
            .apply()

        if (enabled) {
            scheduleReminders(context, intervalHours, startHour, endHour)
        } else {
            cancelReminders(context)
        }
    }

    fun scheduleReminders(
        context: Context,
        intervalHours: Int,
        startHour: Int,
        endHour: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, WaterReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            2001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val intervalMillis = intervalHours * 60 * 60 * 1000L

        val calendar = Calendar.getInstance().apply {
            val currentHour = get(Calendar.HOUR_OF_DAY)
            if (currentHour < startHour) {
                set(Calendar.HOUR_OF_DAY, startHour)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            } else if (currentHour >= endHour) {
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, startHour)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            } else {
                add(Calendar.HOUR_OF_DAY, intervalHours)
            }
        }

        try {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                intervalMillis,
                pendingIntent
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelReminders(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, WaterReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            2001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        try {
            alarmManager.cancel(pendingIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun triggerTestNotification(context: Context) {
        val receiver = WaterReminderReceiver()
        receiver.onReceive(context, Intent())
    }
}
