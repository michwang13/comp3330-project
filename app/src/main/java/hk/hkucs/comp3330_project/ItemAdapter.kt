package hk.hkucs.comp3330_project

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*


class ItemAdapter(private val context : Activity, private val arrayList: ArrayList<Item>) : ArrayAdapter<Item>(context,
                  R.layout.list_item, arrayList)    {

    private var tempItemList = ArrayList(arrayList)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.list_item, null)

        val imageView : ImageView = view.findViewById((R.id.itemPic))
        val itemName : TextView = view.findViewById(R.id.itemName)
        val expDate : TextView = view.findViewById(R.id.expiryDate)
        val warningIcon : ImageView = view.findViewById(R.id.warning_icon)

        if (arrayList[position].imageURI != ""){
            imageView?.setImageURI(Uri.parse(arrayList[position].imageURI))
        }
        itemName.text = arrayList[position].itemName
        expDate.text = arrayList[position].expiryDate

        setColor(warningIcon, arrayList[position].expiryDate)

        return view
    }

    fun filterName(name: String?) {
        val text = name!!.lowercase()
        arrayList.clear()
        if (text.isEmpty()) {
            arrayList.addAll(tempItemList)
        } else {
            for (i in 0..tempItemList.size - 1) {
                if (tempItemList[i].itemName.lowercase().contains(text)) {
                    arrayList.add(tempItemList.get(i))
                }
            }
        }
        notifyDataSetChanged()
    }

    fun sortByExpiryDate(ascending: Boolean){
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        arrayList.clear()
        if (ascending){
            tempItemList.sortBy { formatter.parse(it.expiryDate) }
        } else {
            tempItemList.sortByDescending { formatter.parse(it.expiryDate) }
        }
        arrayList.addAll(tempItemList)
        notifyDataSetChanged()
    }

    private fun setColor(imgView: ImageView, expDate: String) {
        val today = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val expiry = formatter.parse(expDate)

        val diff: Long = expiry.time - today.time
        val diffDays: Long = diff / (1000 * 60 * 60 * 24)

        if (diffDays < 0)
            imgView.setColorFilter(
                ContextCompat.getColor(context, R.color.black),
                android.graphics.PorterDuff.Mode.SRC_IN
            );
        else if (diffDays <= 7)
            imgView.setColorFilter(
                ContextCompat.getColor(context, R.color.red),
                android.graphics.PorterDuff.Mode.SRC_IN
            );
        else if (diffDays <= 14)
            imgView.setColorFilter(
                ContextCompat.getColor(context, R.color.orange),
                android.graphics.PorterDuff.Mode.SRC_IN
            );
        else
            imgView.setColorFilter(
                ContextCompat.getColor(context, R.color.green),
                android.graphics.PorterDuff.Mode.SRC_IN
            );

    }
}
