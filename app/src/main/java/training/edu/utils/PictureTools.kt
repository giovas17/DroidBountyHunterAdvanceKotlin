package training.edu.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by rdelgado on 5/4/2018.
 */
class PictureTools {

    companion object {

        private const val REQUEST_CODE = 1707

        private var BASE_PATH = ""


        var currentPhotoPath = ""


        private val TAG = PictureTools::class.java.simpleName

        private fun getCameraPhotoOrientation(imagePath: String): Int {
            var rotate = 0
            try {
                val imageFile = File(imagePath)
                val exif = ExifInterface(
                        imageFile.absolutePath)
                val orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL)
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                }

                Log.v(TAG, "Exif orientation: $orientation")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return rotate
        }

        /** Create a file Uri for saving an image or video  */
        fun getOutputMediaFileUri(context: Context, type: Int): Uri? {
            try {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    FileProvider.getUriForFile(context,
                            context.packageName + ".provider", getOutputMediaFile())
                } else Uri.fromFile(getOutputMediaFile())
            } catch (e: IOException) {
                return null
            }
        }

        /** Create a File for saving an image or video  */
        @Throws(IOException::class)
        private fun getOutputMediaFile(): File {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.
            val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).path, "DroidBountyHunterPictures")

            BASE_PATH = mediaStorageDir.path + File.separator
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("File", "No se pudo crear el folder")
                }
            }

            // Create a media file name
            val timeStamp = SimpleDateFormat("yyyy-MM-dd_HHmmss").format(Date())
            var path = "PNG_$timeStamp.png"
            path = path.replace(" ", "_")
            if (path.contains("'")) {
                path = path.replace("'", "")
            }
            val imageFileName = BASE_PATH + path
            val image = File(imageFileName)

            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = image.absolutePath

            return image
        }

        fun decodeSampledBitmapFromUri(dir: String, Width: Int, Height: Int): Bitmap? {
            val rotatedBitmap: Bitmap?
            try {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(dir, options)

                options.inSampleSize = calculateInSampleSize(options, Width, Height)
                options.inJustDecodeBounds = false
                val bitmap = BitmapFactory.decodeFile(dir, options)
                val matrix = Matrix()
                matrix.postRotate(PictureTools.getCameraPhotoOrientation(dir).toFloat())
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } catch (e: Exception) {
                return null
            }

            return rotatedBitmap
        }

        private fun calculateInSampleSize(options: BitmapFactory.Options, Width: Int, Height: Int): Int {
            val height = options.outHeight
            val width = options.outWidth
            var sizeInicialize = 1

            if (height > Height || width > Width) {
                if (width > height) {
                    sizeInicialize = Math.round(height.toFloat() / Height.toFloat())
                } else {
                    sizeInicialize = Math.round(width.toFloat() / Width.toFloat())
                }
            }
            return sizeInicialize
        }

        fun permissionReadMemmory(context: Activity): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation
                    if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), REQUEST_CODE)
                        return false
                    } else {
                        //No explanation needed, we can request the permissions.
                        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), REQUEST_CODE)
                        return false
                    }
                } else {
                    return true
                }
            } else {
                return true
            }
        }
    }
}