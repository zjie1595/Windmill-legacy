package com.zj.windmill.data.local

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime

class AppTypeConverter {

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String? {
        return localDateTime?.toString()
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }
}