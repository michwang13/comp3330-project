package hk.hkucs.comp3330_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class DummyCrud: AppCompatActivity() {
    private var dbhelper: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy_crud)
        dbhelper = DBHelper(this, null)
    }

    fun onChangeIntentClicked(view: View) {
        val i = Intent(this, ManualInputActivity::class.java)
        startActivity(i)
    }

    fun onGetAllItemClicked(view: View) {
        val cursor = dbhelper?.getAllItems()

        cursor!!.moveToFirst()
        Log.d("TAG", cursor.getString(cursor.getColumnIndexOrThrow("ID")) + "\n")
        Log.d("TAG", cursor.getString(cursor.getColumnIndexOrThrow("itemName")) + "\n")
        Log.d("TAG",cursor.getString(cursor.getColumnIndexOrThrow("notes")) + "\n")

        // moving cursor to next position and log next values
        while(cursor.moveToNext()){
            Log.d("TAG", cursor.getString(cursor.getColumnIndexOrThrow("ID")) + "\n")
            Log.d("TAG", cursor.getString(cursor.getColumnIndexOrThrow("itemName")) + "\n")
            Log.d("TAG",cursor.getString(cursor.getColumnIndexOrThrow("notes")) + "\n")
        }

        // close cursor
        cursor.close()
    }

    fun onGetOneItemClicked(view: View) {
        // Change the id depending on whats inside the db
        val cursor = dbhelper?.getOneItemByID("869d277a-74e4-4778-9af8-1e739d166609")
        cursor!!.moveToFirst()
        Log.d("TAG", cursor.getString(cursor.getColumnIndexOrThrow("itemName")) + "\n")
        Log.d("TAG",cursor.getString(cursor.getColumnIndexOrThrow("notes")) + "\n")
        Log.d("TAG",cursor.getString(cursor.getColumnIndexOrThrow("category")) + "\n")
        cursor.close()
    }

    fun onDeleteOneItemClicked(view: View) {
        // Change the id depending on whats inside the db
        dbhelper?.deleteOneItemByID("9c32e8cc-2485-440d-b39e-4500b55f2653")
    }

    fun onGetItemsByCategoryClicked(view: View) {
        val cursor = dbhelper?.getItemsByCategory("Poultry")

        cursor!!.moveToFirst()
        Log.d("TAG", cursor.getString(cursor.getColumnIndexOrThrow("ID")) + "\n")
        Log.d("TAG", cursor.getString(cursor.getColumnIndexOrThrow("itemName")) + "\n")
        Log.d("TAG",cursor.getString(cursor.getColumnIndexOrThrow("category")) + "\n")

        // moving cursor to next position and log next values
        while(cursor.moveToNext()){
            Log.d("TAG", cursor.getString(cursor.getColumnIndexOrThrow("ID")) + "\n")
            Log.d("TAG", cursor.getString(cursor.getColumnIndexOrThrow("itemName")) + "\n")
            Log.d("TAG",cursor.getString(cursor.getColumnIndexOrThrow("category")) + "\n")
        }

        // close cursor
        cursor.close()
    }

    fun onUpdateOneClicked(view: View) {
        val updateItem = Item(
            "958ed99c-8e9d-42f4-bb77-577c760fa9ad",
            "Update",
            "Update Note",
            "Dairy",
            "Nov 18, 2022",
            "2 day"
        )
        dbhelper?.updateOneItemByID("958ed99c-8e9d-42f4-bb77-577c760fa9ad", updateItem)
    }
}