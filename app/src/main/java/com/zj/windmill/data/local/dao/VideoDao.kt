package com.zj.windmill.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zj.windmill.model.Video

@Dao
interface VideoDao {

    @Query("select * from video order by watchTime desc limit :limit offset :offset")
    fun getVideos(limit: Int, offset: Int): LiveData<List<Video>>

    @Query("select * from video where id = :videoId")
    suspend fun getById(videoId: Long): Video?

    @Query("select * from video where title = :title")
    suspend fun getByTitle(title: String): Video?

    @Query("select * from video where title like :keyword order by title limit :limit offset :offset")
    fun search(keyword: String, limit: Int, offset: Int): LiveData<List<Video>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: Video): Long

    @Delete
    suspend fun delete(vararg videos: Video)
}