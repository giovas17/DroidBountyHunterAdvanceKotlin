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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_log_eliminacion)
        title = "Log de Eliminación"
    }
}