package com.zj.windmill.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episode(
    val title: String,
    val playPageUrl: String
) : Parcelable
