package hk.hkucs.comp3330_project

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.app.DatePickerDialog
import android.util.Log


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_input)

        initializeViews();

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

    private fun initializeViews() {
        itemNameEditText = findViewById(R.id.itemNameEditText)
        notesEditText = findViewById(R.id.notesEditText)
        categoriesSpinner = findViewById(R.id.categories_spinner)
        reminderSpinner = findViewById(R.id.reminder_spinner)
    }

    fun onDoneButtonClicked(view: android.view.View) {
        Log.d("TAG","itemName")
        Log.d("TAG", itemNameEditText!!.text.toString())
        Log.d("TAG", "NOTES")
        Log.d("TAG", notesEditText?.text.toString())
        Log.d("TAG",categoriesSpinner?.selectedItem.toString())
        Log.d("TAG",reminderSpinner?.selectedItem.toString())

    }


}