package fr.epsi.arosaje

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.*
import java.io.IOException

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updateprofile)

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
