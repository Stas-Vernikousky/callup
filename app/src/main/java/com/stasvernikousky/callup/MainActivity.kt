package com.stasvernikousky.callup

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var numberInput: EditText

    private val callPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                makeCall()
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        numberInput = findViewById(R.id.number_input)
        numberInput.setText(prefs.getString(KEY_NUMBER, ""))

        findViewById<Button>(R.id.save_button).setOnClickListener {
            prefs.edit()
                .putString(KEY_NUMBER, numberInput.text.toString().trim())
                .apply()
            Toast.makeText(this, R.string.number_saved, Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.call_button).setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                makeCall()
            } else {
                callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

    private fun makeCall() {
        val number = numberInput.text.toString().trim()
        if (number.isEmpty()) {
            Toast.makeText(this, R.string.empty_number, Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$number")))
    }

    private companion object {
        const val PREFS_NAME = "callup_prefs"
        const val KEY_NUMBER = "target_number"
    }
}
