package com.sasfmlzr.findfilm.service

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.sasfmlzr.findfilm.MainActivity
import com.sasfmlzr.findfilm.R
import java.util.concurrent.TimeUnit

class NotificationService : IntentService("Notify") {
    private var nm: NotificationManager? = null
    override fun onCreate() {
        super.onCreate()
        nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onHandleIntent(intent: Intent?) {
        try {
            TimeUnit.SECONDS.sleep(60)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        sendNotification()
    }

    private fun sendNotification() {
        val icon = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.ic_app
        )
        val notifyBuilder = NotificationCompat.Builder(applicationContext, "notify_001")
        val ii = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, ii, 0)
        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText(LOW_TEXT)
        bigText.setBigContentTitle(BIG_TEXT)
        notifyBuilder
            .setContentIntent(pendingIntent)
            .setContentTitle(BIG_TEXT)
            .setContentText(LOW_TEXT)
            .setStyle(bigText)
            .setPriority(Notification.PRIORITY_MAX)
            .setSmallIcon(R.drawable.ic_app)
            .setLargeIcon(icon)
        val notification = notifyBuilder.build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "notify_001",
                BIG_TEXT,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            nm!!.createNotificationChannel(channel)
        }
        nm!!.notify(0, notification)
    }

    companion object {
        private const val BIG_TEXT = "Looking film!"
        private const val LOW_TEXT = "More, more film!"
    }
}