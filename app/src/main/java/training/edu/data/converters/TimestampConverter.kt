package training.edu.data.converters

import android.arch.persistence.room.TypeConverter
import java.sql.Date


/**
 * Created by rdelgado on 5/3/2018.
 */
class TimestampConverter {

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toLong(value: Date?): Long? {
        return value?.time
    }
}