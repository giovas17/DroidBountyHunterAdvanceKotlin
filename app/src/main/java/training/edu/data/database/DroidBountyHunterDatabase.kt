package training.edu.data.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
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
        const val VERSION = 4

        private var INSTANCE: DroidBountyHunterDatabase? = null

        private val sLock = Any()

        private const val TABLE_NAME = "fugitivos"
        private const val COLUMN_NAME_NAME = "name"
        private const val TABLE_NAME_LOG = "Log"
        private const val COLUMN_NAME_DATE = "date"
        private const val COLUMN_NAME_STATUS = "status"
        private const val COLUMN_NAME_NOTIFICATION = "notification"

        fun getInstance(context: Context?): DroidBountyHunterDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context!!.applicationContext,
                            DroidBountyHunterDatabase::class.java, "DroidBountyHunterDataBase.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(CALLBACK)
                            .addMigrations(MIGRATION_3_4)
                            .build()
                }
                return INSTANCE
            }
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                createTrigger(db)
            }
        }

        private val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                createTrigger(db)
            }
        }

        private fun createTrigger(db: SupportSQLiteDatabase){
            db.execSQL("CREATE TRIGGER LogEliminacion Before DELETE ON " + TABLE_NAME +
                    " FOR EACH ROW " +
                    "BEGIN " +
                    "INSERT INTO " + TABLE_NAME_LOG + "(" + COLUMN_NAME_NAME + "," +
                    COLUMN_NAME_DATE + "," + COLUMN_NAME_STATUS + "," + COLUMN_NAME_NOTIFICATION + ")" +
                    " VALUES(old.name, datetime('now'), old.status, old.notification); " +
                    "END")
        }
    }

    abstract fun fugitivoDao(): FugitivoDao

    abstract fun logDao(): LogDao
}