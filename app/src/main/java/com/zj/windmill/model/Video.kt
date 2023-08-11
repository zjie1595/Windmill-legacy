package com.zj.windmill.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDateTime

@Entity(
    indices = [Index(value = ["title"], unique = true)]
)
@Parcelize
data class Video(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    val title: String,
    val coverImageUrl: String,
    val detailPageUrl: String,
    var contentPosition: Long? = null,
    var contentDuration: Long? = null,
    var watchTime: LocalDateTime? = null,
    var playlistIndex: Int = 0,
    var episodeIndex: Int = 0
) : Parcelable
