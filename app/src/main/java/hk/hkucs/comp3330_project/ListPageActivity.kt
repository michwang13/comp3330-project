package hk.hkucs.comp3330_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.view.LayoutInflater
import hk.hkucs.comp3330_project.databinding.ActivityPageBinding

class PageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPageBinding
    private lateinit var itemArrayList : ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageId = intArrayOf(
            R.drawable.dummy_image,
            R.drawable.egg_image

        )

        val itemName = arrayOf(
            "Meiji milk",
            "Eggs"
        )

        val expiryDate = arrayOf(
            "22/12/22",
            "10/11/22"
        )

        itemArrayList = ArrayList()


        for (i in itemName.indices){
            val item = Item(itemName = itemName[i], expiryDate = expiryDate[i], imageId = imageId[i],
                category = "", reminder = "", notes = "")
            itemArrayList.add(item)
        }

        binding.listview.isClickable = true
        binding.listview.adapter = ItemAdapter(this, itemArrayList)
        binding.listview.setOnItemClickListener{ parent, view, position, id ->
            val name = itemName[position]
            val exp = expiryDate[position]
            val imgId = imageId[position]

            val i = Intent(this, ManualInputActivity::class.java)
            i.putExtra("name", name)
            i.putExtra("exp", exp)
            i.putExtra("imgId", imgId)
            startActivity(i)

        }
        // first launch -> enter info
//        val i = Intent(this, ManualInputActivity::class.java)
//        startActivity(i)
    }
}