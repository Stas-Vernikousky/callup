package com.stasvernikousky.callup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var setupView: View
    private lateinit var callView: View
    private lateinit var numberInput: EditText
    private lateinit var savedNumberText: TextView

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

        setupView = findViewById(R.id.setup_view)
        callView = findViewById(R.id.call_view)
        numberInput = findViewById(R.id.number_input)
        savedNumberText = findViewById(R.id.saved_number_text)

        findViewById<Button>(R.id.save_button).setOnClickListener {
            val number = numberInput.text.toString().trim()
            if (number.isEmpty()) {
                Toast.makeText(this, R.string.empty_number, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CallPrefs.setNumber(this, number)
            showCallView()
        }

        findViewById<Button>(R.id.call_button).setOnClickListener {
            if (hasCallPermission()) {
                makeCall()
            } else {
                callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }

        findViewById<Button>(R.id.change_number_button).setOnClickListener {
            numberInput.setText(CallPrefs.getNumber(this))
            setupView.visibility = View.VISIBLE
            callView.visibility = View.GONE
        }

        findViewById<Button>(R.id.settings_button).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        maybeAutoCall()
    }

    override fun onResume() {
        super.onResume()
        refreshState()
    }

    private fun refreshState() {
        val number = CallPrefs.getNumber(this)
        if (number.isEmpty()) {
            numberInput.setText("")
            setupView.visibility = View.VISIBLE
            callView.visibility = View.GONE
        } else {
            savedNumberText.text = number
            setupView.visibility = View.GONE
            callView.visibility = View.VISIBLE
        }
    }

    private fun showCallView() {
        savedNumberText.text = CallPrefs.getNumber(this)
        setupView.visibility = View.GONE
        callView.visibility = View.VISIBLE
    }

    private fun maybeAutoCall() {
        val number = CallPrefs.getNumber(this)
        if (number.isEmpty() || !CallPrefs.isAutoCallEnabled(this)) return
        if (hasCallPermission()) {
            makeCall()
        } else {
            callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    private fun hasCallPermission(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
            PackageManager.PERMISSION_GRANTED

    private fun makeCall() {
        val number = CallPrefs.getNumber(this)
        if (number.isEmpty()) {
            Toast.makeText(this, R.string.empty_number, Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$number")))
    }
}
