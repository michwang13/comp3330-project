package hk.hkucs.comp3330_project

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import hk.hkucs.comp3330_project.databinding.ActivityListPageBinding

class ListPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListPageBinding
    private lateinit var itemArrayList : ArrayList<Item>
    private lateinit var notifIDList : ArrayList<String>
    private lateinit var searchView: SearchView
    private lateinit var sortImageButton: ImageButton
    private var sortExpAscending : Boolean = true
    private var dbhelper: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbhelper = DBHelper(this, null)
        itemArrayList = ArrayList()
        notifIDList = ArrayList()

        val cursor = dbhelper?.getAllItems()

        if(cursor != null && cursor.moveToFirst()){
            var itemID = cursor.getString(cursor.getColumnIndexOrThrow("ID"))
            var itemName = cursor.getString(cursor.getColumnIndexOrThrow("itemName"))
            var expiryDate = cursor.getString(cursor.getColumnIndexOrThrow("expiryDate"))
            var imageURI = cursor.getString(cursor.getColumnIndexOrThrow("imageURI"))
            var category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
            var notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"))
            var reminder = cursor.getString(cursor.getColumnIndexOrThrow("reminder"))
            var notifID = cursor.getString(cursor.getColumnIndexOrThrow("notifID"))

            var item = Item(itemID = itemID, itemName = itemName, expiryDate = expiryDate, imageURI = imageURI,
                category = category, reminder = reminder, notes = notes)
            itemArrayList.add(item)
            notifIDList.add(notifID)
            // moving cursor to next position and log next values
            while(cursor.moveToNext()){
                itemID = cursor.getString(cursor.getColumnIndexOrThrow("ID"))
                itemName = cursor.getString(cursor.getColumnIndexOrThrow("itemName"))
                expiryDate = cursor.getString(cursor.getColumnIndexOrThrow("expiryDate"))
                imageURI = cursor.getString(cursor.getColumnIndexOrThrow("imageURI"))
                category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"))
                reminder = cursor.getString(cursor.getColumnIndexOrThrow("reminder"))
                notifID = cursor.getString(cursor.getColumnIndexOrThrow("notifID"))

                 item = Item(itemID = itemID, itemName = itemName, expiryDate = expiryDate, imageURI = imageURI,
                    category = category, reminder = reminder, notes = notes)
                itemArrayList.add(item)
                notifIDList.add(notifID)
            }

            // close cursor
            cursor.close()

        }

        val customItemAdapter = ItemAdapter(this, itemArrayList)
        binding.listview.isClickable = true
        binding.listview.adapter = customItemAdapter
        binding.listview.setOnItemClickListener{ parent, view, position, id ->
            val i = Intent(this, ManualInputActivity::class.java)
            i.putExtra("itemID", itemArrayList[position].itemID)
            i.putExtra("notifID", notifIDList[position])
            startActivity(i)
        }

        customItemAdapter.sortByExpiryDate(sortExpAscending)
        searchView = findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                customItemAdapter.filterName(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                customItemAdapter.filterName(newText)
                return false
            }
        })

        sortImageButton = findViewById(R.id.sort_image_button)
        sortImageButton.setOnClickListener{
            sortExpAscending = !sortExpAscending
            if (sortExpAscending) sortImageButton.setImageResource(R.drawable.keyboard_arrow_up)
            else sortImageButton.setImageResource(R.drawable.keyboard_arrow_down)

            customItemAdapter.sortByExpiryDate(sortExpAscending)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setSelectedItemId(R.id.items)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.items -> {
                    startActivity(Intent(this, ListPageActivity::class.java))
                }
                R.id.scan -> {
                    startActivity(Intent(this, ListPageActivity::class.java))
                }
                R.id.categories -> {
                    startActivity(Intent(this, CategoriesActivity::class.java))
                }
            }
            true
        }
    }
}