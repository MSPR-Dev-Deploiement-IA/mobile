package fr.epsi.arosaje

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        val editTextMail = findViewById<EditText>(R.id.editTextEmail)
        val editTextUsername = findViewById<EditText>(R.id.editTextName)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val email = editTextMail.text.toString()
            val name = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword) {
                register(email, name, password)
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs et vérifier que les mots de passe correspondent", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun register(email: String, name: String, password: String) {
        val client = OkHttpClient()

        val json = Json.encodeToJsonElement(
            JsonObject(mapOf(
                "email" to Json.encodeToJsonElement(email),
                "name" to Json.encodeToJsonElement(name),
                "password" to Json.encodeToJsonElement(password)
            ))
        ).toString()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/backend/auth/register")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Inscription réussie", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
