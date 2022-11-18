package hk.hkucs.comp3330_project

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
    private lateinit var searchView: SearchView
    private lateinit var sortImageButton: ImageButton
    private var sortExpAscending : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  dummy data
        val imageId = intArrayOf(
            R.drawable.dummy_image,
            R.drawable.egg_image,
            R.drawable.dummy_image,
            R.drawable.egg_image,
            R.drawable.dummy_image,
            R.drawable.egg_image,
            R.drawable.dummy_image,
            R.drawable.egg_image,
            R.drawable.dummy_image,
        )

        val itemName = arrayOf(
            "Meiji milk",
            "Eggs",
            "Meiji milk",
            "Eggs",
            "Meiji milk",
            "Eggs",
            "Meiji milk",
            "Eggs",
            "Meiji milk"
        )

        val expiryDate = arrayOf(
            "22/12/22",
            "10/11/22",
            "22/12/22",
            "10/11/22",
            "22/12/22",
            "10/11/22",
            "22/12/22",
            "10/11/22",
            "22/12/22",
        )

        itemArrayList = ArrayList()
        for (i in itemName.indices){
            val item = Item(itemName = itemName[i], expiryDate = expiryDate[i], imageURI = "",
                category = "", reminder = "", notes = "")
            itemArrayList.add(item)
        }

        val customItemAdapter = ItemAdapter(this, itemArrayList)
        binding.listview.isClickable = true
        binding.listview.adapter = customItemAdapter
        binding.listview.setOnItemClickListener{ parent, view, position, id ->
            val name = itemArrayList[position].itemName
            val exp = itemArrayList[position].expiryDate
            val imageURI = itemArrayList[position].imageURI

            val i = Intent(this, ManualInputActivity::class.java)
            i.putExtra("name", name)
            i.putExtra("exp", exp)
            i.putExtra("imageURI", imageURI)
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