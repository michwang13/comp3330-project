package hk.hkucs.comp3330_project

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CategoryAdapter(private val context : Activity, private val arrayList: ArrayList<Category>) : ArrayAdapter<Category>(context, R.layout.list_item, arrayList) {
    private var tempCategoryList = ArrayList(arrayList)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.list_category, null)

        val imageView : ImageView = view.findViewById((R.id.categoryPic))
        val categoryName : TextView = view.findViewById(R.id.categoryName)
        val categorySize : TextView = view.findViewById(R.id.categorySize)

        imageView.setImageResource(arrayList[position].imageId)
        categoryName.text = arrayList[position].categoryName
        categorySize.text = arrayList[position].categorySize.toString() + " item(s)"

        return view
    }

    fun filterName(name: String?) {
        val text = name!!.lowercase()
        arrayList.clear()
        if (text.isEmpty()) {
            arrayList.addAll(tempCategoryList)
        } else {
            for (i in 0..tempCategoryList.size - 1) {
                if (tempCategoryList[i].categoryName.lowercase().contains(text)) {
                    arrayList.add(tempCategoryList.get(i))
                }
            }
        }
        notifyDataSetChanged()
    }
}