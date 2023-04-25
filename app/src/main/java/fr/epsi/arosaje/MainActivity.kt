package fr.epsi.arosaje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val deconnexionButton: Button = findViewById(R.id.deconnexion_button)
        deconnexionButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val carteButton: Button = findViewById(R.id.carte_button)
        val photoButton: Button = findViewById(R.id.photo_button)
        val conseilsButton: Button = findViewById(R.id.conseils_button)
        val chatButton: Button = findViewById(R.id.chat_button)
        val profilButton: Button = findViewById(R.id.profil_button)

        carteButton.setOnClickListener {
        }

        photoButton.setOnClickListener {
        }

        conseilsButton.setOnClickListener {
        }

        chatButton.setOnClickListener {
        }

        profilButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
