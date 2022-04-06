package com.ppb.grimoire.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Schedule(
    var title: String
) : Parcelable