package fr.epsi.arosaje

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

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
            // Vérifier si la permission pour utiliser la caméra est accordée
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                // Demander la permission pour utiliser la caméra
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
            } else {
                // Lancer l'application de la caméra
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
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

    // Cette fonction est appelée lorsque l'utilisateur répond à la demande de permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                // Si la permission est accordée, lancer l'application de la caméra
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent.resolveActivity(packageManager) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
                return
            }
        }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val MY_PERMISSIONS_REQUEST_CAMERA = 2
    }
}
