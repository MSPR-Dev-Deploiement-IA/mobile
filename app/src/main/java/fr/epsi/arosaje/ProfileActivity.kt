package fr.epsi.arosaje

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        apiService = ApiService(this)

        val nameTextView = findViewById<TextView>(R.id.textViewName)
        val emailTextView = findViewById<TextView>(R.id.textViewEmail)

        apiService.getUserInfo { name, email ->
            runOnUiThread {
                nameTextView.text = name
                emailTextView.text = email
            }
        }
    }
}
