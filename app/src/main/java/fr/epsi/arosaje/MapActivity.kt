package fr.epsi.arosaje

import android.content.Context
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
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
                // Create a marker at the clicked location
                val marker = Marker(mapView)
                marker.position = p
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.infoWindow = object : InfoWindow(R.layout.bubble, mapView) {
                    override fun onOpen(item: Any?) {
                        val et = mView.findViewById<EditText>(R.id.bubble_text)
                        val btn = mView.findViewById<Button>(R.id.bubble_btn)
                        val btnDelete = mView.findViewById<Button>(R.id.bubble_delete)

                        btn.setOnClickListener {
                            // Store your text here
                            val text = et.text.toString()
                            close()
                        }

                        btnDelete.setOnClickListener {
                            // Remove the marker
                            mapView.overlays.remove(marker)
                            // And remove it from shared preferences
                            val sharedPref = getPreferences(Context.MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                remove(marker.position.latitude.toString())
                                apply()
                            }
                            mapView.invalidate()  // Redraw the map
                            close()
                        }
                    }

                    override fun onClose() {}
                }
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
