package com.ppb.grimoire.helper

import android.database.Cursor
import com.ppb.grimoire.db.DatabaseContract
import com.ppb.grimoire.model.Note
import com.ppb.grimoire.model.Schedule

object MappingHelper {
    fun mapCursorToArrayList(scheduleCursor: Cursor?): ArrayList<Schedule> {
        val scheduleList = ArrayList<Schedule>()

        scheduleCursor?.apply {
            while (moveToNext()) {
                val id = getInt(
                    getColumnIndexOrThrow(
                        DatabaseContract.ScheduleColumns._ID
                    )
                )

                val personId = getString(
                    getColumnIndexOrThrow(
                        DatabaseContract.ScheduleColumns.PERSON_ID
                    )
                )

                val title = getString(
                    getColumnIndexOrThrow(
                        DatabaseContract.ScheduleColumns.TITLE
                    )
                )

                val date = getString(
                    getColumnIndexOrThrow(
                        DatabaseContract.ScheduleColumns.DATE
                    )
                )

                scheduleList.add(
                    Schedule(
                        id,
                        personId,
                        title,
                        date
                    )
                )
            }
        }
        return scheduleList
    }

    fun mapCursorNoteToArrayList(notesCursor: Cursor?): ArrayList<Note> {
        val notesList = ArrayList<Note>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(
                    getColumnIndexOrThrow(
                        DatabaseContract.NoteColumns._ID
                    )
                )

                val title = getString(
                    getColumnIndexOrThrow(
                        DatabaseContract.NoteColumns.TITLE
                    )
                )

                val description = getString(
                    getColumnIndexOrThrow(
                        DatabaseContract.NoteColumns.DESCRIPTION
                    )
                )

                val date = getString(
                    getColumnIndexOrThrow(
                        DatabaseContract.NoteColumns.DATE
                    )
                )

                notesList.add(
                    Note(
                        id,
                        title,
                        description,
                        date
                    )
                )
            }
        }
        return notesList
    }
}