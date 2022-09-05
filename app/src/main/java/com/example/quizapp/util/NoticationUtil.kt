package com.example.quizapp.util
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.quizapp.MainActivity
import com.example.quizapp.R
import com.example.quizapp.screens.game.GameFragment

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_MUTABLE
    )
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.game_notification_channel_id)
    ).setSmallIcon(R.drawable.logo)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
    notify(NOTIFICATION_ID,builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}