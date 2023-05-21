package fr.epsi.arosaje

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import kotlinx.serialization.json.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class ApiService(private val context: Context) {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .cookieJar(object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {}
            override fun loadForRequest(url: HttpUrl): List<Cookie> = getStoredCookies()
        })
        .build()

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)

    private val mainHandler = Handler(Looper.getMainLooper())

    fun login(email: String, password: String, callback: (success: Boolean) -> Unit) {
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
                mainHandler.post {
                    callback(false)
                    Toast.makeText(context, "Erreur lors de la connexion", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()

                    if (responseBody != null) {
                        val json = Json.parseToJsonElement(responseBody) as JsonObject
                        val accessToken = json["access_token"]?.jsonPrimitive?.content
                        val refreshToken = json["refresh_token"]?.jsonPrimitive?.content

                        if (accessToken != null) {
                            storeTokens(accessToken, refreshToken)
                            mainHandler.post {
                                callback(true)
                            }
                        } else {
                            mainHandler.post {
                                callback(false)
                            }
                        }
                    } else {
                        mainHandler.post {
                            callback(false)
                        }
                    }
                } else {
                    mainHandler.post {
                        callback(false)
                    }
                }
            }
        })
    }

    fun getUserInfo(callback: (name: String?, email: String?) -> Unit) {
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/api/users/me")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    callback(null, null)
                    Toast.makeText(context, "Erreur lors de la récupération des informations de l'utilisateur", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val json = Json.parseToJsonElement(responseBody) as JsonObject
                        val user = json["user"]?.jsonObject
                        val name = user?.get("name")?.jsonPrimitive?.content
                        val email = user?.get("email")?.jsonPrimitive?.content
                        mainHandler.post {
                            callback(name, email)
                        }
                    } else {
                        mainHandler.post {
                            callback(null, null)
                        }
                    }
                } else {
                    mainHandler.post {
                        callback(null, null)
                        Toast.makeText(context, "Impossible de récupérer les informations de l'utilisateur", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun storeTokens(accessToken: String, refreshToken: String?) {
        with(sharedPref.edit()) {
            putString("accessToken", accessToken)
            putString("refreshToken", refreshToken)
            apply()
        }
    }

    private fun getStoredCookies(): List<Cookie> {
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
