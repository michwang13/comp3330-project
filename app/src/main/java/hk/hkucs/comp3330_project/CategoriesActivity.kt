package hk.hkucs.comp3330_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import hk.hkucs.comp3330_project.databinding.ActivityCategoriesBinding
import java.util.*
import kotlin.collections.ArrayList

class CategoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var categoryArrayList : ArrayList<Category>
    private lateinit var searchView: SearchView
//    private lateinit var editButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  dummy data
        val imageId = intArrayOf(
            R.drawable.dummy_image,
            R.drawable.egg_image,
            R.drawable.dummy_image,
            R.drawable.egg_image,
            R.drawable.dummy_image
        )

        val categoriesName = arrayOf(
            "Poultry",
            "Snacks",
            "Dairy",
            "Baked Goods",
            "Others"
        )

        val categoriesItems = arrayOf(
            arrayListOf<Item>(
                Item(itemName = "Meiji Milk", expiryDate = "22/12/22", imageURI = "", category = "Poultry", reminder = "", notes = ""),
                Item(itemName = "Egg", expiryDate = "21/12/22", imageURI = "", category = "Poultry", reminder = "", notes = ""),
                Item(itemName = "Meiji Milk", expiryDate = "20/12/22", imageURI = "", category = "Poultry", reminder = "", notes = ""),
                Item(itemName = "Egg", expiryDate = "19/12/22", imageURI = "", category = "Poultry", reminder = "", notes = ""),
                Item(itemName = "Meiji Milk", expiryDate = "10/12/22", imageURI = "", category = "Poultry", reminder = "", notes = ""),
            ),
            arrayListOf<Item>(
                Item(itemName = "Meiji Milk", expiryDate = "22/12/22", imageURI = "", category = "Snacks", reminder = "", notes = ""),
                Item(itemName = "Egg", expiryDate = "21/12/22", imageURI = "", category = "Snacks", reminder = "", notes = ""),
            ),
            arrayListOf<Item>(
                Item(itemName = "Meiji Milk", expiryDate = "22/12/22", imageURI = "", category = "Dairy", reminder = "", notes = ""),
            ),
            arrayListOf<Item>(
                Item(itemName = "Meiji Milk", expiryDate = "22/12/22", imageURI = "", category = "Baked Goods", reminder = "", notes = ""),
                Item(itemName = "Egg", expiryDate = "21/12/22", imageURI = "", category = "Baked Goods", reminder = "", notes = ""),
            ),
            arrayListOf<Item>(
                Item(itemName = "Meiji Milk", expiryDate = "22/12/22", imageURI = "", category = "Canned Foods", reminder = "", notes = ""),
                Item(itemName = "Egg", expiryDate = "21/12/22", imageURI = "", category = "Canned Foods", reminder = "", notes = ""),
                Item(itemName = "Meiji Milk", expiryDate = "22/12/22", imageURI = "", category = "Canned Foods", reminder = "", notes = ""),
            ),
        )

        categoryArrayList = ArrayList()
        for (i in categoriesName.indices){
            val category = Category(categoryName = categoriesName[i], categorySize = categoriesItems[i].size, imageId = imageId[i], categoryItems = categoriesItems[i])
            categoryArrayList.add(category)
        }

        val customCategoryAdapter = CategoryAdapter(this, categoryArrayList)
        binding.listview.isClickable = true
        binding.listview.adapter = customCategoryAdapter
        binding.listview.setOnItemClickListener{ parent, view, position, id ->
            val name = categoryArrayList[position].categoryName
            val items = categoryArrayList[position].categoryItems

            val i = Intent(this, CategoryItemsActivity::class.java)
            i.putExtra("categoryName", name)
            i.putExtra("categoryItems", items as java.io.Serializable)
//            i.putParcelableArrayListExtra("categoryItems", items)
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

//        editButton = findViewById(R.id.edit_button)
//        editButton.setOnClickListener{
//            var views : ArrayList<EditText> = ArrayList<EditText>()
//            for (i in 0..categoriesName.size - 1) {
//                views.add(findViewById(R.id.categoryName))
//                views[i].setFocusable(true)
//                views[i].setFocusableInTouchMode(true)
//                views[i].setBackgroundResource(android.R.drawable.edit_text)
//            }
//            var categoryName : EditText = findViewById(R.id.categoryName)
//            categoryName.setFocusable(true)
//            categoryName.setFocusableInTouchMode(true)
//            categoryName.setBackgroundResource(android.R.drawable.edit_text)
////            println("HELLO")
//        }
    }
}