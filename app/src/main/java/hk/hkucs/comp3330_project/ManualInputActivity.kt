package hk.hkucs.comp3330_project

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*


class ManualInputActivity : AppCompatActivity() {
    private var itemName: String = ""
    private var notes: String = ""
    private var category: String = ""
    private var expiryDate: String = ""
    private var reminder: String = ""
    private var currentItemID: String = UUID.randomUUID().toString()

    private var itemNameEditText: EditText? = null
    private var notesEditText: EditText? = null
    private var categoriesSpinner: Spinner? = null
    private var reminderSpinner: Spinner? = null
    private lateinit var categoriesSpinnerAdapter: ArrayAdapter<CharSequence>
    private lateinit var reminderSpinnerAdapter: ArrayAdapter<CharSequence>


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

        dbhelper = DBHelper(this, null)
        itemImageView = findViewById<ImageView>(R.id.itemImageView)


        val categoriesSpinner: Spinner = findViewById(R.id.categories_spinner)
        categoriesSpinnerAdapter =
            ArrayAdapter.createFromResource(
                this,
                R.array.categories_array,           //strings.xml
                android.R.layout.simple_spinner_item
            )
        categoriesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoriesSpinner.adapter = categoriesSpinnerAdapter

        val reminderSpinner: Spinner = findViewById(R.id.reminder_spinner)
        reminderSpinnerAdapter =
            ArrayAdapter.createFromResource(
                this,
                R.array.reminder_array,           //strings.xml
                android.R.layout.simple_spinner_item
            )
        reminderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        reminderSpinner.adapter = reminderSpinnerAdapter

        // initialize the categories spinner to include poultry, dairy goods, etc
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.categories_array,           //strings.xml
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            categoriesSpinner.adapter = adapter
//        }

        // initialize the categories spinner to include poultry, dairy goods, etc
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.reminder_array,           //strings.xml
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            reminderSpinner.adapter = adapter
//        }


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
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.items -> {
                    startActivity(Intent(this, ListPageActivity::class.java))
                }
                R.id.scan -> {
                    startActivity(Intent(this, CodeScannerActivity::class.java))
                }
                R.id.categories -> {
                    startActivity(Intent(this, CategoriesActivity::class.java))
                }
            }
            true
        }

        val bundle: Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                val itemID: String? = getString("itemID")
                itemID?.let {
                    val cursor = dbhelper?.getOneItemByID(itemID)
                    if (cursor!!.moveToFirst()){
                        itemName = cursor.getString(cursor.getColumnIndexOrThrow("itemName"))
                        expiryDate = cursor.getString(cursor.getColumnIndexOrThrow("expiryDate"))
                        imageURI = cursor.getString(cursor.getColumnIndexOrThrow("imageURI"))
                        category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                        notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"))
                        reminder = cursor.getString(cursor.getColumnIndexOrThrow("reminder"))
                        cursor.close()

                        itemNameEditText?.setText(itemName.toEditable())
                        notesEditText?.setText(notes.toEditable())

                        if (imageURI != ""){
                            itemImageView?.setImageURI(Uri.parse(imageURI))
                        }

                        setExpiryDate(expiryDate)
                        updateDatePicker()

                        categoriesSpinner.setSelection(categoriesSpinnerAdapter.getPosition(category))

                        reminderSpinner.setSelection(categoriesSpinnerAdapter.getPosition(reminder))
                    }
                    currentItemID = itemID
                }

                val itemNameIntent: String? = getString("itemName")
                itemNameIntent?.let{
                    itemName = itemNameIntent
                    itemNameEditText?.setText(itemName.toEditable())
                    initializeCurrentDate()
                }

                val categoryIntent: String? = getString("category")
                categoryIntent?.let{
                    category = categoryIntent
                    categoriesSpinner.setSelection(categoriesSpinnerAdapter.getPosition(category))
                    initializeCurrentDate()
                }
            }
        } ?: run {
            initializeCurrentDate()
        }
    }

    private fun initializeCurrentDate(){
        val today = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        setExpiryDate(formatter.format(today))
//        val c = Calendar.getInstance()
//        year = c.get(Calendar.YEAR)
//        month = c.get(Calendar.MONTH) + 1           // plus 1 bcs month is 0-indexed
//        day = c.get(Calendar.DAY_OF_MONTH)
        updateDatePicker()
    }

    private fun setExpiryDate(expDate: String?){

        val dateTemp = expDate!!.split("/")
        day = Integer.parseInt(dateTemp[0])
        month = Integer.parseInt(dateTemp[1])
        year = Integer.parseInt(dateTemp[2])

    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)


    private fun prefillForm(itemID: String){
        val cursor = dbhelper?.getOneItemByID(itemID)
        if (cursor!!.moveToFirst()){
            val itemName = cursor.getString(cursor.getColumnIndexOrThrow("itemName"))
            val expiryDate = cursor.getString(cursor.getColumnIndexOrThrow("expiryDate"))
            val imageURI = cursor.getString(cursor.getColumnIndexOrThrow("imageURI"))
            val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
            val notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"))
            val reminder = cursor.getString(cursor.getColumnIndexOrThrow("reminder"))
            cursor.close()


            itemNameEditText?.setText(itemName.toEditable())
            notesEditText?.setText(notes.toEditable())
            itemImageView?.setImageURI(Uri.parse(imageURI))

            setExpiryDate(expiryDate)
            updateDatePicker()

            categoriesSpinner!!.setSelection(categoriesSpinnerAdapter.getPosition(category))

            reminderSpinner!!.setSelection(categoriesSpinnerAdapter.getPosition(reminder))

        }

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
        return day.toString() + "/" + getMonthFormat(month!!) + "/" + year.toString()
    }
    private fun getMonthFormat(month: Int): String {
        return month.toString()
    }

    private fun initializeViews() {
        itemNameEditText = findViewById(R.id.itemNameEditText)
        notesEditText = findViewById(R.id.notesEditText)
        categoriesSpinner = findViewById(R.id.categories_spinner)
        reminderSpinner = findViewById(R.id.reminder_spinner)
        datePickerButton = findViewById(R.id.datePickerButton)
        itemImageView = findViewById(R.id.itemImageView)
    }

    fun onDoneButtonClicked(view: View) {
        val itemDone = Item(
            currentItemID,
            itemNameEditText!!.text.toString(),
            notesEditText!!.text.toString(),
            categoriesSpinner!!.selectedItem.toString(),
            makeDateString(),
            reminderSpinner!!.selectedItem.toString(),
            imageURI
            )

//        Log.d("TAG","itemName: " + itemNameEditText?.text.toString())
//        Log.d("TAG","notes: " + notesEditText?.text.toString())
//        Log.d("TAG","category: " + categoriesSpinner?.selectedItem.toString())
//        Log.d("TAG", "expiryDate: " + makeDateString())
//        Log.d("TAG","reminder: " + reminderSpinner?.selectedItem.toString())
//        Log.d("TAG","imageURI: " + imageURI.toString())

        val cursor = dbhelper?.getOneItemByID(currentItemID)
        if (cursor != null && cursor.moveToFirst()){
            dbhelper?.updateOneItemByID(currentItemID, itemDone)
        } else {
            dbhelper?.insertItem(itemDone)
        }



        val i = Intent(this, ListPageActivity::class.java)
        startActivity(i)
        // below code is for sample use only
//        val cursor = dbhelper?.queryData();
////        val id = cursor.getString(cursor.getColumnIndex()) // id is first column in db
//        cursor?.moveToFirst();
//        val id = cursor?.getString(0) // id is first column in db
//        Log.d("TAG", "test_id: "+id)
    }

    fun onDeleteButtonClicked(view: View) {
        val cursor = dbhelper?.getOneItemByID(currentItemID)
        if (cursor != null && cursor.moveToFirst()){
            dbhelper?.deleteOneItemByID(currentItemID)
            Toast.makeText(this, "Item deleted!", Toast.LENGTH_SHORT).show()
            val i = Intent(this, ListPageActivity::class.java)
            startActivity(i)
        } else {
            Toast.makeText(this, "Item not in database!", Toast.LENGTH_SHORT).show()
        }
    }

    fun onEditPhotoButtonClicked(view: View){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            Log.d("TAG","test1");

            pickMedia!!.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        else{
            Log.d("TAG", "Needs Android Tiramisu or later");

        }
    }

}