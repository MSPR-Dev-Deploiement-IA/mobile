package fr.epsi.arosaje

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.system.Os.close
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MapActivity : AppCompatActivity() {
    private var isResetButtonClicked = false
    private lateinit var mapView: MapView
    private lateinit var overlayEvents: MapEventsOverlay
    private lateinit var mapEventsReceiver: MapEventsReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        Configuration.getInstance().load(
            this,
            android.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        mapView = MapView(this)
        mapView.setTileSource(TileSourceFactory.MAPNIK)

        // Initialize map on Bordeaux
        val startPoint = GeoPoint(44.841225, -0.580036)
        mapView.controller.setCenter(startPoint)
        mapView.controller.setZoom(15.0)

        val mapContainer = findViewById<FrameLayout>(R.id.map_container)
        mapContainer.addView(mapView)

        // Load stored markers
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        sharedPref.all.keys.forEach { lat ->
            val lon = sharedPref.getString(lat, "")?.toDoubleOrNull()
            if (lon != null) {
                val marker = LabelledMarker(mapView, "")
                marker.position = GeoPoint(lat.toDouble(), lon)
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapView.overlays.add(marker)
            }
        }

        // Initialize the reset button and add a click listener
        val resetButton = findViewById<Button>(R.id.reset_btn)
        resetButton.setOnClickListener {
            // Clear all markers from the map
            mapView.overlays.clear()
            mapView.invalidate()  // Redraw the map

            // Clear all markers from shared preferences
            with(sharedPref.edit()) {
                clear()
                apply()
            }

            // Set the flag to indicate that the reset button was clicked
            isResetButtonClicked = true

            // Re-initialize the map events receiver
            mapView.overlays.remove(overlayEvents)
            overlayEvents = MapEventsOverlay(mapEventsReceiver)
            mapView.overlays.add(overlayEvents)

            // Reset the flag
            isResetButtonClicked = false

            // Relaunch MapActivity
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Add a click listener to the map
        mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                if (isResetButtonClicked) {
                    // Ignore the click event if the reset button was clicked
                    return false
                }

                // Create a marker at the clicked location
                val marker = LabelledMarker(mapView, "")
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
                            marker.text = text
                            mapView.invalidate()  // Force redraw to show new text
                            close()
                        }

                        btnDelete.setOnClickListener {
                            // Remove the marker
                            mapView.overlays.remove(marker)
                            // And remove it from shared preferences
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

        overlayEvents = MapEventsOverlay(mapEventsReceiver)
        mapView.overlays.add(overlayEvents)
    }
}
