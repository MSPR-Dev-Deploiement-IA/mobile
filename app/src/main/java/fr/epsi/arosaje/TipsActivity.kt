package fr.epsi.arosaje

import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TipsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)

        // Initialise le texte centré
        val tipsTextView = findViewById<TextView>(R.id.tips_text)
        tipsTextView.gravity = Gravity.CENTER
    }
}
