package training.edu.data.interfaces

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import training.edu.data.entities.Log

/**
 * Created by rdelgado on 5/8/2018.
 */
@Dao
interface LogDao {
    @Query("SELECT * FROM Log")
    fun getAll(): LiveData<List<Log>>

    @Query("SELECT COUNT(*) FROM fugitivos WHERE notification = '1'" )
    fun countFugitivosSinNotificar(): Int
}