package hk.hkucs.comp3330_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import hk.hkucs.comp3330_project.databinding.ActivityCategoryItemsBinding


class CategoryItemsActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityCategoryItemsBinding
    private lateinit var itemArrayList : ArrayList<Item>
    private lateinit var searchView: SearchView
    private lateinit var sortImageButton: ImageButton
    private lateinit var backButton: ImageButton
    private var sortExpAscending : Boolean = true
    private var dbhelper: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbhelper = DBHelper(this, null)

        val bundle: Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                val categoryName: String? = getString("categoryName")
                categoryName?.let {
                    updateCategoryName(categoryName)
                    itemArrayList = getItemsByCategory(categoryName)
                }
            }
        }

        val customItemAdapter = ItemAdapter(this, itemArrayList)
        binding.listview.isClickable = true
        binding.listview.adapter = customItemAdapter
        binding.listview.setOnItemClickListener{ parent, view, position, id ->
            val i = Intent(this, ManualInputActivity::class.java)
            i.putExtra("itemID", itemArrayList[position].itemID)
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

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }

        sortImageButton = findViewById(R.id.sort_image_button)
        sortImageButton.setOnClickListener{
            sortExpAscending = !sortExpAscending
            if (sortExpAscending) sortImageButton.setImageResource(R.drawable.keyboard_arrow_up)
            else sortImageButton.setImageResource(R.drawable.keyboard_arrow_down)

            customItemAdapter.sortByExpiryDate(sortExpAscending)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setSelectedItemId(R.id.categories)

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

    private fun updateCategoryName(name: String){
        val categoryHeader : TextView = findViewById(R.id.header_name)
        categoryHeader.text = name
    }

    private fun getItemsByCategory(category: String): ArrayList<Item>{
        var itemArrayList: ArrayList<Item> = ArrayList()
        val cursor = dbhelper?.getItemsByCategory(category)

        if(cursor != null && cursor.moveToFirst()){
            val itemID = cursor.getString(cursor.getColumnIndexOrThrow("ID"))
            val itemName = cursor.getString(cursor.getColumnIndexOrThrow("itemName"))
            val expiryDate = cursor.getString(cursor.getColumnIndexOrThrow("expiryDate"))
            val imageURI = cursor.getString(cursor.getColumnIndexOrThrow("imageURI"))
            val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
            val notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"))
            val reminder = cursor.getString(cursor.getColumnIndexOrThrow("reminder"))

            var item = Item(itemID = itemID, itemName = itemName, expiryDate = expiryDate, imageURI = imageURI,
                category = category, reminder = reminder, notes = notes)
            itemArrayList.add(item)
            // moving cursor to next position and log next values
            while(cursor.moveToNext()){
                var itemID = cursor.getString(cursor.getColumnIndexOrThrow("ID"))
                var itemName = cursor.getString(cursor.getColumnIndexOrThrow("itemName"))
                var expiryDate = cursor.getString(cursor.getColumnIndexOrThrow("expiryDate"))
                var imageURI = cursor.getString(cursor.getColumnIndexOrThrow("imageURI"))
                var category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                var notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"))
                var reminder = cursor.getString(cursor.getColumnIndexOrThrow("reminder"))

                var item = Item(itemID = itemID, itemName = itemName, expiryDate = expiryDate, imageURI = imageURI,
                    category = category, reminder = reminder, notes = notes)
                itemArrayList.add(item)
            }

            // close cursor
            cursor.close()

        }
        return itemArrayList
    }
}