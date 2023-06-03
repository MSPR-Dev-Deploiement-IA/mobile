package fr.epsi.arosaje

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

class PhotoAdapter(private val context: Context, private val dataSource: List<String>) : BaseAdapter() {

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

        val photoUrl = getItem(position) as String?
        if (photoUrl != null) {
            Picasso.get().load(photoUrl).into(holder.thumbnailImageView)
        } else {
            // handle null photoUrl here, you can set a default image or make the ImageView invisible
            // for example:
            holder.thumbnailImageView.setImageResource(R.drawable.gallery_one) // replace default_image with your actual default image resource
        }

        return view
    }


    private class ViewHolder {
        lateinit var thumbnailImageView: ImageView
    }
}
