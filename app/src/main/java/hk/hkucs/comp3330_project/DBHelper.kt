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
        private val DATABASE_NAME = "TEST2"
    // below is the variable for database version
        private val DATABASE_VERSION = 1
        const val CONTACTS_TABLE_NAME = "items"
        const val CONTACTS_COLUMN_ID = "id"
        const val CONTACTS_COLUMN_NAME = "itemName"
        const val CONTACTS_COLUMN_NOTES = "notes"
        const val CONTACTS_COLUMN_CATEGORY = "category"
        const val CONTACTS_COLUMN_EXP = "expiryDate"
        const val CONTACTS_COLUMN_PHONE = "reminder"
        const val CONTACTS_COLUMN_URI = "imageURI"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("TAG","TESTTTTT------")
        val CREATE_TABLE_QUERY =
            ("CREATE TABLE ITEMS" + " (ID TEXT PRIMARY KEY, " +
                "itemName TEXT, " +
                "notes TEXT, " +
                "category TEXT, " +
                "expiryDate TEXT, " +
                "imageURI TEXT, " +
                "reminder TEXT)")
//            "create table items (id text primary key, itemName text, notes text, category text, expiryDate text, reminder text)"

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
        // create a readable variable of the database
        val db = this.readableDatabase
        // returns a cursor to read data from the database
        return db.rawQuery("SELECT * FROM ITEMS", null)
    }

    fun getOneItemByID(id: String): Cursor? {
        // create a readable variable of the database
        val db = this.readableDatabase
        // returns a cursor to read data from the database
        return db.rawQuery("SELECT * FROM ITEMS WHERE id='$id'", null)
    }

    fun getItemsByCategory(category: String): Cursor? {
        // create a readable variable of the database
        val db = this.readableDatabase
        // returns a cursor to read data from the database
        return db.rawQuery("SELECT * FROM ITEMS WHERE category='$category'", null)
    }

    fun deleteOneItemByID(id: String) {
        // create a writable variable of the database
        val db = this.writableDatabase
        db.execSQL("DELETE FROM ITEMS WHERE id='$id'")
    }

    fun updateOneItemByID(id: String, item: Item) {
        // first check if items already in list by getAllItem, return the id
        val contentValues = ContentValues()
        contentValues.put("ID", item.itemID)
        contentValues.put("itemName", item.itemName)
        contentValues.put("notes", item.notes)
        contentValues.put("category", item.category)
        contentValues.put("expiryDate", item.expiryDate)
        contentValues.put("reminder", item.reminder)

        val db = this.writableDatabase
        db.update("items", contentValues, "ID='$id'", arrayOf())
    }

    // ---------- CODE WANG -------
//    fun getData(id:String): Cursor{
//        val db = this.readableDatabase
//        return db.rawQuery("select * from items where id=$id", null)
//    }

    // why does it return int?? <may not be correct>
//    fun deleteContact(id: String?): Int {
//        val db = this.writableDatabase
//        return db.delete(
//            "items",
//            "id = ? ", arrayOf(id!!)
//        )
//    }

    // dummy function to test, can delete later
//    fun queryData(): Cursor{
//        val db = this.readableDatabase
////        return db.rawQuery("select * from items", null);
//        return db.rawQuery("select * from items where itemName='test'", null);
//
//    }

//    fun numberOfRows(): Int {
//        val db = this.readableDatabase
//        return DatabaseUtils.queryNumEntries(db, DBHelper.Companion.CONTACTS_TABLE_NAME).toInt()
//    }

    // Below are sample queries that could be used
//    fun updateContact(
//        id: Int?,
//        name: String?,
//        phone: String?,
//        email: String?,
//        street: String?,
//        place: String?
//    ): Boolean {
//        val db = this.writableDatabase
//        val contentValues = ContentValues()
//        contentValues.put("name", name)
//        contentValues.put("phone", phone)
//        contentValues.put("email", email)
//        contentValues.put("street", street)
//        contentValues.put("place", place)
//        db.update(
//            "contacts", contentValues, "id = ? ", arrayOf(
//                Integer.toString(
//                    id!!
//                )
//            )
//        )
//        return true
//    }

//    fun deleteContact(id: Int?): Int {
//        val db = this.writableDatabase
//        return db.delete(
//            "contacts",
//            "id = ? ", arrayOf(Integer.toString(id!!))
//        )
//    }

    //hp = new HashMap();
//    val allCotacts: ArrayList<String>
//        get() {
//            val array_list = ArrayList<String>()
//
//            //hp = new HashMap();
//            val db = this.readableDatabase
//            val res: Cursor = db.rawQuery("select * from contacts", null)
//            res.moveToFirst()
//            while (res.isAfterLast() === false) {
//                array_list.add(res.getString(res.getColumnIndex(DBHelper.Companion.CONTACTS_COLUMN_NAME)))
//                res.moveToNext()
//            }
//            return array_list
//        }
}