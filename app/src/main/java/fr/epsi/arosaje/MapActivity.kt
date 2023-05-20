package fr.epsi.arosaje

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
    private lateinit var mapView: MapView
    private lateinit var overlayEvents: MapEventsOverlay
    private lateinit var mapEventsReceiver: MapEventsReceiver
    private val markers: MutableList<LabelledMarker> = mutableListOf()

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

        // Add a click listener to the map
        mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                // Check if the clicked location is within an existing marker
                for (marker in markers) {
                    val distance = p.distanceToAsDouble(marker.position)
                    if (distance <= marker.accuracy) {
                        // Handle marker click here
                        // For example, show a dialog or start a new activity
                        return true
                    }
                }

                // Create a new marker at the clicked location with accuracy value
                val marker = LabelledMarker(mapView, "", accuracy = 10f) // Set the accuracy value as desired
                marker.position = p
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.infoWindow = object : InfoWindow(R.layout.bubble, mapView) {
                    override fun onOpen(item: Any?) {
                        val et = mView.findViewById<EditText>(R.id.bubble_text)
                        val btn = mView.findViewById<Button>(R.id.bubble_btn)
                        val btnDelete = mView.findViewById<Button>(R.id.bubble_delete)

                        et.setText(marker.text)

                        btn.setOnClickListener {
                            val newText = et.text.toString()
                            marker.text = newText
                            mapView.invalidate()
                            close()

                            // Store the updated marker text to shared preferences
                            with(getPreferences(Context.MODE_PRIVATE).edit()) {
                                putString(marker.position.latitude.toString(), "${marker.position.longitude},${newText}")
                                apply()
                            }
                        }

                        btnDelete.setOnClickListener {
                            mapView.overlays.remove(marker)
                            markers.remove(marker)
                            mapView.invalidate()
                            close()

                            // Remove the marker from shared preferences
                            with(getPreferences(Context.MODE_PRIVATE).edit()) {
                                remove(marker.position.latitude.toString())
                                apply()
                            }
                        }
                    }

                    override fun onClose() {}
                }

                // Add the new marker to the map and the list of markers
                mapView.overlays.add(marker)
                markers.add(marker)
                return true
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                return false
            }
        }

        overlayEvents = MapEventsOverlay(mapEventsReceiver)
        mapView.overlays.add(overlayEvents)

        // Load stored markers
        loadMarkersFromPreferences()

        // Initialize the reset button and add a click listener
        val resetButton = findViewById<Button>(R.id.reset_btn)
        resetButton.setOnClickListener {
            // Clear all markers from the map and the list of markers
            mapView.overlays.removeAll(markers)
            markers.clear()
            mapView.invalidate()  // Redraw the map

            // Clear all markers from shared preferences
            with(getPreferences(Context.MODE_PRIVATE).edit()) {
                clear()
                apply()
            }
        }
    }

    private fun loadMarkersFromPreferences() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        sharedPref.all.keys.forEach { lat ->
            val data = sharedPref.getString(lat, "")?.split(',')
            if (data != null && data.size == 2) {
                val lon = data[0].toDoubleOrNull()
                val text = data[1]
                if (lon != null) {
                    val marker = LabelledMarker(mapView, text)
                    marker.position = GeoPoint(lat.toDouble(), lon)
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.infoWindow = object : InfoWindow(R.layout.bubble, mapView) {
                        override fun onOpen(item: Any?) {
                            val et = mView.findViewById<EditText>(R.id.bubble_text)
                            val btn = mView.findViewById<Button>(R.id.bubble_btn)
                            val btnDelete = mView.findViewById<Button>(R.id.bubble_delete)

                            et.setText(marker.text)

                            btn.setOnClickListener {
                                val newText = et.text.toString()
                                marker.text = newText
                                mapView.invalidate()
                                close()

                                // Store the updated marker text to shared preferences
                                with(getPreferences(Context.MODE_PRIVATE).edit()) {
                                    putString(lat, "${lon},${newText}")
                                    apply()
                                }
                            }

                            btnDelete.setOnClickListener {
                                mapView.overlays.remove(marker)
                                markers.remove(marker)
                                mapView.invalidate()
                                close()

                                // Remove the marker from shared preferences
                                with(getPreferences(Context.MODE_PRIVATE).edit()) {
                                    remove(lat)
                                    apply()
                                }
                            }
                        }

                        override fun onClose() {}
                    }
                    mapView.overlays.add(marker)
                    markers.add(marker)
                }
            }
        }
        mapView.invalidate()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onBackPressed() {
        saveMarkersToPreferences()
        super.onBackPressed()
    }

    private fun saveMarkersToPreferences() {
        with(getPreferences(Context.MODE_PRIVATE).edit()) {
            clear()
            markers.forEach { marker ->
                putString(
                    marker.position.latitude.toString(),
                    "${marker.position.longitude},${marker.text}"
                )
            }
            apply()
        }
    }
}
