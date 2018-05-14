package training.edu.data.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by rdelgado on 5/4/2018.
 */
@Entity(tableName = "fugitivos")
data class Fugitivo constructor(
        @PrimaryKey(autoGenerate = true) var id: Int? = null,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "status") var status: String,
        @ColumnInfo(name = "photo") var photo: String,
        @ColumnInfo(name = "notification") var notification: Int)

