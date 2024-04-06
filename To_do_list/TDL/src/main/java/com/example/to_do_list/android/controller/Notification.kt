package com.example.to_do_list.android.controller

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.to_do_list.android.R
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.TimeUnit


class CreateurDeNotifications (private val applicationContext : Context){
    private val channel = "channel"

    init {
        creerUneChannelDeNotifications()
    }

    private fun creerUneChannelDeNotifications (){
        val nomChannel = "Notification tâche"
        val descriptionChannel = "Notification pour une tâche"
        val importanceChannel = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel (channel, nomChannel, importanceChannel).apply {
            description = descriptionChannel
        }
        val notificationManager : NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun creerUneNotification (nomTache : String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, channel)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(nomTache)
            .setContentText("se termine dans 24 heures !")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }
}

class AfficheurDeNotifications : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(applicationContext: Context, intent: Intent) {
        val nomTache = intent.getStringExtra("nomTache") ?: "Une tache"
        val createurDeNotifications = CreateurDeNotifications(applicationContext)
        val notification = createurDeNotifications.creerUneNotification(nomTache)
        with(applicationContext.let { NotificationManagerCompat.from(it) }){
            this.notify(0, notification.build())
        }
    }
}

@SuppressLint("ScheduleExactAlarm")
@RequiresApi(Build.VERSION_CODES.S)
fun plannifierNotification(nomTache: String, dateTache: String, applicationContext: Context) {
    val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(applicationContext, AfficheurDeNotifications::class.java).apply {
        putExtra("nomTache", nomTache)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        nomTache.hashCode(),
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val jour = dateTache[0] + dateTache[1].toString()
    val mois = dateTache[2] + dateTache[3].toString()
    val annee = dateTache[4] + dateTache[5].toString() + dateTache[6].toString() + dateTache[7].toString()
    var date = LocalDate.of(annee.toInt(), mois.toInt(), jour.toInt())

    val timeZone = ZoneId.systemDefault()

    val jourDeLaNotification = date.atStartOfDay(timeZone).toEpochSecond().minus(TimeUnit.HOURS.toSeconds(24))

    val jourDeLaNotificationEnMillis = TimeUnit.MILLISECONDS.convert(jourDeLaNotification, TimeUnit.SECONDS)

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        jourDeLaNotificationEnMillis,
        pendingIntent
    )
}
