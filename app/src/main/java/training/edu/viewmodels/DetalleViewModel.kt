package training.edu.viewmodels

import android.arch.lifecycle.ViewModel
import android.net.Uri
import org.json.JSONException
import org.json.JSONObject
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.data.entities.Fugitivo
import training.edu.usecases.FugitivosUseCase

/**
 * Created by rdelgado on 5/9/2018.
 */
class DetalleViewModel(droidBountyHunterDatabase: DroidBountyHunterDatabase?) : ViewModel() {

    private val fugitivosUseCase = FugitivosUseCase(droidBountyHunterDatabase)

    lateinit var titulo: String
    var mode: Int = 0
    var id: Int = 0
    var pathImage: Uri? = null
    var foto: String = ""

    companion object {
        const val REQUEST_CODE_PHOTO_IMAGE = 1787
    }

    fun updatePhotoFugitivo(pathPhoto : String){
        foto = pathPhoto
        fugitivosUseCase.updateFugitivo(Fugitivo(id, titulo, mode.toString(), if (pathPhoto.isEmpty()) "" else pathPhoto, 0))
    }

    fun updateCapturedFugitivo(){
        fugitivosUseCase.updateFugitivo(Fugitivo(id, titulo, "1", foto, 0))
    }

    fun getMessageFromJSON(response: String): String?{
        // despues de traer los datos del web service se actualiza la interfaz...
        try {
            val jsonObject = JSONObject(response)
            return jsonObject.optString("mensaje", "")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    fun deleteFugitivo(id: Int){
        fugitivosUseCase.deleteFugitivo(Fugitivo(id,"","","", 0))
    }


}