package training.edu.data.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import training.edu.data.converters.TimestampConverter
import training.edu.data.entities.Fugitivo
import training.edu.data.entities.Log
import training.edu.data.interfaces.FugitivoDao

/**
 * Created by rdelgado on 5/8/2018.
 */
@Database(entities = [Fugitivo::class, Log::class], version = DroidBountyHunterDatabase.VERSION, exportSchema = false )
@TypeConverters(TimestampConverter::class)
abstract class DroidBountyHunterDatabase : RoomDatabase(){

    companion object {
        const val VERSION = 1

        private var INSTANCE: DroidBountyHunterDatabase? = null

        private val sLock = Any()

        fun getInstance(context: Context?): DroidBountyHunterDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context!!.applicationContext,
                            DroidBountyHunterDatabase::class.java, "DroidBountyHunterDataBase.db")
                            .fallbackToDestructiveMigration()
                            .build()
                }
                return INSTANCE
            }
        }
    }

    abstract fun fugitivoDao(): FugitivoDao
}