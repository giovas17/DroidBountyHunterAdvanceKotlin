package training.edu.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by rdelgado on 5/7/2018.
 */
class Receiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (!ServicioNotificaciones.isRunning()) {
            context.startService(Intent(context, ServicioNotificaciones::class.java))
        }
    }

}