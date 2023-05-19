package fr.epsi.arosaje

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient

class UpdateProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updateprofile)

        val nameTextView = findViewById<TextView>(R.id.textViewName)
        val emailTextView = findViewById<TextView>(R.id.textViewEmail)

        getUserInfo { name, email ->
            runOnUiThread {
                nameTextView.text = name
                emailTextView.text = email
            }
        }
    }

    private fun getUserInfo(callback: (name: String, email: String) -> Unit) {
        val client = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {}
                override fun loadForRequest(url: HttpUrl): List<Cookie> = getStoredCookies()
            })
            .build()

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/api/users/me")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    println("1")
                    Toast.makeText(this@UpdateProfileActivity, "Erreur lors de la récupération des informations de l'utilisateur", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        Log.d("UpdateProfileActivity", "Response body: $responseBody")
                        val json = Json.parseToJsonElement(responseBody) as JsonObject
                        val user = json["user"]?.jsonObject
                        val name = user?.get("name")?.jsonPrimitive?.content
                        val email = user?.get("email")?.jsonPrimitive?.content
                        if (name != null && email != null) {
                            callback(name, email)
                        } else {
                            Log.d("UpdateProfileActivity", "Name or email was null: name=$name, email=$email")
                        }
                    } else {
                        Log.d("UpdateProfileActivity", "Response body was null.")
                    }
                } else {
                    val statusCode = response.code
                    val responseBody = response.body?.string()
                    Log.d("UpdateProfileActivity", "HTTP status code: $statusCode")
                    Log.d("UpdateProfileActivity", "Response body: $responseBody")

                    runOnUiThread {
                        Toast.makeText(this@UpdateProfileActivity, "Impossible de récupérer les informations de l'utilisateur", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }

    private fun getStoredCookies(): List<Cookie> {
        val sharedPref = getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString("accessToken", null)

        return if (accessToken != null) {
            val cookie = Cookie.Builder()
                .domain("10.0.2.2")
                .path("/api")
                .name("access_token")
                .value(accessToken)
                .build()
            listOf(cookie)
        } else {
            emptyList()
        }
    }
}
