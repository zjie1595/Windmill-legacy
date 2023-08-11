package com.zj.windmill.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zj.windmill.data.local.dao.EpisodeDao
import com.zj.windmill.data.local.dao.VideoDao
import com.zj.windmill.model.Episode
import com.zj.windmill.model.Video

@Database(entities = [Video::class, Episode::class], version = 1, exportSchema = false)
@TypeConverters(AppTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    abstract fun episodeDao(): EpisodeDao
}