package training.edu.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.opengl.GLU
import android.opengl.GLUtils
import android.util.Log
import training.edu.droidbountyhunter.ActivityOpenGLFugitivos
import training.edu.droidbountyhunter.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by rdelgado on 5/7/2018.
 */
class SimpleRenderer(private val context: Context) : GLSurfaceView.Renderer {

    private var vertexBuffer: FloatBuffer? = null
    private var texturalBuffer: FloatBuffer? = null
    private var indexBuffer: ShortBuffer? = null
    private var carasLength: Int = 0

    private fun cargarTextura(gl: GL10) {
        val bitmap: Bitmap?
        if (ActivityOpenGLFugitivos.fotoDefault.equals("0")) {
            bitmap = PictureTools.decodeSampledBitmapFromUri(ActivityOpenGLFugitivos.foto, 200, 200)
        } else {
            bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
        }
        val textureIds = IntArray(1)
        gl.glGenTextures(1, textureIds, 0)
        val textureId = textureIds[0]

        gl.glEnable(GL10.GL_TEXTURE_2D)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId)

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR.toFloat())

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)

    }

    override fun onSurfaceCreated(gl: GL10, eglConfig: EGLConfig) {

    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        Log.d("AR", "superficie modificada: " + width + "x" + height)
        val positivo = ActivityOpenGLFugitivos.distorcion
        val negativo = ActivityOpenGLFugitivos.distorcion * -1.0f

        val vertices = floatArrayOf(negativo, 1f, 0f, -1f, -1f, 0f, 0f, -1f, 0f, 1f, -1f, 0f, positivo, 1f, 0f)

        val caras = shortArrayOf(0, 1, 2, 0, 2, 4, 2, 3, 4)

        carasLength = caras.size

        val textura = floatArrayOf(0f, 0f, 0f, 1f, 0.5f, 1f, 1f, 1f, 1f, 0f)

        var vbb =  ByteBuffer.allocateDirect(vertices.size*4)
        vbb.order(ByteOrder.nativeOrder())
        vertexBuffer = vbb.asFloatBuffer()
        vertexBuffer?.put(vertices)
        vertexBuffer?.position(0)

        var ibb =  ByteBuffer.allocateDirect(caras.size*4)
        ibb.order(ByteOrder.nativeOrder())
        indexBuffer = ibb.asShortBuffer()
        indexBuffer?.put(caras)
        indexBuffer?.position(0)

        var tbb =  ByteBuffer.allocateDirect(textura.size*4)
        tbb.order(ByteOrder.nativeOrder())
        texturalBuffer = tbb.asFloatBuffer()
        texturalBuffer?.put(textura)
        texturalBuffer?.position(0)


        gl.glViewport(0,0,width, height)

        gl.glMatrixMode(GL10.GL_PROJECTION)

        gl.glLoadIdentity()

        GLU.gluPerspective(gl, 45.0f, (width / height).toFloat() , 0.1f, 100.0f)

        gl.glMatrixMode(GL10.GL_MODELVIEW)

        gl.glLoadIdentity()

    }

    private fun draw(gl: GL10){
        cargarTextura(gl)

        gl.glFrontFace(GL10.GL_CCW)

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)

        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)

        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texturalBuffer)

        gl.glDrawElements(GL10.GL_TRIANGLES, carasLength, GL10.GL_UNSIGNED_SHORT, indexBuffer)

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
    }

    override fun onDrawFrame(gl: GL10) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)

        gl.glLoadIdentity()

        gl.glTranslatef(0f, 0f, -7f)

        draw(gl)
    }
}
