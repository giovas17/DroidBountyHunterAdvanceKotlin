package training.edu.droidbountyhunter

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import org.json.JSONException
import training.edu.data.database.DroidBountyHunterDatabase
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
}