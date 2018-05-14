package training.edu.viewmodels

import android.arch.lifecycle.ViewModel
import org.json.JSONArray
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.data.entities.Fugitivo
import training.edu.usecases.FugitivosUseCase


/**
 * Created by rdelgado on 5/8/2018.
 */
class AgregarViewModel(droidBountyHunterDatabase: DroidBountyHunterDatabase?) : ViewModel() {

    private val fugitivosUseCase = FugitivosUseCase(droidBountyHunterDatabase)

    fun addFugitivo(name: String){
        fugitivosUseCase.addFugitivo(Fugitivo(null, name, "0", "", 0))
    }

    fun getFugitivosCount(): Int{
        return fugitivosUseCase.getFugitivosCount()
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