package fr.epsi.arosaje

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Récupération des boutons depuis la vue
        val carteButton: Button = findViewById(R.id.carte_button)
        val photoButton: Button = findViewById(R.id.photo_button)
        val conseilsButton: Button = findViewById(R.id.conseils_button)
        val chatButton: Button = findViewById(R.id.chat_button)
        val profilButton: Button = findViewById(R.id.profil_button)

        // Ajout des listeners pour chaque bouton
        carteButton.setOnClickListener {
            // Action à effectuer lors du clic sur le bouton "Carte"
        }

        photoButton.setOnClickListener {
            // Action à effectuer lors du clic sur le bouton "Photo"
        }

        conseilsButton.setOnClickListener {
            // Action à effectuer lors du clic sur le bouton "Conseils"
        }

        chatButton.setOnClickListener {
            // Action à effectuer lors du clic sur le bouton "Chat"
        }

        profilButton.setOnClickListener {
            // Action à effectuer lors du clic sur le bouton "Profil"
        }
    }
}
