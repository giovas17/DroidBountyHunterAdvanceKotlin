package training.edu.droidbountyhunter

import android.content.Intent
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import training.edu.utils.SimpleRenderer

/**
 * Created by rdelgado on 5/7/2018.
 */
class ActivityOpenGLFugitivos : AppCompatActivity() {

    lateinit var glView: GLSurfaceView

    companion object {
        lateinit var foto: String
        var distorcion: Float = 0f
        lateinit var fotoDefault: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var oExt = this.intent.extras
        foto = oExt.getString("foto")
        distorcion = oExt.getFloat("distorcion")
        fotoDefault = oExt.getString("default")
        glView = GLSurfaceView(this)
        glView.setRenderer(SimpleRenderer(this))
        setContentView(glView)
    }

    override fun onResume() {
        super.onResume()
        glView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glView.onPause()
    }

    fun onOpenGLClick(v: View){
        var txtView = findViewById<View>(R.id.mensajeText) as TextView
        var texto = txtView.text.toString()
        var cbDefault = "0"

        var checkBox = findViewById<View>(R.id.cbDefault) as CheckBox

        if(checkBox.isChecked){
            cbDefault = "1"
        }

        try {
            var textoFloat =  texto.toFloat()

            if(textoFloat < 0.0f){
                Toast.makeText(this,
                        "Entrada erronea debe ser número flotante positivo.",
                        Toast.LENGTH_LONG).show()
                return
            }

            var intentOpenGl = Intent()
            intentOpenGl.setClass(this, ActivityOpenGLFugitivos::class.java)
            intentOpenGl.putExtra("foto", foto)
            intentOpenGl.putExtra("distorcion", texto)
            intentOpenGl.putExtra("default", cbDefault)
        }catch (e: Exception) {
            Toast.makeText(this,
                    "Entrada erronea debe ser número flotante.",
                    Toast.LENGTH_LONG).show()
        }
    }
}