package training.edu.fragments

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.data.entities.Log
import training.edu.droidbountyhunter.DetalleLogEliminacion
import training.edu.droidbountyhunter.R
import training.edu.droidbountyhunter.interfaces.OnLogListener
import training.edu.viewmodels.LogEliminacionViewModel

/**
 * Created by rdelgado on 5/4/2018.
 */
class LogEliminacion : Fragment(){

    private var adaptador: ArrayAdapter<Log>? = null
    private var listener: OnLogListener? = null
    private var isTablet = false

    private lateinit var vm: LogEliminacionViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isTablet = activity!!.supportFragmentManager.findFragmentById(R.id.fragmentDetalleLogEliminacion) != null
        if (isTablet) {
            listener = activity!!.supportFragmentManager.findFragmentById(R.id.fragmentDetalleLogEliminacion) as OnLogListener
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_log_eliminacion, container, false)

        vm = LogEliminacionViewModel(DroidBountyHunterDatabase.getInstance(context))

        val lista = view.findViewById<ListView>(R.id.list)

        vm.getLogs().observe(this, Observer {  logsList ->
            if (logsList!!.isNotEmpty()) {
                adaptador = ArrayAdapter(this.context,
                        android.R.layout.simple_list_item_1, logsList)
                // Assign adapter to ListView
                lista.adapter = adaptador
            }
        })

        lista.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            val date = adaptador!!.getItem(position).date
            val status = adaptador!!.getItem(position).status

            Toast.makeText(context, position.toString() + " " + adaptador!!.getItem(position), Toast.LENGTH_LONG).show()
            if (isTablet) {
                listener!!.onLogItemList(date.toString(), status)
            } else {
                val intent = Intent(context, DetalleLogEliminacion::class.java)
                intent.putExtra("isTablet", isTablet)
                intent.putExtra("status", status)
                intent.putExtra("date", date)
                startActivity(intent)
            }
        }

        return view
    }
}