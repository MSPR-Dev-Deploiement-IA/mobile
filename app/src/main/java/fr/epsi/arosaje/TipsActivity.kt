package fr.epsi.arosaje

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TipsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)

        // Récupération du header
        val header: View = findViewById(R.id.header_layout)
        val backButton: ImageView = header.findViewById(R.id.back_button)
        val titleTextView: TextView = header.findViewById(R.id.title_textview)

        // Définition du titre et du listener pour le bouton de retour
        titleTextView.text = "Conseils"
        backButton.setOnClickListener { finish() }

        // Initialise le texte centré
        val tipsTextView = findViewById<TextView>(R.id.tips_text)
        tipsTextView.gravity = Gravity.CENTER
    }
}
