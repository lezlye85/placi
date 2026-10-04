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
import com.example.data.local.SupplementsData
import com.example.data.model.SupplementTiming
import com.example.data.model.UserDailySupplement
import java.util.Calendar
import java.util.Locale

class SupplementReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val timingName = intent.getStringExtra(EXTRA_TIMING_NAME)
        val timing = if (timingName != null) {
            try { SupplementTiming.valueOf(timingName) } catch (e: Exception) { null }
        } else null

        val customTitle = intent.getStringExtra(EXTRA_CUSTOM_TITLE)
        val customBody = intent.getStringExtra(EXTRA_CUSTOM_BODY)

        val prefs = context.getSharedPreferences(SupplementReminderManager.PREFS_NAME, Context.MODE_PRIVATE)
        val isMasterEnabled = prefs.getBoolean(SupplementReminderManager.KEY_MASTER_ENABLED, true)
        if (!isMasterEnabled && timing != null) return

        showSupplementNotification(context, timing, customTitle, customBody)
    }

    private fun showSupplementNotification(
        context: Context,
        timing: SupplementTiming?,
        customTitle: String?,
        customBody: String?
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "supplement_reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Étrend-kiegészítő Emlékeztetők",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Értesítések az étrendkiegészítők megfelelő időpontban történő bevételéhez az adagolási útmutató alapján"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "supplements")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            (timing?.hashCode() ?: 9999),
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = customTitle ?: when (timing) {
            SupplementTiming.MORNING_FASTED -> "🌅 Reggeli Éhgyomri Kiegészítő Emlékeztető"
            SupplementTiming.WITH_BREAKFAST -> "🍳 Reggeli Kiegészítő Emlékeztető"
            SupplementTiming.PRE_WORKOUT -> "⚡ Edzés Előtti Pörgető & Aminosav Emlékeztető"
            SupplementTiming.INTRA_WORKOUT -> "🥤 Edzés Közbeni Frissítő Emlékeztető"
            SupplementTiming.POST_WORKOUT -> "🏋️ Edzés Utáni Fehérje & Kreatin Emlékeztető"
            SupplementTiming.WITH_MEAL -> "🍽️ Főétkezési Kiegészítő Emlékeztető"
            SupplementTiming.BEFORE_BED -> "🌙 Esti Pihentető Kiegészítő Emlékeztető"
            SupplementTiming.DAILY_ANYTIME -> "🕒 Napi Kiegészítő Emlékeztető"
            null -> "💊 Étrend-kiegészítő Emlékeztető"
        }

        val body = customBody ?: getTimingDefaultBody(timing)

        val notificationId = 3000 + (timing?.ordinal ?: 0)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    private fun getTimingDefaultBody(timing: SupplementTiming?): String {
        return when (timing) {
            SupplementTiming.MORNING_FASTED -> "Ideje bevenni a reggeli éhgyomri kiegészítőidet (pl. L-Karnitin, Probiotikum, Meleg citromos víz) bőséges folyadékkal!"
            SupplementTiming.WITH_BREAKFAST -> "Reggeli étkezéssel vedd be a Multivitamint, D3+K2 és Omega-3 zsírsavakat a maximális felszívódásért!"
            SupplementTiming.PRE_WORKOUT -> "20-30 perc múlva kezdődik az edzés! Ideje elfogyasztani az edzés előtti pörgetőt, béta-alanint vagy koffeint."
            SupplementTiming.INTRA_WORKOUT -> "Edzés közben kortyolgasd az EAA/BCAA aminosav és elektrolit italodat a tartós energiaszintért és izomvédelemért!"
            SupplementTiming.POST_WORKOUT -> "Edzés végeztével ideje a tejsavófehérje turmixnak és 5g kreatin-monohidrátnak az azonnali regenerációért!"
            SupplementTiming.WITH_MEAL -> "Főétkezéseddel vedd be az emésztőenzimeket, ízületvédő kollagént vagy krómiumot!"
            SupplementTiming.BEFORE_BED -> "Lefekvés előtt 30-45 perccel vedd be a Magnézium-biszglicinátot, Cinket vagy Ashwagandhát a mély, pihentető alvásért!"
            SupplementTiming.DAILY_ANYTIME -> "Ne feledkezz meg a napi vitaminjaid és ásványi anyagaid pótlásáról!"
            null -> "Ideje bevenni a beállított adagolású étrend-kiegészítőidet az optimális fejlődésért!"
        }
    }

    companion object {
        const val EXTRA_TIMING_NAME = "extra_timing_name"
        const val EXTRA_CUSTOM_TITLE = "extra_custom_title"
        const val EXTRA_CUSTOM_BODY = "extra_custom_body"
    }
}

data class TimingScheduleInfo(
    val timing: SupplementTiming,
    val isEnabled: Boolean,
    val hour: Int,
    val minute: Int,
    val defaultHour: Int,
    val defaultMinute: Int,
    val matchingSupplements: List<UserDailySupplement>
) {
    val formattedTime: String get() = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
}

object SupplementReminderManager {
    const val PREFS_NAME = "supplement_reminder_prefs"
    const val KEY_MASTER_ENABLED = "supplement_reminders_master_enabled"

    // Default hours for timings
    val DEFAULT_HOURS: Map<SupplementTiming, Pair<Int, Int>> = mapOf(
        SupplementTiming.MORNING_FASTED to Pair(7, 0),
        SupplementTiming.WITH_BREAKFAST to Pair(8, 0),
        SupplementTiming.WITH_MEAL to Pair(12, 30),
        SupplementTiming.PRE_WORKOUT to Pair(16, 30),
        SupplementTiming.INTRA_WORKOUT to Pair(17, 15),
        SupplementTiming.POST_WORKOUT to Pair(18, 15),
        SupplementTiming.DAILY_ANYTIME to Pair(14, 0),
        SupplementTiming.BEFORE_BED to Pair(22, 0)
    )

    fun isMasterEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_MASTER_ENABLED, true)
    }

    fun setMasterEnabled(context: Context, enabled: Boolean, stack: List<UserDailySupplement>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_MASTER_ENABLED, enabled).apply()
        if (enabled) {
            scheduleAllReminders(context, stack)
        } else {
            cancelAllReminders(context)
        }
    }

    fun isTimingEnabled(context: Context, timing: SupplementTiming): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("timing_enabled_${timing.name}", true)
    }

    fun setTimingEnabled(context: Context, timing: SupplementTiming, enabled: Boolean, stack: List<UserDailySupplement>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("timing_enabled_${timing.name}", enabled).apply()
        if (isMasterEnabled(context)) {
            scheduleAllReminders(context, stack)
        }
    }

    fun getTimingTime(context: Context, timing: SupplementTiming): Pair<Int, Int> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val defaultTime = DEFAULT_HOURS[timing] ?: Pair(8, 0)
        val hour = prefs.getInt("timing_hour_${timing.name}", defaultTime.first)
        val minute = prefs.getInt("timing_minute_${timing.name}", defaultTime.second)
        return Pair(hour, minute)
    }

    fun setTimingTime(context: Context, timing: SupplementTiming, hour: Int, minute: Int, stack: List<UserDailySupplement>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt("timing_hour_${timing.name}", hour)
            .putInt("timing_minute_${timing.name}", minute)
            .apply()
        if (isMasterEnabled(context)) {
            scheduleAllReminders(context, stack)
        }
    }

    fun getFullSchedule(context: Context, stack: List<UserDailySupplement>): List<TimingScheduleInfo> {
        return SupplementTiming.values().map { timing ->
            val default = DEFAULT_HOURS[timing] ?: Pair(8, 0)
            val time = getTimingTime(context, timing)
            val isEnabled = isTimingEnabled(context, timing)
            val matching = stack.filter { it.timing == timing }
            TimingScheduleInfo(
                timing = timing,
                isEnabled = isEnabled,
                hour = time.first,
                minute = time.second,
                defaultHour = default.first,
                defaultMinute = default.second,
                matchingSupplements = matching
            )
        }
    }

    fun scheduleAllReminders(context: Context, stack: List<UserDailySupplement>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        if (!isMasterEnabled(context)) {
            cancelAllReminders(context)
            return
        }

        SupplementTiming.values().forEach { timing ->
            val isEnabled = isTimingEnabled(context, timing)
            val (hour, minute) = getTimingTime(context, timing)
            val matchingSupps = stack.filter { it.timing == timing }

            val intent = Intent(context, SupplementReminderReceiver::class.java).apply {
                putExtra(SupplementReminderReceiver.EXTRA_TIMING_NAME, timing.name)
                if (matchingSupps.isNotEmpty()) {
                    val suppNamesWithDosage = matchingSupps.joinToString(separator = "\n• ", prefix = "• ") {
                        "${it.brand} ${it.supplementName}: ${it.dosageText}"
                    }
                    val title = "${timing.iconEmoji} ${timing.displayNameHu} (${String.format(Locale.getDefault(), "%02d:%02d", hour, minute)})"
                    val body = "Ideje bevenni a következő kiegészítőket az adagolási útmutató szerint:\n$suppNamesWithDosage"
                    putExtra(SupplementReminderReceiver.EXTRA_CUSTOM_TITLE, title)
                    putExtra(SupplementReminderReceiver.EXTRA_CUSTOM_BODY, body)
                }
            }

            val requestCode = 5000 + timing.ordinal
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (!isEnabled) {
                try {
                    alarmManager.cancel(pendingIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return@forEach
            }

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            val intervalMillis = AlarmManager.INTERVAL_DAY

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
    }

    fun cancelAllReminders(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        SupplementTiming.values().forEach { timing ->
            val intent = Intent(context, SupplementReminderReceiver::class.java)
            val requestCode = 5000 + timing.ordinal
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            try {
                alarmManager.cancel(pendingIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun triggerTestNotification(
        context: Context,
        timing: SupplementTiming? = null,
        stack: List<UserDailySupplement> = emptyList()
    ) {
        val receiver = SupplementReminderReceiver()
        val intent = Intent().apply {
            if (timing != null) {
                putExtra(SupplementReminderReceiver.EXTRA_TIMING_NAME, timing.name)
                val matching = stack.filter { it.timing == timing }
                if (matching.isNotEmpty()) {
                    val suppNamesWithDosage = matching.joinToString(separator = "\n• ", prefix = "• ") {
                        "${it.brand} ${it.supplementName} (${it.dosageText})"
                    }
                    val title = "🔔 ${timing.displayNameHu} - Kiegészítő Emlékeztető"
                    val body = "Adagolási útmutató alapján most esedékes:\n$suppNamesWithDosage\n\n💧 Fogyaszd megfelelő folyadékkal!"
                    putExtra(SupplementReminderReceiver.EXTRA_CUSTOM_TITLE, title)
                    putExtra(SupplementReminderReceiver.EXTRA_CUSTOM_BODY, body)
                }
            } else {
                val title = "🔔 Étrend-kiegészítő Emlékeztető Teszt"
                val body = if (stack.isNotEmpty()) {
                    val sample = stack.take(3).joinToString(separator = "\n• ", prefix = "• ") {
                        "${it.brand} ${it.supplementName}: ${it.dosageText} (${it.timing.displayNameHu})"
                    }
                    "A mai kiegészítőid az adagolási útmutató szerint:\n$sample"
                } else {
                    "Minden beállított kiegészítőd megfelelő időben emlékeztetni fog a bevételre! 💊"
                }
                putExtra(SupplementReminderReceiver.EXTRA_CUSTOM_TITLE, title)
                putExtra(SupplementReminderReceiver.EXTRA_CUSTOM_BODY, body)
            }
        }
        receiver.onReceive(context, intent)
    }
}
