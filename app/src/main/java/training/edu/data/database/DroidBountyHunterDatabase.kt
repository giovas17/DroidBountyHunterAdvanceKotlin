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
import training.edu.data.interfaces.LogDao

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

        private const val TABLE_NAME = "fugitivos"
        private const val COLUMN_NAME_NAME = "name"
        private const val TABLE_NAME_LOG = "Log"
        private const val COLUMN_NAME_DATE = "date"

        fun getInstance(context: Context?): DroidBountyHunterDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context!!.applicationContext,
                            DroidBountyHunterDatabase::class.java, "DroidBountyHunterDataBase.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(CALLBACK)
                            .build()
                }
                return INSTANCE
            }
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                db.execSQL("CREATE TRIGGER LogEliminacion Before DELETE ON " + TABLE_NAME +
                        " FOR EACH ROW " +
                        "BEGIN " +
                        "INSERT INTO " + TABLE_NAME_LOG + "(" + COLUMN_NAME_NAME + "," +
                        COLUMN_NAME_DATE + ")" +
                        " VALUES(old.name, datetime('now')); " +
                        "END")
            }
        }
    }

    abstract fun fugitivoDao(): FugitivoDao

    abstract fun logDao(): LogDao
}