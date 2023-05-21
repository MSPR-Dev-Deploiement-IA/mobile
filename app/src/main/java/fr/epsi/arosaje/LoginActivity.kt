package fr.epsi.arosaje


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiService = ApiService(this)

        val editTextMail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonSubmit)
        val textViewNoAccount = findViewById<TextView>(R.id.textViewNoAccount)

        buttonLogin.setOnClickListener {
            val email = editTextMail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                apiService.login(email, password) { success ->
                    if (success) {
                        // Connexion réussie, effectuez les actions nécessaires
                        Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SplashloginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Erreur lors de la connexion", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

        textViewNoAccount.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }
    }
}
