package com.ppb.grimoire.model

data class NoteElement(
    var id: Int = 0,
    var str: String? = null,
    var type: String = "",
    var pos: Int = 0,
    var noteId: Int = 0,
)
