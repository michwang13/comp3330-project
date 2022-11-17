package hk.hkucs.comp3330_project

import android.content.Intent
import android.os.Bundle
import android.view.View
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
    private var sortExpAscending : Boolean = true

//    private lateinit var categoryHeader: TextView
//    private var categoryNameHeader: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                val categoryName: String? = getString("categoryName")
                categoryName?.let {
                    updateCategoryName(categoryName)
                }

                val categoryItems = getSerializable("categoryItems") as ArrayList<Item>?
                categoryItems?.let {
                    updateCategoryItems(categoryItems)
                }
            }
        }

        val customItemAdapter = ItemAdapter(this, itemArrayList)
        binding.listview.isClickable = true
        binding.listview.adapter = customItemAdapter
        binding.listview.setOnItemClickListener{ parent, view, position, id ->
            val name = itemArrayList[position].itemName
            val exp = itemArrayList[position].expiryDate
            val imgId = itemArrayList[position].imageId

            val i = Intent(this, ManualInputActivity::class.java)
            i.putExtra("name", name)
            i.putExtra("exp", exp)
            i.putExtra("imgId", imgId)
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

    private fun updateCategoryItems(items: ArrayList<Item>){
        itemArrayList = ArrayList()
        for (i in items){
            itemArrayList.add(i)
        }
    }
}