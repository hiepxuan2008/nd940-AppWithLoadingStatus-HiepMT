package com.udacity.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.udacity.DetailActivity
import com.udacity.R


const val NOTIFICATION_DOWNLOAD_COMPLETED_ID = 0
const val DOWNLOAD_CHANNEL_ID = "download_channel_id"

const val EXTRA_FILE_NAME_KEY = "fileName"
const val EXTRA_DOWNLOAD_SUCCEED_KEY = "downloadSucceeded"

object NotificationUtils {
    fun sendNotificationDownloadCompleted(
        context: Context,
        fileName: String,
        downloadSucceeded: Boolean
    ) {
        // Make a channel if necessary
        createChannel(
            context,
            DOWNLOAD_CHANNEL_ID,
            context.getString(R.string.download_channel_name),
            context.getString(R.string.download_channel_description)
        )

        val contentIntent = Intent(context, DetailActivity::class.java)
        contentIntent.putExtra(EXTRA_FILE_NAME_KEY, fileName)
        contentIntent.putExtra(EXTRA_DOWNLOAD_SUCCEED_KEY, downloadSucceeded)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            0,
            contentIntent,
            PendingIntent.FLAG_ONE_SHOT
        )

        // Create the notification
        val notiDesc = when (downloadSucceeded) {
            true -> context.getString(R.string.notification_description, fileName, "succeed!")
            false -> context.getString(R.string.notification_description, fileName, "failed!")
        }

        val largeIcon = when (downloadSucceeded) {
            true -> Utils.getBitmapFromDrawable(context, R.drawable.ic_outline_check_circle_24)
            else -> Utils.getBitmapFromDrawable(context, R.drawable.ic_outline_error_24)
        }

        val builder = NotificationCompat.Builder(context, DOWNLOAD_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(notiDesc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(contentPendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(notiDesc)
            )
            .addAction(
                R.drawable.ic_assistant_black_24dp,
                context.getString(R.string.notification_button),
                contentPendingIntent
            )
            .setLargeIcon(largeIcon)

        // Show the notification
        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_DOWNLOAD_COMPLETED_ID, builder.build())
    }

    fun createChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDescription: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setShowBadge(false)
                enableVibration(true)
                description = channelDescription
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}