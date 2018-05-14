package training.edu.droidbountyhunter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.viewmodels.DetalleViewModel

/**
 * Created by rdelgado on 5/4/2018.
 */
class Detalle :  AppCompatActivity() {

    private lateinit var vm: DetalleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        vm = DetalleViewModel(DroidBountyHunterDatabase.getInstance(applicationContext))

        // Se obtiene la información del intent
        vm.titulo = intent.getStringExtra("title")
        vm.mode = intent.getIntExtra("mode", 0)

        // Se pone el nombre del fugitivo como titulo
        title = vm.titulo

        val message = findViewById<TextView>(R.id.mensajeText)

        // Se identifica si es Fugitivo o Capturado para el mensaje...
        if (vm.mode == 0) {
            message.text = "El fugitivo sigue suelto..."
        } else {
            message.text = "Atrapado!!!"
        }

    }
}