package fr.epsi.arosaje

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashloginActivity : AppCompatActivity()  {

    private val SPLASH_TIME_OUT:Long = 3000 // 3 secondes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashlogin)

        Handler().postDelayed({
            // Cette méthode sera exécutée une fois que le délai sera écoulé
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}