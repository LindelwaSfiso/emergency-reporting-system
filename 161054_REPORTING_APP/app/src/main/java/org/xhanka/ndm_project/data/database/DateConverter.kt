package org.xhanka.ndm_project.data.database

import androidx.room.TypeConverter
import java.util.*


/**
 * #1. Converter class, used for converting Date objects to Long since we can not populate database with
 * Date objects.
 *
 * #2. Second approach, save Date objects as Strings ::
 * SimpleDateFormat("EEE d MMM yyyy", Locale.ENGLISH).format(Date())
 */

class DateConverter {
    @TypeConverter
    fun toDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}