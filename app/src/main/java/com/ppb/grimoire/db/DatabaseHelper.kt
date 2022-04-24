package com.ppb.grimoire.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ppb.grimoire.db.DatabaseContract.ElementColumns.Companion.TABLE_NAME_ELEMENTS
import com.ppb.grimoire.db.DatabaseContract.ScheduleColumns.Companion.TABLE_NAME
import com.ppb.grimoire.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME_NOTE

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_grimoire"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_SCHEDULES = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.ScheduleColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.ScheduleColumns.PERSON_ID} TEXT NOT NULL," +
                " ${DatabaseContract.ScheduleColumns.TITLE} TEXT NOT NULL," +
                " ${DatabaseContract.ScheduleColumns.DATE} TEXT NOT NULL)"


        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME_NOTE" +
                " (${DatabaseContract.NoteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.NoteColumns.PERSON_ID} TEXT NOT NULL," +
                " ${DatabaseContract.NoteColumns.TITLE} TEXT NOT NULL," +
                " ${DatabaseContract.NoteColumns.DESCRIPTION} TEXT NOT NULL," +
                " ${DatabaseContract.NoteColumns.DATE} TEXT NOT NULL)"

        private const val SQL_CREATE_TABLE_ELEMENTS = "CREATE TABLE $TABLE_NAME_ELEMENTS" +
                " (${DatabaseContract.NoteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.ElementColumns.PERSON_ID} TEXT NOT NULL," +
                " ${DatabaseContract.ElementColumns.STR} TEXT NOT NULL," +
                " ${DatabaseContract.ElementColumns.TYPE} TEXT NOT NULL," +
                " ${DatabaseContract.ElementColumns.POS} INTEGER NOT NULL," +
                " ${DatabaseContract.ElementColumns.NOTE_ID} INTEGER NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_SCHEDULES)
        db.execSQL(SQL_CREATE_TABLE_NOTE)
        db.execSQL(SQL_CREATE_TABLE_ELEMENTS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_NOTE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ELEMENTS")
        onCreate(db)
    }

}