package com.ppb.grimoire.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.ppb.grimoire.db.DatabaseContract.ElementColumns.Companion.POS
import com.ppb.grimoire.db.DatabaseContract.ElementColumns.Companion.TABLE_NAME_ELEMENTS
import com.ppb.grimoire.db.DatabaseContract.ElementColumns.Companion._ID
import java.sql.SQLException

class ElementHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME_ELEMENTS
        private lateinit var dataBaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase

        private var INSTANCE: ElementHelper? = null

        /**
         * Metode untuk menginisiasi Database
         */
        fun getInstance(context: Context): ElementHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ElementHelper(context)
        }
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

        if (database.isOpen)
            database.close()
    }
    //-----------------------------------------------

    //--------------------------------- CRUD DATABASE
    fun queryAll(personId: String, noteId: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "person_id = ? AND note_id = ?",
            arrayOf(personId, noteId),
            null,
            null,
            "$POS ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
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
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
    //-----------------------------------------------
}