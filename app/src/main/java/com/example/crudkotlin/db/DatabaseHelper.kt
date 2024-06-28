package com.example.crudkotlin.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "table_test.db"
        private const val DATABASE_VERSION = 1

        // Tabla de usuarios
        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        // Tabla de personas
        private const val TABLE_CUSTOMERS = "customers"
        private const val COLUMN_PERSON_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_MESSAGE = "message"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableUsers = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT,
                $COLUMN_PASSWORD TEXT
            )
        """.trimIndent()

        val createTableCustomers = """
            CREATE TABLE $TABLE_CUSTOMERS (
                $COLUMN_PERSON_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_AGE INTEGER,
                $COLUMN_MESSAGE TEXT
            )
        """.trimIndent()

        db?.execSQL(createTableUsers)
        db?.execSQL(createTableCustomers)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOMERS")
        onCreate(db)
    }

    // Insertar customer
    fun insertUser(username: String, password: String): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val result = db.insert(TABLE_USERS, null, contentValues)
        return result != -1L
    }

    // Verificar customer
    fun checkUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?", arrayOf(username, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Insertar customer
    fun insertCustomer(name: String, age: Int,message:String): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_AGE, age)
            put(COLUMN_MESSAGE, message)
        }
        val result = db.insert(TABLE_CUSTOMERS, null, contentValues)
        return result != -1L
    }

    // Obtener todos los customers
    fun getAllCustomers(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_CUSTOMERS", null)
    }

    // Actualizar customer
    fun updateCustomer(id: Int, name: String, age: Int): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_AGE, age)
        }
        val result = db.update(TABLE_CUSTOMERS, contentValues, "$COLUMN_PERSON_ID = ?", arrayOf(id.toString()))
        return result > 0
    }

    // Eliminar customer
    fun deleteCustomer(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_CUSTOMERS, "$COLUMN_PERSON_ID = ?", arrayOf(id.toString()))
        return result > 0
    }
}