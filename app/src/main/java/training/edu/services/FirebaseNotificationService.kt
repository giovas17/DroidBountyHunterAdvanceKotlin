package training.edu.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import training.edu.droidbountyhunter.Home
import training.edu.droidbountyhunter.R
import training.edu.utils.NotifyManager

/**
 * Created by rdelgado on 5/4/2018.
 */
class FirebaseNotificationService : FirebaseMessagingService() {

    companion object {
        private val TAG = FirebaseNotificationService::class.java.simpleName
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.w(TAG, "From: " + remoteMessage.from)
        Log.w(TAG, "Notification Message Body: " + remoteMessage.notification!!.body)

        val manager = NotifyManager()
        manager.enviarNotificacion(this, Home::class.java,
                remoteMessage.notification!!.body,
                "Notificacion Push", R.mipmap.ic_launcher, 0)

    }
}