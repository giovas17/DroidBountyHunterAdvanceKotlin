package training.edu.data.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.sql.Date

/**
 * Created by rdelgado on 5/8/2018.
 */
@Entity(tableName = "Log")
data class Log constructor(
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "date") var date: Date)
{
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}
