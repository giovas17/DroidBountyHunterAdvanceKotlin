package training.edu.droidbountyhunter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import org.json.JSONException
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.droidbountyhunter.interfaces.OnTaskListener
import training.edu.network.NetServices
import training.edu.viewmodels.AgregarViewModel

/**
 * Created by rdelgado on 5/4/2018.
 */
class Agregar : AppCompatActivity() {

    private lateinit var vm: AgregarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
        vm = AgregarViewModel(DroidBountyHunterDatabase.getInstance(applicationContext))
    }

    fun onSaveClick(view : View) {
        val name = findViewById<TextView>(R.id.editTextName)
        if (name.text.isNotEmpty()) {
            vm.addFugitivo(name.text.toString())
            setResult(0)
            finish()
        } else {
            android.app.AlertDialog.Builder(this)
                    .setTitle("Alerta")
                    .setMessage("Favor de capturar el nombre del fugitivo.")
                    .show()
        }
    }

    fun onWebServiceClick(view: View) {

        if(vm.getFugitivosCount() == 0){
            val apiCall = NetServices(object : OnTaskListener {
                override fun onTaskCompleted(response: String) {
                    try {
                        vm.addFugitivoFromJSON(response)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } finally {
                        // Despues de cargar los registros en la web en la base de datos cerraremos el activity
                        setResult(0)
                        finish()
                    }
                }

                override fun onTaskError(errorCode: Int, message: String, error: String) {
                    // Error de servicio.
                    Toast.makeText(this@Agregar, "Ocurrió un problema con el Webservice!!", Toast.LENGTH_LONG).show()
                }
            })
            apiCall.execute("Fugitivos")
        } else {
            // Error de servicio.
            Toast.makeText(this, "No se puede hacer la carga remota ya que se tiene al menos un fugitivo" + " en la base de datos", Toast.LENGTH_LONG).show()
        }
    }

    internal fun retardo() {
        // Se implementa retardo en el hilo
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun onXMLClick(view: View) {

        if (vm.getFugitivosCount() <= 0) {
            // Se ocultan los botones que no se utilizarán
            val nameTxt = findViewById<TextView>(R.id.editTextName)
            nameTxt.visibility = View.GONE
            val botonXML = findViewById<Button>(R.id.buttonAddXML)
            botonXML.visibility = View.GONE
            val botonSave = findViewById<Button>(R.id.buttonSave)
            botonSave.visibility = View.GONE
            val botonWebService = findViewById<Button>(R.id.buttonAddWebService)
            botonWebService.visibility = View.GONE
            //----------------------------------------------------------------------
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            val statusProgress = findViewById<TextView>(R.id.txtProgreso)
            progressBar.max = 100

            object : Thread() {
                override fun run() {
                    progressBar.post { progressBar.progress = 0 }
                    val items = vm.getXmlNodeItems(resources)
                    for (i in 0 until items!!.length) {
                        val valor = items.item(i).firstChild.nodeValue
                        val contadorPorcentaje = (i + 1) * 10
                        retardo()
                        vm.addFugitivo(valor)
                        progressBar.post {
                            statusProgress.text = (getString(R.string.progreso)
                                    + " " + contadorPorcentaje + "%")
                            progressBar.incrementProgressBy(10)
                        }
                    }
                    runOnUiThread {
                        Toast.makeText(applicationContext,
                                "Importación de Fugitivos finalizada!", Toast.LENGTH_LONG).show()
                        setResult(0)
                        finish()
                    }
                }
            }.start()
        } else {
            // notificacion de Fugitivos en la Base de Datos
            Toast.makeText(applicationContext,
                    "No es posible solicitar carga via XML ya que se tiene al menos un fugitivo" + "en la base de datos", Toast.LENGTH_LONG).show()
        }
    }
}