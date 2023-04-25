package fr.epsi.arosaje

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private val userDatabase = mutableMapOf(
        "marco" to "1234",
        "germain" to "1234",
        "thomas" to "1234",
        "max" to "1234",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonSubmit)
        val textViewNoAccount = findViewById<TextView>(R.id.textViewNoAccount)

        val intentKeys = IntentKeys
        val username = intent.getStringExtra(intentKeys.EXTRA_USERNAME)
        val password = intent.getStringExtra(intentKeys.EXTRA_PASSWORD)

        // Récupérer les informations de l'utilisateur ajouté dans la SignActivity
        val intent = intent
        val newUsername = intent.getStringExtra(intentKeys.EXTRA_USERNAME)
        val newPassword = intent.getStringExtra(intentKeys.EXTRA_PASSWORD)
        if (newUsername != null && newPassword != null) {
            userDatabase[newUsername] = newPassword
        }

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            if (userDatabase.containsKey(username) && userDatabase[username] == password) {
                // Les informations d'identification sont valides, connectez l'utilisateur
                Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
            } else {
                // Les informations d'identification sont invalides, afficher un message d'erreur
                Toast.makeText(this, "Nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_SHORT).show()
            }
        }

        textViewNoAccount.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignActivity::class.java)
            startActivity(intent)
        }
    }
}