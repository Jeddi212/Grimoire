package com.ppb.grimoire.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ppb.grimoire.db.DatabaseContract.ScheduleColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) :
SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "db_grimoire"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_SCHEDULES = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.ScheduleColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.ScheduleColumns.PERSON_ID} TEXT NOT NULL," +
                " ${DatabaseContract.ScheduleColumns.TITLE} TEXT NOT NULL," +
                " ${DatabaseContract.ScheduleColumns.DATE} TEXT NOT NULL)"


        private val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.NoteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.NoteColumns.TITLE} TEXT NOT NULL," +
                " ${DatabaseContract.NoteColumns.DESCRIPTION} TEXT NOT NULL," +
                " ${DatabaseContract.NoteColumns.DATE} TEXT NOT NULL)"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_SCHEDULES)

        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}