package training.edu.services

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by rdelgado on 5/4/2018.
 */
class FirebaseIdService : FirebaseInstanceIdService(){

    companion object {
        private val TAG = FirebaseIdService::class.java.simpleName
    }

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Token: " + refreshedToken!!)
    }
}