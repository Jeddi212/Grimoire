package com.ppb.grimoire.helper

import android.database.Cursor
import com.ppb.grimoire.db.DatabaseContract
import com.ppb.grimoire.model.Schedule

object MappingHelper {
    fun mapCursorToArrayList(scheduleCursor: Cursor?): ArrayList<Schedule> {
        val scheduleList = ArrayList<Schedule>()

        scheduleCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.ScheduleColumns._ID))
                val personId = getString(getColumnIndexOrThrow(DatabaseContract.ScheduleColumns.PERSON_ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.ScheduleColumns.TITLE))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.ScheduleColumns.DATE))
                scheduleList.add(Schedule(id, personId, title, date))
            }
        }
        return scheduleList
    }
}