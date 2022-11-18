package hk.hkucs.comp3330_project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.mashape.unirest.http.Unirest
import hk.hkucs.comp3330_project.databinding.ActivityCodeScannerBinding
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.doAsync


class CodeScanner : AppCompatActivity() {

    private var name = " "
    private var category = ""

    private lateinit var codescanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_scanner)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==
            PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.CAMERA),123)
        }
        else{
            startScannning()
        }
    }

    private fun startScannning(){
        val scannerView: CodeScannerView = findViewById(R.id.scanner_view)
        codescanner = CodeScanner(this,scannerView)
        codescanner.camera = CodeScanner.CAMERA_BACK
        codescanner.formats = CodeScanner.ALL_FORMATS
        codescanner.autoFocusMode = AutoFocusMode.SAFE
        codescanner.scanMode = ScanMode.SINGLE
        codescanner.isAutoFocusEnabled = true
        codescanner.isFlashEnabled = false

        codescanner.decodeCallback = DecodeCallback {
            var barcode_number = it.text
            runOnUiThread {
                Toast.makeText(this, "Scan Successful", Toast.LENGTH_SHORT).show()

                doAsync{
                    var response =
                        Unirest.get("https://chomp-food-nutrition-database-v2.p.rapidapi.com/food/branded/barcode.php?code=${barcode_number} \n")
                            .header("X-RapidAPI-Key", "2db1950c03mshddf89187ce9abdcp156e5ajsn88b1399658d8")
                            .header("X-RapidAPI-Host", "chomp-food-nutrition-database-v2.p.rapidapi.com")
                            .asString()
                    var all = response.body

                    //to handle case if product is not found in our database
                    if ((all.indexOf("No food items were found.").toString()).toInt() != -1){
                        Log.d("TAG","The item is not found in our database")
                    }
                    else{
                        name = getItemName(all)
                        Log.d("TAG",name)
                        category = getCategory(all)
                        Log.d("TAG",category)
                        Log.d("TAG","end of print")
                    }
                }
                val i = Intent(this, ManualInputActivity::class.java)
                //INI YG MASI ERROR YA
//                i.putExtra("name", name)
//                i.putExtra("category",category)
                startActivity(i)
            }
        }

        codescanner.errorCallback = ErrorCallback {
            runOnUiThread{
                Toast.makeText(this,"Camera initialization error: ${it.message}", Toast.LENGTH_SHORT).show()

            }
        }
        scannerView.setOnClickListener{
            codescanner.startPreview()
        }
    }

    private fun getItemName(result:String): String {
        var temp_index = (result.indexOf("name").toString()).toInt()
        //+8 because name": " is of length 7
        var starting_index_name = temp_index + 8
        //Retrieve index of '"' starting after the word '"name": "'
        var end_index_name = (result.indexOf('"',starting_index_name).toString()).toInt() - 1
        var itemName = result.slice(starting_index_name..end_index_name)
        return itemName

    }

    private fun getCategory(result:String):String{
        var categoryType = "Others"
        var temp_index = (result.indexOf("keywords").toString()).toInt() + 9
        var starting_index_keywords = (result.indexOf('"',temp_index).toString()).toInt()
        if (starting_index_keywords != -1){
            var end_index_keywords = (result.indexOf(']',temp_index).toString()).toInt()-1
            var keywords = result.slice(starting_index_keywords..end_index_keywords)
            var keywords_list  = keywords.split(" ")
            val mutable_keywords = mutableListOf<String>()

            for (i in 0 until keywords_list.size){
                if (keywords_list[i].length > 1){
                    mutable_keywords.add(keywords_list[i])
                }
            }

            for (i in 0 until mutable_keywords.size){
                mutable_keywords.set(i,mutable_keywords[i].replace(",",""))
                mutable_keywords.set(i,mutable_keywords[i].drop(1))
                mutable_keywords.set(i,mutable_keywords[i].dropLast(2))
                Log.d("TAG",mutable_keywords[i])
            }

            var category_list = listOf("Dairy","Snack","Poultry","Canned Food","Baked Good")
            for (i in 0 until mutable_keywords.size){
                if (mutable_keywords[i] in category_list){
                    categoryType = mutable_keywords[i]
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
                startScannning()
            }
            else
            {
                Toast.makeText(this,"Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codescanner.isInitialized)
        {
            codescanner?.startPreview()
        }
    }

    override fun onPause() {
        if (::codescanner.isInitialized){
            codescanner?.releaseResources()
        }
        super.onPause()
    }
}