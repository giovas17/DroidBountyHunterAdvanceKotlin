package training.edu.viewmodels

import android.arch.lifecycle.ViewModel
import android.content.res.Resources
import org.json.JSONArray
import org.w3c.dom.NodeList
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.data.entities.Fugitivo
import training.edu.usecases.FugitivosUseCase
import training.edu.usecases.XmlUseCase


/**
 * Created by rdelgado on 5/8/2018.
 */
class AgregarViewModel(droidBountyHunterDatabase: DroidBountyHunterDatabase?) : ViewModel() {

    private val fugitivosUseCase = FugitivosUseCase(droidBountyHunterDatabase)
    private val xmlUseCase = XmlUseCase()

    fun addFugitivo(name: String){
        fugitivosUseCase.addFugitivo(Fugitivo(null, name, "0", "",0))
    }

    fun getFugitivosCount(): Int{
        return fugitivosUseCase.getFugitivosCount()
    }

    fun getXmlNodeItems(resources: Resources): NodeList?{
        return xmlUseCase.getNodeItems(resources)
    }

    fun addFugitivoFromJSON(response: String){

        val array = JSONArray(response)
        for (i in 0 until array.length()) {
            val jsonObject = array.getJSONObject(i)
            val nameFugitive = jsonObject.optString("name", "")
            addFugitivo(nameFugitive)
        }

    }
}