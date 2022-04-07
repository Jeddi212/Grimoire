package com.ppb.grimoire.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.ppb.grimoire.db.DatabaseContract.ScheduleColumns.Companion.TABLE_NAME
import com.ppb.grimoire.db.DatabaseContract.ScheduleColumns.Companion._ID
import java.sql.SQLException

class ScheduleHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: ScheduleHelper? = null

        /**
         * Metode untuk menginisiasi Database
         */
        fun getInstance(context: Context): ScheduleHelper = INSTANCE?: synchronized(this) {
            INSTANCE?: ScheduleHelper(context)
        }

        private lateinit var database: SQLiteDatabase
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    //------------------------------ KONEKSI DATABASE
    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen) database.close()
    }
    //-----------------------------------------------

    //--------------------------------- CRUD DATABASE
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun queryByDate(date: String, personId: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "date = ? AND person_id = ?",
            arrayOf(date, personId),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        Log.i("JEDDI", "idnya di update method ::: $id")
        Log.i("JEDDI", "Content values ::: $values")

        // TODO, ERROR UPDATE DISINI RUPANYA WOY
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = $id", null)
    }
    //-----------------------------------------------

}