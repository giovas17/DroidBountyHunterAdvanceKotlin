package training.edu.usecases

import android.arch.lifecycle.LiveData
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.data.entities.Fugitivo

/**
 * Created by rdelgado on 5/8/2018.
 */
class FugitivosUseCase constructor(droidBountyHunterDatabase: DroidBountyHunterDatabase?) {

    private val fugitivoDao = droidBountyHunterDatabase!!.fugitivoDao()

    fun getFugitivos(): LiveData<List<Fugitivo>> {
        return fugitivoDao.getFugitivos()
    }

    fun getCapturados(): LiveData<List<Fugitivo>> {
        return fugitivoDao.getCapturados()
    }

    fun addFugitivo(fugitivo: Fugitivo){
        val t = Thread{
            fugitivoDao.insert(fugitivo)
        }
        t.start()
    }

    fun getFugitivosCount(): Int{
        var count = 0
        val t = Thread{
            count =  fugitivoDao.count()
        }
        t.start()
        t.join()
        return count
    }

    fun updateFugitivo(fugitivo: Fugitivo){
        val t = Thread{
            fugitivoDao.update(fugitivo)
        }
        t.start()
    }

    fun deleteFugitivo(fugitivo: Fugitivo){
        val t = Thread{
            fugitivoDao.delete(fugitivo)
        }
        t.start()
    }
}