package fr.epsi.arosaje

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.MapEventsOverlay

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        Configuration.getInstance().load(this, android.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext))

        val mapView = MapView(this)
        mapView.setTileSource(TileSourceFactory.MAPNIK)

        // Initialize map on Bordeaux
        val startPoint = GeoPoint(44.841225, -0.580036)
        mapView.controller.setCenter(startPoint)
        mapView.controller.setZoom(15.0)

        // Add a click listener to the map
        val mapEventsReceiver: MapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                val marker = Marker(mapView)
                marker.position = p
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapView.overlays.add(marker)
                // Store the marker location
                val sharedPref = getPreferences(Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString(p.latitude.toString(), p.longitude.toString())
                    apply()
                }
                mapView.invalidate()  // Redraw the map
                return true
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                return false
            }
        }

        val overlayEvents = MapEventsOverlay(mapEventsReceiver)
        mapView.overlays.add(overlayEvents)

        // Load stored markers
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        sharedPref.all.keys.forEach { lat ->
            val lon = sharedPref.getString(lat, "")?.toDoubleOrNull()
            if (lon != null) {
                val marker = Marker(mapView)
                marker.position = GeoPoint(lat.toDouble(), lon)
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapView.overlays.add(marker)
            }
        }

        val mapContainer = findViewById<FrameLayout>(R.id.map_container)
        mapContainer.addView(mapView)
    }
}
