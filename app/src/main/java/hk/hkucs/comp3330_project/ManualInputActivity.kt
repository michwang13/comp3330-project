package hk.hkucs.comp3330_project

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.app.DatePickerDialog
import android.database.Cursor
import android.util.Log
import java.util.*
import android.database.sqlite.SQLiteDatabase
import android.media.Image
import android.os.Build
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import android.os.ext.SdkExtensions.getExtensionVersion
import androidx.activity.result.ActivityResultLauncher


class ManualInputActivity : AppCompatActivity() {
    private var itemName: String? = null
    private var notes: String? = null
    private var category: String? = null
    private var expiryDate: String? = null
    private var reminder: String? = null

    private var itemNameEditText: EditText? = null
    private var notesEditText: EditText? = null
    private var categoriesSpinner: Spinner? = null
    private var reminderSpinner: Spinner? = null

    private var datePickerButton: Button? = null

    private var year: Int? = null
    private var month: Int? = null
    private var day: Int? = null
    private var dbhelper: DBHelper? = null
    private var itemImageView: ImageView? = null;
    private var imageURI: String? = null;

    private var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null;

    // resources:  https://guides.codepath.com/android/local-databases-with-sqliteopenhelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_input)

        initializeViews()
        initializeCurrentDate()
        dbhelper = DBHelper(this)
        itemImageView = findViewById<ImageView>(R.id.itemImageView)

        // initialize the categories spinner to include poultry, dairy goods, etc
        val categoriesSpinner: Spinner = findViewById(R.id.categories_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.categories_array,           //strings.xml
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categoriesSpinner.adapter = adapter
        }

        // initialize the categories spinner to include poultry, dairy goods, etc
        val reminderSpinner: Spinner = findViewById(R.id.reminder_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.reminder_array,           //strings.xml
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            reminderSpinner.adapter = adapter
        }


        // initialize photo picker
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                imageURI = uri.toString();
                itemImageView?.setImageURI(uri)
                // store to db
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    private fun initializeCurrentDate(){
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH) + 1           // plus 1 bcs month is 0-indexed
        day = c.get(Calendar.DAY_OF_MONTH)
        updateDatePicker()
    }

    private fun updateDatePicker(){
        datePickerButton?.text = makeDateString()
    }

     fun openDatePickerDialog(view: View) {

         val datePickerDialog = DatePickerDialog(this, { _, yearPicked, monthPicked, dayPicked ->

             // Display Selected date in textbox
             year = yearPicked
             month = monthPicked + 1 // because month is 0-indexed
             day = dayPicked
             updateDatePicker()
         }, year!!, month!!-1, day!!)

         datePickerDialog.show()
    }

    private fun makeDateString(): String {
        return getMonthFormat(month!!) + " " + day + ", " + year
    }
    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "Jan"
        if (month == 2) return "Feb"
        if (month == 3) return "Mar"
        if (month == 4) return "Apr"
        if (month == 5) return "May"
        if (month == 6) return "Jun"
        if (month == 7) return "Jul"
        if (month == 8) return "Aug"
        if (month == 9) return "Sep"
        if (month == 10) return "Oct"
        if (month == 11) return "Nov"
        if (month == 12) return "Dec"

        return "Jan"
    }

    private fun initializeViews() {
        itemNameEditText = findViewById(R.id.itemNameEditText)
        notesEditText = findViewById(R.id.notesEditText)
        categoriesSpinner = findViewById(R.id.categories_spinner)
        reminderSpinner = findViewById(R.id.reminder_spinner)
        datePickerButton = findViewById(R.id.datePickerButton)
    }
    fun onDoneButtonClicked(view: View) {
        val itemDone = Item(itemNameEditText!!.text.toString(),
            notesEditText!!.text.toString(),
            categoriesSpinner!!.selectedItem.toString(),
            makeDateString(),
            reminderSpinner!!.selectedItem.toString(),
            imageURI
            )


        Log.d("TAG","itemName: " + itemNameEditText?.text.toString())
        Log.d("TAG","notes: " + notesEditText?.text.toString())
        Log.d("TAG","category: " + categoriesSpinner?.selectedItem.toString())
        Log.d("TAG", "expiryDate: " + makeDateString())
        Log.d("TAG","reminder: " + reminderSpinner?.selectedItem.toString())
        Log.d("TAG","imageURI: " + imageURI.toString())


        dbhelper?.insertItem(itemDone)

        // below code is for sample use only
//        val cursor = dbhelper?.queryData();
////        val id = cursor.getString(cursor.getColumnIndex()) // id is first column in db
//        cursor?.moveToFirst();
//        val id = cursor?.getString(0) // id is first column in db
//        Log.d("TAG", "test_id: "+id)
    }

    fun onEditPhotoButtonClicked(view: View){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d("TAG","test1");

            pickMedia!!.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        else{
            Log.d("TAG", "Needs Android Tiramisu or later");

        }
    }

}