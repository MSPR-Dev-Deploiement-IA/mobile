package fr.epsi.arosaje

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextMail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonSubmit)
        val textViewNoAccount = findViewById<TextView>(R.id.textViewNoAccount)

        buttonLogin.setOnClickListener {
            val email = editTextMail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

        textViewNoAccount.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(email: String, password: String) {
        val client = OkHttpClient()

        val json = Json.encodeToJsonElement(
            JsonObject(mapOf(
                "email" to Json.encodeToJsonElement(email),
                "password" to Json.encodeToJsonElement(password)
            ))
        ).toString()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/auth/login")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    println("1 + $email + $password")
                    Toast.makeText(applicationContext, "Erreur lors de la connexion", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Connexion réussie", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, SplashloginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    runOnUiThread {
                        println("2 + $email + $password")
                        Toast.makeText(applicationContext, "Nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
