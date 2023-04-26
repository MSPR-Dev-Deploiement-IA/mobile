package fr.epsi.arosaje

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import fr.epsi.arosaje.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Initialise OsmDroid
        Configuration.getInstance().load(this, android.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext))

        // Crée une instance de la carte OsmDroid
        val mapView = MapView(this)
        mapView.setTileSource(TileSourceFactory.MAPNIK)

        // Ajoute la carte au conteneur
        val mapContainer = findViewById<FrameLayout>(R.id.map_container)
        mapContainer.addView(mapView)
    }
}
