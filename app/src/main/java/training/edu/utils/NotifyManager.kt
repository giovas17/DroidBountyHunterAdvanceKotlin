package training.edu.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat

/**
 * Created by rdelgado on 5/4/2018.
 */
class NotifyManager {

    companion object {
        const val SIMPLE_NOTIFICATION_ID = 1
    }

    fun playNotification(context: Context, cls: Class<*>, textNotification: String, titleNotification: String, drawable: Int) {

        /*NOTIFICATION VARS*/
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifyDetails =  Notification(drawable, titleNotification, System.currentTimeMillis())
        /*NOTIFICATION INICIO*/
        val vibrate = longArrayOf(100, 100, 200, 300)
        notifyDetails.vibrate = vibrate
        notifyDetails.defaults = Notification.DEFAULT_ALL
        notifyDetails.flags = notifyDetails.flags or Notification.FLAG_AUTO_CANCEL

        /*NOTIFICATION FIN*/
        val notifyIntent = Intent(context, cls)
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        notifyDetails.tickerText = textNotification

        try {
            mNotificationManager.notify(SIMPLE_NOTIFICATION_ID, notifyDetails)
        } catch (e: Exception) {

        }

    }

    fun enviarNotificacion(context: Context, cls: Class<*>, textNotification: String?, titleNotification: String, drawable: Int, idNotification: Int) {

        val vibrate = longArrayOf(100, 100, 200, 300)
        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(drawable)
                .setContentTitle(titleNotification)
                .setContentText(textNotification)
                .setVibrate(vibrate)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
        // Creates an explicit intent for an Activity in your app
        val resultIntent = Intent(context, cls)

        val resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0)
        mBuilder.setContentIntent(resultPendingIntent)
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //se manda el id para notificar cual es la notificacion que se actualizar�
        mNotificationManager.notify( if(idNotification == 0) SIMPLE_NOTIFICATION_ID else idNotification, mBuilder.build())
    }
}