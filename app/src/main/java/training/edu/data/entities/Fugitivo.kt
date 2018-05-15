package training.edu.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by rdelgado on 5/4/2018.
 */
@Entity(tableName = "fugitivos")
data class Fugitivo constructor(
        @PrimaryKey(autoGenerate = true) var id: Int? = null,
        var name: String,
        var status: String,
        var photo: String)

