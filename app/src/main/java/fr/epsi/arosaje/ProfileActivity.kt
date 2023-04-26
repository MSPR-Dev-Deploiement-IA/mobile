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

        val header: View = findViewById(R.id.header_layout)
        val backButton: ImageView = header.findViewById(R.id.back_button)
        val titleTextView: TextView = header.findViewById(R.id.title_textview)

        titleTextView.text = "Mon profil"
        backButton.setOnClickListener { finish() }

        val photosButton: Button = findViewById(R.id.photos_button)
        val historiqueButton: Button = findViewById(R.id.history_button)
        val favorisButton: Button = findViewById(R.id.favorites_button)
        val modifierProfilButton: Button = findViewById(R.id.edit_profile_button)

        photosButton.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }

        historiqueButton.setOnClickListener {
            //histo
        }

        favorisButton.setOnClickListener {
        }

        modifierProfilButton.setOnClickListener {
            val intent = Intent(this, UpdateprofileActivity::class.java)
            startActivity(intent)
        }
    }
}