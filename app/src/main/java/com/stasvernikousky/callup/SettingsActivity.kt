package com.stasvernikousky.callup

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val numberInput = findViewById<EditText>(R.id.settings_number_input)
        numberInput.setText(CallPrefs.getNumber(this))

        val autoCallSwitch = findViewById<MaterialSwitch>(R.id.auto_call_switch)
        autoCallSwitch.isChecked = CallPrefs.isAutoCallEnabled(this)
        autoCallSwitch.setOnCheckedChangeListener { _, checked ->
            CallPrefs.setAutoCallEnabled(this, checked)
        }

        findViewById<Button>(R.id.settings_save_button).setOnClickListener {
            val number = numberInput.text.toString().trim()
            if (number.isEmpty()) {
                Toast.makeText(this, R.string.empty_number, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CallPrefs.setNumber(this, number)
            Toast.makeText(this, R.string.number_saved, Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.back_button).setOnClickListener { finish() }
    }
}
