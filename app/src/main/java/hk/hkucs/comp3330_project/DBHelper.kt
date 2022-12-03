package hk.hkucs.comp3330_project

import android.database.sqlite.SQLiteDatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import android.database.DatabaseUtils

import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*

class DBHelper(context: Context?, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "DATABASE"
        private val DATABASE_VERSION = 1
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY =
            ("CREATE TABLE ITEMS" + " (ID TEXT, " +
                "itemName TEXT, " +
                "notes TEXT, " +
                "category TEXT, " +
                "expiryDate TEXT, " +
                "imageURI TEXT, " +
                "reminder TEXT, " +
                "notifID INTEGER PRIMARY KEY AUTOINCREMENT)")

        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO Auto-generated method stub
//        db.execSQL("DROP TABLE IF EXISTS items")
//        onCreate(db)
    }

    fun insertItem(item: Item) : Boolean{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        val uniqueID = UUID.randomUUID().toString()
        contentValues.put("ID", uniqueID)
        contentValues.put("itemName", item.itemName)
        contentValues.put("notes", item.notes)
        contentValues.put("category", item.category)
        contentValues.put("expiryDate", item.expiryDate)
        contentValues.put("reminder", item.reminder)
        contentValues.put("imageURI", item.imageURI)
        db.insert("items", null, contentValues)
        return true
    }

    fun getAllItems(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM ITEMS", null)
    }

    fun getOneItemByID(id: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM ITEMS WHERE id='$id'", null)
    }

    fun getItemsByCategory(category: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM ITEMS WHERE category='$category'", null)
    }

    fun deleteOneItemByID(id: String) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM ITEMS WHERE id='$id'")
    }

    fun updateOneItemByID(id: String, item: Item) {
        val contentValues = ContentValues()
        contentValues.put("ID", item.itemID)
        contentValues.put("itemName", item.itemName)
        contentValues.put("notes", item.notes)
        contentValues.put("category", item.category)
        contentValues.put("expiryDate", item.expiryDate)
        contentValues.put("reminder", item.reminder)
        contentValues.put("imageURI",item.imageURI)

        val db = this.writableDatabase
        db.update("items", contentValues, "ID='$id'", arrayOf())
    }
}