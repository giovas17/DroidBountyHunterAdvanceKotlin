package training.edu.droidbountyhunter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.droidbountyhunter.interfaces.OnTaskListener
import training.edu.network.NetServices
import training.edu.utils.PictureTools
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
        vm.id = intent.getIntExtra("id", 0)

        // Se pone el nombre del fugitivo como titulo
        title = vm.titulo + " - [" + vm.id + "]"

        val message = findViewById<TextView>(R.id.mensajeText)

        // Se identifica si es Fugitivo o Capturado para el mensaje...
        if (vm.mode == 0) {
            message.text = "El fugitivo sigue suelto..."
        } else {
            val delete = findViewById<View>(R.id.buttonEliminar) as Button
            delete.visibility = View.GONE
            message.text = "Atrapado!!!"
            val photoImageView = findViewById<ImageView>(R.id.pictureFugitive)
            val pathPhoto = intent.getStringExtra("photo")
            if (pathPhoto != null && pathPhoto.isNotEmpty()) {
                val bitmap = PictureTools.decodeSampledBitmapFromUri(pathPhoto, 200, 200)
                photoImageView.setImageBitmap(bitmap)
                vm.foto = pathPhoto
            }
        }
    }

    fun onCaptureClick(view: View) {
        val pathPhoto = PictureTools.currentPhotoPath
        if (pathPhoto.isEmpty()) {
            Toast.makeText(this, "Es necesario tomar la foto antes de capturar al fugitivo", Toast.LENGTH_LONG).show()
            return
        }
        vm.updateCapturedFugitivo()

        val netServices = NetServices(object : OnTaskListener {
            override fun onTaskCompleted(response: String) {
                messageClose(vm.getMessageFromJSON(response))
            }
            override fun onTaskError(errorCode: Int, message: String, error: String) {
                Toast.makeText(this@Detalle, "Ocurrio un problema en la comunicación con el WebService!!!", Toast.LENGTH_LONG).show()
            }
        })
        netServices.execute("Atrapar", Home.UDID)
        setResult(0)
    }

    fun onDeleteClick(view: View) {
        vm.deleteFugitivo(vm.id)
        setResult(0)
        finish()
    }

    fun messageClose(message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.create()
        builder.setTitle("Alerta!!!")
        builder.setMessage(message)
        builder.setOnDismissListener {
            setResult(vm.mode)
            finish()
        }
        builder.show()
    }

    fun onFotoClick(view: View) {
        if (PictureTools.permissionReadMemmory(this)) {
            dispatchPicture()
        }
    }

    private fun dispatchPicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        vm.pathImage = PictureTools.getOutputMediaFileUri(this@Detalle, MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, vm.pathImage)
        startActivityForResult(intent, DetalleViewModel.REQUEST_CODE_PHOTO_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DetalleViewModel.REQUEST_CODE_PHOTO_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val imageFugitive = findViewById<ImageView>(R.id.pictureFugitive)
                val bitmap = PictureTools.decodeSampledBitmapFromUri(PictureTools.currentPhotoPath, 200, 200)
                imageFugitive.setImageBitmap(bitmap)
                vm.updatePhotoFugitivo(PictureTools.currentPhotoPath)
            }
        }
    }
}