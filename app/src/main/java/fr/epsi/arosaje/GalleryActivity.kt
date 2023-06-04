package fr.epsi.arosaje

import PhotoAdapter
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity

class GalleryActivity : AppCompatActivity() {
    private lateinit var gridView: GridView
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        gridView = findViewById(R.id.gridview)
        apiService = ApiService(this)

        apiService.getPhotos { photos ->
            runOnUiThread {
                if (photos != null) {
                    gridView.adapter = PhotoAdapter(this, photos)
                } else {
                    // Handle error
                }
            }
        }
    }
}
