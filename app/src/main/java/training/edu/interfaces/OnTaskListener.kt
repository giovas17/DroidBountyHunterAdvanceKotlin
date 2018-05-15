package training.edu.droidbountyhunter.interfaces

/**
 * Created by rdelgado on 5/4/2018.
 */
interface OnTaskListener {
    fun onTaskCompleted(response: String)
    fun onTaskError(errorCode: Int, message: String, error: String)
}