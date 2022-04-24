package com.ppb.grimoire.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class ScheduleColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "schedules"
            const val _ID = "_id"
            const val PERSON_ID = "person_id"
            const val TITLE = "title"
            const val DATE = "date"
        }
    }

    internal class NoteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME_NOTE = "note"
            const val _ID = "_id"
            const val PERSON_ID = "person_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"
        }
    }

    internal class ElementColumns : BaseColumns {
        companion object {
            const val TABLE_NAME_ELEMENTS = "elements"
            const val _ID = "_id"
            const val PERSON_ID = "person_id"
            const val STR = "str"
            const val TYPE = "type"
            const val POS = "pos"
            const val NOTE_ID = "note_id"
        }
    }
}