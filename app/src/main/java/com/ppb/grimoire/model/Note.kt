package com.ppb.grimoire.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    var id: Int = 0,
    var personId: String? = null,
    var title: String? = null,
    var description: String? = null,
    var date: String? = null
) : Parcelable