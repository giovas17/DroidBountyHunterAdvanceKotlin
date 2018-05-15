package training.edu.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import training.edu.droidbountyhunter.R
import training.edu.droidbountyhunter.interfaces.OnLogListener

/**
 * Created by rdelgado on 5/4/2018.
 */
class DetalleLogEliminacion : Fragment(), OnLogListener {

    private var isTablet = false
    private var statusTxt: TextView? = null
    private var dateTxt: TextView? = null
    private var status: String? = null
    private var date: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isTablet = activity!!.intent.getBooleanExtra("isTablet", false)
        if (!isTablet) {
            status = activity!!.intent.getStringExtra("status")
            date = activity!!.intent.getStringExtra("date")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detalle_log_de_eliminacion, container, false)

        statusTxt = view.findViewById(R.id.txtEstatus)
        dateTxt = view.findViewById(R.id.txtFecha)
        if (!isTablet) {
            updateData()
        }

        return view
    }

    private fun updateData() {
        if (status != null || date != null) {
            statusTxt!!.text = if (status == "0") "Fugitivo" else "Atrapado"
            dateTxt!!.text = date
        }
    }

    override fun onLogItemList(date: String, status: String) {
        this.status = status
        this.date = date
        updateData()
    }
}