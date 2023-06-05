import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import fr.epsi.arosaje.Photo
import fr.epsi.arosaje.R

class PhotoAdapter(private val context: Context, private val dataSource: List<Photo>) : BaseAdapter() {
    private val prodUrl = "https://mspr-arosaje.francecentral.cloudapp.azure.com"
    private val localUrl = "http://10.0.2.2:8080"
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_photo, parent, false)
            holder = ViewHolder()
            holder.thumbnailImageView = view.findViewById(R.id.photo_imageview) as ImageView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val photo = getItem(position) as Photo
        Picasso.get().load("$prodUrl/backend/static/" + photo.photo_file_url).into(holder.thumbnailImageView) // "http://10.0.2.2:8080/backend/static/"

        return view
    }

    private class ViewHolder {
        lateinit var thumbnailImageView: ImageView
    }
}
