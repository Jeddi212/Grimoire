package com.ppb.grimoire.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteElement(
    var id: Int = 0,
    var personId: String? = null,
    var str: String? = null,
    var type: String = "",
    var pos: Int = 0,
    var noteId: Int = 0,
) : Parcelable
