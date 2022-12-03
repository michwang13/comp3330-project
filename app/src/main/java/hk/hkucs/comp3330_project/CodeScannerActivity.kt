package hk.hkucs.comp3330_project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.mashape.unirest.http.Unirest
import org.jetbrains.anko.doAsync


class CodeScannerActivity : AppCompatActivity() {

    private var name = " "
    private var category = ""
    private lateinit var cancelButton: ImageButton
    private lateinit var manualInputButton: ImageButton


    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_scanner)

        cancelButton = findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener{
            val i = Intent(this, ListPageActivity::class.java)
            startActivity(i)
        }

        manualInputButton = findViewById(R.id.manual_input_button)
        manualInputButton.setOnClickListener{
            val i = Intent(this, ManualInputActivity::class.java)
            startActivity(i)
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==
            PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.CAMERA),123)
        }
        else{
            startScanning()
        }

    }

    private fun startScanning(){
        val scannerView: CodeScannerView = findViewById(R.id.scanner_view)
        codeScanner = CodeScanner(this,scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            val barcodeNum = it.text
            runOnUiThread {
                Toast.makeText(this, "Scan Successful", Toast.LENGTH_SHORT).show()

                val i = Intent(this, ManualInputActivity::class.java)

                doAsync{
                    val response =
                        Unirest.get("https://chomp-food-nutrition-database-v2.p.rapidapi.com/food/branded/barcode.php?code=${barcodeNum} \n")
                            .header("X-RapidAPI-Key", "94c75bd9bfmsh7bfbbbb4b10c945p1d9de5jsnf7a0b9e847e1")
                            .header("X-RapidAPI-Host", "chomp-food-nutrition-database-v2.p.rapidapi.com")
                            .asString()
                    val all = response.body

                    //to handle case if product is not found in our database
                    if ((all.indexOf("No food items were found.").toString()).toInt() != -1){
                        Log.d("FAIL","The item is not found in our database")
                    }
                    else{
                        name = getItemName(all)
                        category = getCategory(all)
                    }
                    i.putExtra("itemName", name)
                    i.putExtra("category",category)
                    startActivity(i)
                }
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread{
                Toast.makeText(this,"Camera initialization error: ${it.message}", Toast.LENGTH_SHORT).show()

            }
        }
        scannerView.setOnClickListener{
            codeScanner.startPreview()
        }
    }

    private fun getItemName(result: String): String {
        val tempIndex = (result.indexOf("name").toString()).toInt()
        val startingIndexName = tempIndex + 8
        val endIndexName = (result.indexOf('"', startingIndexName).toString()).toInt() - 1
        return result.slice(startingIndexName..endIndexName)

    }

    private fun getCategory(result:String):String{
        var categoryType = "Others"
        val tempIndex = (result.indexOf("keywords").toString()).toInt() + 9
        val startingIndexKeywords = (result.indexOf('"',tempIndex).toString()).toInt()
        if (startingIndexKeywords != -1){
            val endIndexKeywords = (result.indexOf(']',tempIndex).toString()).toInt()-1
            val keywords = result.slice(startingIndexKeywords..endIndexKeywords)
            val keywordsList  = keywords.split(" ")
            val mutableKeywords = mutableListOf<String>()

            for (i in keywordsList.indices){
                if (keywordsList[i].length > 1){
                    mutableKeywords.add(keywordsList[i])
                }
            }

            for (i in 0 until mutableKeywords.size){
                mutableKeywords.set(i,mutableKeywords[i].replace(",",""))
                mutableKeywords.set(i,mutableKeywords[i].drop(1))
                mutableKeywords.set(i,mutableKeywords[i].dropLast(2))
            }
            
            val res: Resources = resources
            val categoryList = res.getStringArray(R.array.categories_array)
            for (i in 0 until mutableKeywords.size){
                if (mutableKeywords[i] in categoryList){
                    categoryType = mutableKeywords[i]
                }
            }
        }
        return categoryType
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Camera permission granted", Toast.LENGTH_SHORT).show()
                startScanning()
            }
            else
            {
                Toast.makeText(this,"Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized)
        {
            codeScanner?.startPreview()
        }
    }

    override fun onPause() {
        if (::codeScanner.isInitialized){
            codeScanner?.releaseResources()
        }
        super.onPause()
    }
}