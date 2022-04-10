package com.ppb.grimoire.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    var title: String,
    var description: String,
    var photo: String,
    var url: String
) : Parcelable
