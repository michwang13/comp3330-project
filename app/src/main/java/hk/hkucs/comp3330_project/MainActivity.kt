package hk.hkucs.comp3330_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // first launch -> enter info
        val i = Intent(this, DummyCrud::class.java)
        startActivity(i)
    }
}