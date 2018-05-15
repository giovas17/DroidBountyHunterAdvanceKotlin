package training.edu.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.droidbountyhunter.Home
import training.edu.droidbountyhunter.R
import training.edu.usecases.FugitivosUseCase
import training.edu.usecases.LogUseCase
import training.edu.utils.NotifyManager
import java.util.*

/**
 * Created by rdelgado on 5/4/2018.
 */
class ServicioNotificaciones : Service(){

    private lateinit var database: DroidBountyHunterDatabase
    private lateinit var fugitivosUseCase: FugitivosUseCase
    private lateinit var logUseCase: LogUseCase

    companion object {

        var instance: ServicioNotificaciones? = null

        fun isRunning(): Boolean {
            return instance != null
        }
    }

    private var timer: Timer? = null

    override fun onCreate() {
        super.onCreate()

        database = DroidBountyHunterDatabase.getInstance(this)!!
        fugitivosUseCase = FugitivosUseCase(database)
        logUseCase = LogUseCase(database)

        Toast.makeText(this, "Servicio Creado", Toast.LENGTH_SHORT).show()
        instance = this
    }

    fun enviarNotificacion() {
        try {
            var mensaje = ""
            
            val added = fugitivosUseCase.getFugitivosCount()
            val deleted = logUseCase.getCountFugitivosSinNotificar()
            if (added > 0) {
                mensaje += "Añadiste $added"
                if (deleted > 0) {
                    mensaje += ", Eliminaste $deleted"
                }
            } else if (deleted > 0) {
                mensaje += "Eliminaste $deleted"
            } else {
                mensaje = ""
            }
            if (mensaje.isNotEmpty()) {
                // Se crea una Notificacion
                val manager = NotifyManager()
                manager.enviarNotificacion(this, Home::class.java, mensaje,
                        "Notificacion DroidBountyHunter",
                        R.mipmap.ic_launcher, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Servicio Arrancado $startId", Toast.LENGTH_SHORT).show()
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                enviarNotificacion()
            }
        }, 0, (1000 * 60).toLong())
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "Servicion detenido", Toast.LENGTH_SHORT).show()
        instance = null
    }
}