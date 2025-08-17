package com.example.experiment3final

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // Declare variables here
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var cycleLengthEditText: EditText
    private lateinit var createAccountButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views from the updated layout
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        cycleLengthEditText = findViewById(R.id.cycleLengthEditText)
        createAccountButton = findViewById(R.id.createAccountButton)

        createAccountButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val cycleLengthStr = cycleLengthEditText.text.toString().trim()

            var hasError = false
            if (name.isBlank()) {
                nameEditText.error = "Name is required"
                hasError = true
            }
            if (email.isBlank()) {
                emailEditText.error = "Email is required"
                hasError = true
            }
            if (cycleLengthStr.isBlank()) {
                cycleLengthEditText.error = "Cycle length is required"
                hasError = true
            }

            if (!hasError) {
                // Save user data
                val sharedPreferences = getSharedPreferences("PeriodTrackerPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val averageCycleLength = cycleLengthStr.toInt()
                editor.putInt("AVERAGE_CYCLE_LENGTH", averageCycleLength)
                val cycleStartDateMillis = System.currentTimeMillis()
                editor.putLong("CYCLE_START_DATE", cycleStartDateMillis)
                editor.apply()

                // Calculate current cycle day
                val nowMillis = System.currentTimeMillis()
                val diffMillis = nowMillis - cycleStartDateMillis
                val daysPassed = TimeUnit.MILLISECONDS.toDays(diffMillis)
                val currentCycleDay = (daysPassed % averageCycleLength) + 1

                // Create an Intent to start DashboardActivity
                val intent = Intent(this, DashboardActivity::class.java).apply {
                    putExtra("USER_NAME", name)
                    putExtra("USER_EMAIL", email)
                    putExtra("CYCLE_DAY", currentCycleDay)
                    // Pass the average cycle length to the dashboard
                    putExtra("CYCLE_LENGTH", averageCycleLength)
                }
                startActivity(intent)
            }
        }
    }
}
