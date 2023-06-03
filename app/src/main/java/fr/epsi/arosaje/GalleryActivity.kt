package fr.epsi.arosaje

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class GalleryActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private val imageUrls = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val gridView = findViewById<GridView>(R.id.gallery_gridview)
        apiService = ApiService(this)

        // Assuming getPhotosFromApi() returns a List<String>
        val imageUrls = apiService.getPhotosFromApi()

        gridView.adapter = ImageAdapter(this, imageUrls)
    }
}

class ImageAdapter(private val context: Context, private val imageUrls: List<String>) : BaseAdapter() {

    override fun getCount() = imageUrls.size

    override fun getItem(position: Int) = imageUrls[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = ImageView(context)
            imageView.adjustViewBounds = true
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        } else {
            imageView = convertView as ImageView
        }

        // Using Picasso to load the image
        Picasso.get()
            .load(getItem(position))
            .fit()
            .centerCrop()
            .into(imageView)

        return imageView
    }
}



