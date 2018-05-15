package training.edu.network

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import training.edu.droidbountyhunter.interfaces.OnTaskListener
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by rdelgado on 5/4/2018.
 */
class NetServices (private var listener: OnTaskListener) : AsyncTask<String, Void, Boolean>() {

    companion object {
        private val LOG_TAG = NetServices::class.java.simpleName

        private const val endpoint_fugitivos = "http://201.168.207.210/services/droidBHServices.svc/fugitivos"
        private const val endpoint_atrapados = "http://201.168.207.210/services/droidBHServices.svc/atrapados"
        private lateinit var JSONString: String
        private var isFugitivos = true
        private var code = 0
        private lateinit var message: String
        private lateinit var error: String

        private const val TIME_OUT = 5000
    }

    override fun doInBackground(vararg params: String): Boolean? {
        var urlConnection: HttpURLConnection? = null
        var reader: BufferedReader? = null

        try {

            isFugitivos = params[0].matches("Fugitivos".toRegex())

            urlConnection = getStructuredRequest(if (isFugitivos) TYPE.FUGITIVOS else TYPE.ATRAPADOS,
                    if (isFugitivos) endpoint_fugitivos else endpoint_atrapados, if (isFugitivos) "" else params[1])

            val inputStream = urlConnection.inputStream
            reader = BufferedReader(InputStreamReader(inputStream))
            val inputAsString = reader.use { it.readText() }
            if (inputAsString.isEmpty())
                return true
            JSONString = inputAsString
            Log.d(LOG_TAG, "Respuesta del Servidor: $JSONString")
            return true

        } catch (e: FileNotFoundException) {
            manageError(urlConnection)
            return false
        } catch (e: IOException) {
            manageError(urlConnection)
            return false
        } catch (e: Exception) {
            manageError(urlConnection)
            return false
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    Log.e(LOG_TAG, "Error Closing Stream", e)
                }

            }
        }
    }

    @Throws(IOException::class, JSONException::class)
    private fun getStructuredRequest(type: TYPE, endpoint: String, id: String): HttpURLConnection {

        val urlConnection: HttpURLConnection?
        val url: URL?
        if (type == TYPE.FUGITIVOS) { //----------------------------- GET Fugitivos------------------------------------
            url = URL(endpoint)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.readTimeout = TIME_OUT
            urlConnection.requestMethod = "GET"
            urlConnection.setRequestProperty("Content-Type", "application/json")
            urlConnection.connect()
        } else { //------------------------ POST Atrapados----------------------------------
            url = URL(endpoint)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "POST"
            urlConnection.readTimeout = TIME_OUT
            urlConnection.setRequestProperty("Content-Type", "application/json")
            urlConnection.doInput = true
            urlConnection.doOutput = true
            urlConnection.connect()
            val jsonObj = JSONObject()
            jsonObj.put("UDIDString", id)
            //Log.d(LOG_TAG, "UDIDString : " + id)
            Log.d(LOG_TAG, "JsonObj : " + jsonObj.toString())
            val dataOutputStream = DataOutputStream(urlConnection.outputStream)
            dataOutputStream.write(jsonObj.toString().toByteArray())
            dataOutputStream.flush()
            dataOutputStream.close()
        }
        Log.d(LOG_TAG, url.toString())
        return urlConnection
    }

    private fun manageError(urlConnection: HttpURLConnection?) {
        if (urlConnection != null) {
            try {
                code = urlConnection.responseCode
                if (urlConnection.errorStream != null) {
                    val `is` = urlConnection.errorStream
                    val buffer = StringBuilder()
                    val reader = BufferedReader(InputStreamReader(`is`))
                    //var line: String
                    while ( reader.readLine() != null) {
                        buffer.append(reader.readLine() + "\n")
                    }
                    message = buffer.toString()
                } else {
                    message = urlConnection.responseMessage
                }
                error = urlConnection.errorStream.toString()
                Log.e(LOG_TAG, "Error: $message, code: $code")
            } catch (e1: IOException) {
                e1.printStackTrace()
                Log.e(LOG_TAG, "Error")
            }

        } else {
            code = 105
            message = "Error: No internet connection"
            Log.e(LOG_TAG, "code: $code, $message")
        }
    }

    override fun onPostExecute(result: Boolean?) {
        if (result!!) {
            listener.onTaskCompleted(JSONString)
        } else {
            listener.onTaskError(code, message, error)
        }
    }

    internal enum class TYPE {
        FUGITIVOS, ATRAPADOS
    }
}