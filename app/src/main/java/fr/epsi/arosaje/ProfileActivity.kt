package fr.epsi.arosaje

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Récupération du header
        val header: View = findViewById(R.id.header_layout)
        val backButton: ImageView = header.findViewById(R.id.back_button)
        val titleTextView: TextView = header.findViewById(R.id.title_textview)

        // Définition du titre et du listener pour le bouton de retour
        titleTextView.text = "Mon profil"
        backButton.setOnClickListener { finish() }

        // Récupération des boutons depuis la vue
        val photosButton: Button = findViewById(R.id.photos_button)
        val historiqueButton: Button = findViewById(R.id.history_button)
        val favorisButton: Button = findViewById(R.id.favorites_button)
        val modifierProfilButton: Button = findViewById(R.id.edit_profile_button)

        // Ajout des listeners pour chaque bouton
        photosButton.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }

        historiqueButton.setOnClickListener {
            // Action à effectuer lors du clic sur le bouton "Historique"
        }

        favorisButton.setOnClickListener {
            // Action à effectuer lors du clic sur le bouton "Favoris"
        }

        modifierProfilButton.setOnClickListener {
            // Action à effectuer lors du clic sur le bouton "Modifier Profil"
        }
    }
}