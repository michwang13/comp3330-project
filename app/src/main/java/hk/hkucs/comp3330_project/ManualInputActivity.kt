package hk.hkucs.comp3330_project

import android.app.*
import android.content.Context
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
import kotlin.math.min


class ManualInputActivity : AppCompatActivity() {
    private var itemName: String = ""
    private var notes: String = ""
    private var category: String = ""
    private var expiryDate: String = ""
    private var reminder: String = ""
    private var currentItemID: String = UUID.randomUUID().toString()
    private var currentItemNotifID: Int = currentItemID.replace("-", "").filter { it.isDigit() }[0].toInt()

    private var itemNameEditText: EditText? = null
    private var notesEditText: EditText? = null
    private var categoriesSpinner: Spinner? = null
    private var reminderSpinner: Spinner? = null
    private lateinit var categoriesSpinnerAdapter: ArrayAdapter<CharSequence>
    private lateinit var reminderSpinnerAdapter: ArrayAdapter<CharSequence>


    private var datePickerButton: Button? = null

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var dbhelper: DBHelper? = null
    private var itemImageView: ImageView? = null;
    private var imageURI: String? = null;

    private var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_input)

        initializeViews()

        createNotificationChannel()

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

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageURI = uri.toString();
                itemImageView?.setImageURI(uri)
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
                        reminderSpinner.setSelection(reminderSpinnerAdapter.getPosition(reminder))
                    }
                    currentItemID = itemID
                }

                val itemNameIntent: String? = getString("itemName")
                itemNameIntent?.let{
                    itemName = itemNameIntent
                    itemNameEditText?.setText(itemName.toEditable())
                    initializeCurrentDate()
                }

                val notifIDIntent: String? = getString("notifID")
                notifIDIntent?.let{
                    currentItemNotifID = notifIDIntent.toInt()
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
        updateDatePicker()
    }

    private fun setExpiryDate(expDate: String?){

        val dateTemp = expDate!!.split("/")
        day = Integer.parseInt(dateTemp[0])
        month = Integer.parseInt(dateTemp[1])
        year = Integer.parseInt(dateTemp[2])

    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun updateDatePicker(){
        datePickerButton?.text = makeDateString()
    }

     fun openDatePickerDialog(view: View) {

         val datePickerDialog = DatePickerDialog(this, { _, yearPicked, monthPicked, dayPicked ->

             year = yearPicked
             month = monthPicked + 1
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

        val cursor = dbhelper?.getOneItemByID(currentItemID)
        if (cursor != null && cursor.moveToFirst()){
            dbhelper?.updateOneItemByID(currentItemID, itemDone)
        } else {
            dbhelper?.insertItem(itemDone)
        }

        scheduleNotification()

        val i = Intent(this, ListPageActivity::class.java)
        startActivity(i)
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

            pickMedia!!.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        else{
            Log.d("TAG", "Needs Android Tiramisu or later");

        }
    }

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = "Your " + itemNameEditText!!.text.toString() + " is expiring!"
        val message = "You have set a reminder for this item, click the notification to check your items"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        notificationID = currentItemNotifID

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val addDay = reminderSpinner!!.selectedItem.toString().filter { it.isDigit() }
        calendar.add(Calendar.DATE, -Math.abs(addDay.toInt()))
        calendar.add(Calendar.DATE, -30)

        val minute = 0
        val hour = 8
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        calendar.set(year, month, day, hour, minute)

        val time = calendar.timeInMillis

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    fun createNotificationChannel() {
        val name = "Notification Channel"
        val desc = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}