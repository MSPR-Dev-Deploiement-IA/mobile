package fr.epsi.arosaje

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient

class MyCookieJar : CookieJar {
    private var cookies: List<Cookie> = emptyList()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies
    }
}

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
                    Toast.makeText(this@UpdateProfileActivity, "Erreur lors de la récupération des informations de l'utilisateur", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val json = Json.parseToJsonElement(responseBody) as JsonObject
                        val name = json["name"]?.jsonPrimitive?.content
                        val email = json["email"]?.jsonPrimitive?.content

                        if (name != null && email != null) {
                            callback(name, email)
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@UpdateProfileActivity, "Impossible de récupérer les informations de l'utilisateur", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun getStoredCookies(): List<Cookie> {
        val sharedPref = getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
        val access_token = sharedPref.getString("access_token", null)

        return if (access_token != null) {
            val cookie = Cookie.Builder()
                .domain("10.0.2.2")
                .path("/api")
                .name("access_token")
                .value(access_token)
                .build()
            listOf(cookie)
        } else {
            emptyList()
        }
    }
}
