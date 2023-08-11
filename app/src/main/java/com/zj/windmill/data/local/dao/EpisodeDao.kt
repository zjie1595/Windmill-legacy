package com.zj.windmill.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zj.windmill.model.Episode

@Dao
interface EpisodeDao {

    @Query("select * from episode where playPageUrl = :playPageUrl")
    suspend fun getEpisode(playPageUrl: String): Episode?

    @Query("select * from episode where id = :id")
    suspend fun getById(id: Long): Episode?

    @Insert
    suspend fun insert(episode: Episode): Long
}