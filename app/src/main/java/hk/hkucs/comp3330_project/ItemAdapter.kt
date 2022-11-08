package hk.hkucs.comp3330_project

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ItemAdapter(private val context : Activity, private val arrayList: ArrayList<Item>) : ArrayAdapter<Item>(context,
                  R.layout.list_item, arrayList)    {

    private var tempItemList = ArrayList(arrayList)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.list_item, null)

        val imageView : ImageView = view.findViewById((R.id.itemPic))
        val itemName : TextView = view.findViewById(R.id.itemName)
        val expDate : TextView = view.findViewById(R.id.expiryDate)

        imageView.setImageResource(arrayList[position].imageId)
        itemName.text = arrayList[position].itemName
        expDate.text = arrayList[position].expiryDate

        return view
    }

    fun filterName(text: String?) {
        val text = text!!.lowercase()
        arrayList.clear()
        if (text.isEmpty()) {
            arrayList.addAll(tempItemList)
        } else {
            for (i in 0..tempItemList.size - 1) {
                if (tempItemList[i].itemName!!.lowercase().contains(text)) {
                    arrayList.add(tempItemList.get(i))
                }
            }
        }
        notifyDataSetChanged()
    }

//  might need to change later
    fun sortByExpiryDate(ascending: Boolean){
        arrayList.clear()
        if (ascending){
            tempItemList.sortBy { it.expiryDate }
        } else {
            tempItemList.sortByDescending { it.expiryDate }
        }
        arrayList.addAll(tempItemList)
        notifyDataSetChanged()
    }
}
