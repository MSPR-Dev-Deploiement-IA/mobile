package fr.epsi.arosaje

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.serialization.json.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
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
    private lateinit var imageView: ImageView

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
            .url("http://10.0.2.2:8080/backend/auth/login")
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
            .url("http://10.0.2.2:8080/backend/api/users/me")
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

    fun getAllMessages(callback: (messages: List<ChatItem>?) -> Unit) {
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/backend/api/messages/")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    callback(null)
                    Toast.makeText(context, "Erreur lors de la récupération des messages", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val jsonObject = Json.parseToJsonElement(responseBody) as JsonObject
                        val jsonArray = jsonObject["messages"]?.jsonArray
                        if (jsonArray != null) {
                            val messages = jsonArray.mapNotNull { jsonElement ->
                                val messageObject = jsonElement.jsonObject
                                val id = messageObject["ID"]?.jsonPrimitive?.int
                                val userName = messageObject["user"]?.jsonObject?.get("name")?.jsonPrimitive?.content
                                val messageText = messageObject["message_text"]?.jsonPrimitive?.content
                                val timestamp = messageObject["timestamp"]?.jsonPrimitive?.content
                                if (id != null && userName != null && messageText != null && timestamp != null) {
                                    ChatItem(id, userName, messageText, timestamp)
                                } else {
                                    null
                                }
                            }
                            mainHandler.post {
                                callback(messages)
                            }
                        } else {
                            mainHandler.post {
                                callback(null)
                            }
                        }
                    } else {
                        mainHandler.post {
                            callback(null)
                        }
                    }
                } else {
                    mainHandler.post {
                        callback(null)
                        Toast.makeText(context, "Impossible de récupérer les messages", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }


    fun postMessage(message: String, callback: (success: Boolean) -> Unit) {
        val json = Json.encodeToJsonElement(
            JsonObject(mapOf("message_text" to Json.encodeToJsonElement(message)))
        ).toString()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/backend/api/messages/add")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    callback(false)
                    Toast.makeText(context, "Erreur lors de l'envoi du message", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    callback(response.isSuccessful)
                    if (!response.isSuccessful) {
                        Toast.makeText(context, "Impossible d'envoyer le message", Toast.LENGTH_SHORT).show()
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
                .path("/api") // didn't update that with /backend cause it seems to work well
                .name("access_token")
                .value(accessToken)
                .build()
            listOf(cookie)
        } else {
            emptyList()
        }
    }

    // Méthode pour envoyer la photo à l'API
    fun uploadPhotoToApi(filename: String) {
        val file = File(context.filesDir, filename)

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("files", file.name, file.asRequestBody("image/jpeg".toMediaType())) //prvsioulsy was photo
            .build()

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/backend/api/photos/")
            .header("Content-Type", "multipart/form-data")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    Toast.makeText(context, "Erreur lors de l'envoi de la photo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "La photo a été envoyée avec succès", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Erreur lors de l'envoi de la photo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    fun getPhotosFromApi(imageView: ImageView) {
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/backend/api/photos/")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    Toast.makeText(context, "Erreur lors de la récupération des photos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        // Analyse la réponse JSON contenant les liens vers les photos
                        val photoUrls = parsePhotoUrls(responseData)
                        // Utilise Picasso pour charger et afficher les images
                        for (url in photoUrls) {
                            Picasso.get().load("http://10.0.2.2:8080/backend/static/$url").into(imageView)                        }
                    } else {
                        Toast.makeText(context, "Erreur lors de la récupération des photos", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun parsePhotoUrls(responseData: String?): List<String> {
        val urls = mutableListOf<String>()
        try {
            val json = JSONObject(responseData)
            val photosArray = json.getJSONArray("photos")
            for (i in 0 until photosArray.length()) {
                val photoObject = photosArray.getJSONObject(i)
                val url = photoObject.getString("photo_file_url")
                urls.add(url)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return urls
    }

}
