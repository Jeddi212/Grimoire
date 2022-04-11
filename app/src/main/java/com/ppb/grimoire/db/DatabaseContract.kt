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
            const val TABLE_NAME = "note"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"
        }
    }
}