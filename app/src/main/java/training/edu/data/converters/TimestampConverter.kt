package training.edu.data.converters

import android.arch.persistence.room.TypeConverter
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by rdelgado on 5/3/2018.
 */
class TimestampConverter {

    companion object {
        private var df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    }

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        if (value != null) {
            try {
                return df.parse(value)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return null
        } else {
            return null
        }
    }

    @TypeConverter
    fun dateToTimestamp(value: Date?): String? {
        return if (value == null) null else df.format(value)
    }
}