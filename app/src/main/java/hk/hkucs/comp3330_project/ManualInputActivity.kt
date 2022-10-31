package hk.hkucs.comp3330_project

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.app.DatePickerDialog
import android.util.Log
import java.util.*


class ManualInputActivity : AppCompatActivity() {
    private var itemName: String? = null
    private var notes: String? = null
    private var category: String? = null
    private var expiryDate: String? = null
    private var reminder: String? = null

    private var itemNameEditText: EditText? = null
    private var notesEditText: EditText? = null
//    private var expiryDateEditText: EditText? = null
    private var categoriesSpinner: Spinner? = null
    private var reminderSpinner: Spinner? = null

    private var datePickerButton: Button? = null

    private var year: Int? = null
    private var month: Int? = null
    private var day: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_input)

        initializeViews()
        initializeCurrentDate()



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
    }

    private fun initializeCurrentDate(){
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH) + 1           // plus 1 bcs month is 0-indexed
        day = c.get(Calendar.DAY_OF_MONTH)
        updateDatePicker();
    }

    private fun updateDatePicker(){
        datePickerButton?.text = makeDateString();
    }

     fun openDatePickerDialog(view: android.view.View) {

         val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, yearPicked, monthPicked, dayPicked ->

             // Display Selected date in textbox
             year = yearPicked
             month = monthPicked + 1 // because month is 0-indexed
             day = dayPicked
             updateDatePicker()
         }, year!!, month!!-1, day!!)

         datePickerDialog.show()
    }

    private fun makeDateString(): String? {
        return getMonthFormat(month!!).toString() + " " + day + ", " + year
    }
    private fun getMonthFormat(month: Int): String? {
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

        return "JAN"
    }

    private fun initializeViews() {
        itemNameEditText = findViewById(R.id.itemNameEditText)
        notesEditText = findViewById(R.id.notesEditText)
        categoriesSpinner = findViewById(R.id.categories_spinner)
        reminderSpinner = findViewById(R.id.reminder_spinner)
        datePickerButton = findViewById(R.id.datePickerButton)
    }
    fun onDoneButtonClicked(view: android.view.View) {
        Log.d("TAG","itemName: " + itemNameEditText?.text.toString())
        Log.d("TAG","notes: " + notesEditText?.text.toString())
        Log.d("TAG","category: " + categoriesSpinner?.selectedItem.toString())
        Log.d("TAG", "expiryDate: " + makeDateString())
        Log.d("TAG","reminder: " + reminderSpinner?.selectedItem.toString())

    }



}