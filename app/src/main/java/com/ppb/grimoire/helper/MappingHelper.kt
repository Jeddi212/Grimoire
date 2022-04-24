package com.ppb.grimoire.helper

import android.database.Cursor
import com.ppb.grimoire.db.DatabaseContract
import com.ppb.grimoire.model.Note
import com.ppb.grimoire.model.NoteElement
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

                val personId = getString(
                    getColumnIndexOrThrow(
                        DatabaseContract.NoteColumns.PERSON_ID
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
                        personId,
                        title,
                        description,
                        date
                    )
                )
            }
        }
        return notesList
    }

    fun mapCursorElementToArrayList(notesCursor: Cursor?): ArrayList<NoteElement> {
        val elementList = ArrayList<NoteElement>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(
                    getColumnIndexOrThrow(
                        DatabaseContract.ElementColumns._ID
                    )
                )

                val personId = getString(
                    getColumnIndexOrThrow(
                        DatabaseContract.ElementColumns.PERSON_ID
                    )
                )

                val str = getString(
                    getColumnIndexOrThrow(
                        DatabaseContract.ElementColumns.STR
                    )
                )

                val type = getString(
                    getColumnIndexOrThrow(
                        DatabaseContract.ElementColumns.TYPE
                    )
                )

                val pos = getInt(
                    getColumnIndexOrThrow(
                        DatabaseContract.ElementColumns.POS
                    )
                )

                val noteId = getInt(
                    getColumnIndexOrThrow(
                        DatabaseContract.ElementColumns.NOTE_ID
                    )
                )

                elementList.add(
                    NoteElement(
                        id,
                        personId,
                        str,
                        type,
                        pos,
                        noteId
                    )
                )
            }
        }
        return elementList
    }
}