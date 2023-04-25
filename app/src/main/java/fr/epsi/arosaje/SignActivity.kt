package fr.epsi.arosaje

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)

        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            // Vérifier que les champs sont remplis et que les mots de passe correspondent
            if (username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword) {
                // TODO: Enregistrer les informations de l'utilisateur (par exemple dans une base de données)

                // Ouvrir l'activité de connexion (LoginActivity)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                // Afficher un message d'erreur
                Toast.makeText(this, "Veuillez remplir tous les champs et vérifier que les mots de passe correspondent", Toast.LENGTH_SHORT).show()
            }
        }
    }
}