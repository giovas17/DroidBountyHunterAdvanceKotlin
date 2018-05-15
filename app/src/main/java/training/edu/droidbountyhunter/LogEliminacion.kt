package training.edu.droidbountyhunter

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.data.entities.Log
import training.edu.viewmodels.LogEliminacionViewModel

/**
 * Created by rdelgado on 5/4/2018.
 */
class LogEliminacion : AppCompatActivity(){

    private var adaptador: ArrayAdapter<Log>? = null
    private lateinit var vm: LogEliminacionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_log_eliminacion)
        title = "Log de Eliminación"

        vm = LogEliminacionViewModel(DroidBountyHunterDatabase.getInstance(applicationContext))

        val lista = findViewById<ListView>(R.id.list)

        vm.getLogs().observe(this, Observer {  logsList ->
            if (logsList!!.isNotEmpty()) {
                adaptador = ArrayAdapter(applicationContext,
                        android.R.layout.simple_list_item_1, logsList)
                // Assign adapter to ListView
                lista.adapter = adaptador
            }
        })

        lista.onItemClickListener = AdapterView.OnItemClickListener {
            _, _, position, _ ->
            Toast.makeText(this@LogEliminacion, position.toString() + " " +
                    adaptador!!.getItem(position)!!.toString(), Toast.LENGTH_LONG).show() }
    }
}