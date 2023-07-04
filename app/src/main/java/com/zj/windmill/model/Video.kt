package com.zj.windmill.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val title: String,
    val coverImageUrl: String,
    val detailPageUrl: String
): Parcelable
