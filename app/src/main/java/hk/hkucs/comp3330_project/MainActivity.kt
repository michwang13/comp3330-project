package hk.hkucs.comp3330_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity() {
    private var dbhelper: DBHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbhelper = DBHelper(this, null)

//        populate db if empty
        val cursor = dbhelper?.getAllItems()

        if(!cursor!!.moveToFirst()){
            val itemName = arrayOf(
                "Meiji milk",
                "Eggs",
                "Banana milk",
                "Bread",
                "Chocolate",
                "Potato",
                "Chips",
                "Medicine",
                "Meat"
            )
            val categories = arrayOf(
                "Dairy",
                "Dairy",
                "Dairy",
                "Baked Goods",
                "Snack",
                "Snack",
                "Snack",
                "Others",
                "Meat",
            )

            val expiryDate = arrayOf(
                "22/12/2022",
                "10/12/2022",
                "20/12/2022",
                "13/11/2022",
                "2/12/2022",
                "20/11/2022",
                "29/11/2023",
                "30/12/2022",
                "22/12/2023",
            )

            for (i in itemName.indices){
                var uuid = UUID.randomUUID().toString()
                val item = Item(itemID = uuid, itemName = itemName[i], expiryDate = expiryDate[i], imageURI = "",
                    category = categories[i], reminder = "", notes = "")
                dbhelper?.insertItem(item)
            }
        }


        val i = Intent(this, ListPageActivity::class.java)
        startActivity(i)

    }
}