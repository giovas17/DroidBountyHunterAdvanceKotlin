package training.edu.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.data.entities.Log
import training.edu.usecases.LogUseCase

/**
 * Created by rdelgado on 5/9/2018.
 */
class LogEliminacionViewModel(droidBountyHunterDatabase: DroidBountyHunterDatabase?) : ViewModel() {

    private val logUseCase = LogUseCase(droidBountyHunterDatabase)

    fun getLogs(): LiveData<List<Log>>{
        return logUseCase.getLogs()
    }
}