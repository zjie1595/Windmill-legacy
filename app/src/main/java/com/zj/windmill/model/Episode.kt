package com.zj.windmill.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    indices = [Index(value = ["playPageUrl"], unique = true)]
)
@Parcelize
data class Episode(
    @PrimaryKey
    var id: Long? = null,
    val title: String,
    val playPageUrl: String,
    var contentPosition: Long? = null,
    var contentDuration: Long? = null,
    var selected: Boolean = false
) : Parcelable, BaseObservable()
