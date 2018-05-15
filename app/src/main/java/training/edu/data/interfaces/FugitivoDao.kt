package training.edu.data.interfaces

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import training.edu.data.entities.Fugitivo


/**
 * Created by rdelgado on 5/8/2018.
 */
@Dao
interface FugitivoDao {

    @Query("SELECT * FROM fugitivos WHERE status = '0'")
    fun getFugitivos(): LiveData<List<Fugitivo>>

    @Query("SELECT * FROM fugitivos WHERE status = '1' ")
    fun getCapturados(): LiveData<List<Fugitivo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fugitivo: Fugitivo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(fugitivo: Fugitivo)

    @Delete
    fun delete(fugitivo: Fugitivo)

    @Query("SELECT COUNT(*) FROM fugitivos" )
    fun count(): Int
}