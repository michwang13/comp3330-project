package hk.hkucs.comp3330_project

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import hk.hkucs.comp3330_project.databinding.ActivityCategoriesBinding
import kotlin.collections.ArrayList

class CategoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var categoryArrayList : ArrayList<Category>
    private lateinit var searchView: SearchView
    private var dbhelper: DBHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbhelper = DBHelper(this, null)
        val res: Resources = resources
        val categoriesName = res.getStringArray(R.array.categories_array)

        val imageId = arrayOf(
            R.drawable.meat,
            R.drawable.snacks,
            R.drawable.dairy,
            R.drawable.baked_goods,
            R.drawable.others
        )
        categoryArrayList = ArrayList()
        for (i in categoriesName.indices){
            val categoriesItems = getItemsByCategory(categoriesName[i])
            val category = Category(categoryName = categoriesName[i], categorySize = categoriesItems.size, imageId = imageId[i], categoryItems = categoriesItems)
            categoryArrayList.add(category)
        }

        val customCategoryAdapter = CategoryAdapter(this, categoryArrayList)
        binding.listview.isClickable = true
        binding.listview.adapter = customCategoryAdapter
        binding.listview.setOnItemClickListener{ parent, view, position, id ->
            val name = categoryArrayList[position].categoryName

            val i = Intent(this, CategoryItemsActivity::class.java)
            i.putExtra("categoryName", name)
            startActivity(i)
        }

        searchView = findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                customCategoryAdapter.filterName(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                customCategoryAdapter.filterName(newText)
                return false
            }
        })

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
    fun getItemsByCategory(category: String): ArrayList<Item>{
        val itemArrayList: ArrayList<Item> = ArrayList()
        val cursor = dbhelper?.getItemsByCategory(category)

        if(cursor != null && cursor.moveToFirst()){
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

            // moving cursor to next position and log next values
            while(cursor.moveToNext()){
                itemID = cursor.getString(cursor.getColumnIndexOrThrow("ID"))
                itemName = cursor.getString(cursor.getColumnIndexOrThrow("itemName"))
                expiryDate = cursor.getString(cursor.getColumnIndexOrThrow("expiryDate"))
                imageURI = cursor.getString(cursor.getColumnIndexOrThrow("imageURI"))
                category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"))
                reminder = cursor.getString(cursor.getColumnIndexOrThrow("reminder"))

                item = Item(itemID = itemID, itemName = itemName, expiryDate = expiryDate, imageURI = imageURI,
                    category = category, reminder = reminder, notes = notes)
                itemArrayList.add(item)
            }

            // close cursor
            cursor.close()
        }
        return itemArrayList
    }
}