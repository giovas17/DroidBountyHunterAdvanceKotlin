package training.edu.usecases

import android.arch.lifecycle.LiveData
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.data.entities.Log

/**
 * Created by rdelgado on 5/9/2018.
 */
class LogUseCase(droidBountyHunterDatabase: DroidBountyHunterDatabase?) {

    private val logDao = droidBountyHunterDatabase!!.logDao()

    fun getLogs(): LiveData<List<Log>>{
        return logDao.getAll()
    }
}