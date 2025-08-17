package com.example.experiment3final

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity // Corrected import
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// --- THIS IS THE CORRECTED LINE ---
class DashboardActivity : AppCompatActivity() {

    private lateinit var welcomeMessageTextView: TextView
    private lateinit var cycleDayNumberTextView: TextView
    private lateinit var sendEmailButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_dashboard)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        welcomeMessageTextView = findViewById(R.id.welcomeMessageTextView)
        cycleDayNumberTextView = findViewById(R.id.cycleDayNumber)
        sendEmailButton = findViewById(R.id.sendEmailButton)

        // Get data from the intent that started this activity
        val userName = intent.getStringExtra("USER_NAME")
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val currentCycleDay = intent.getLongExtra("CYCLE_DAY", 1L)
        val cycleLength = intent.getIntExtra("CYCLE_LENGTH", 28)

        // Populate the UI with the received data
        welcomeMessageTextView.text = "Hello, $userName ✨"
        cycleDayNumberTextView.text = cycleLength.toString()

        // Set up the click listener for the Send Email button
        sendEmailButton.setOnClickListener {
            val subject = "My Cycle Info"
            val body = "Hi! I am $userName. My current cycle day is $currentCycleDay of a $cycleLength day cycle."

            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(userEmail))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }

            // This check requires a <queries> tag in AndroidManifest.xml for newer Android versions
            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(emailIntent)
            } else {
                Toast.makeText(this, "No email client installed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}