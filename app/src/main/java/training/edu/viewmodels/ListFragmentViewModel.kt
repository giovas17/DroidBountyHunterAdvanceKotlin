package training.edu.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.data.entities.Fugitivo
import training.edu.usecases.FugitivosUseCase

/**
 * Created by rdelgado on 5/8/2018.
 */
class ListFragmentViewModel(droidBountyHunterDatabase: DroidBountyHunterDatabase?) : ViewModel(){

    private val fugitivosUseCase = FugitivosUseCase(droidBountyHunterDatabase)

    fun getFugitivos(mode: Int): LiveData<List<Fugitivo>>{
        if(mode == 0){
            return fugitivosUseCase.getFugitivos()
        }else{
            return fugitivosUseCase.getCapturados()
        }
    }
}