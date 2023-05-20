package fr.epsi.arosaje

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class LabelledMarker(map: MapView?, text: String) : Marker(map) {
    var text = ""

    init {
        this.text = text
    }

    override fun draw(
        canvas: Canvas?,
        mapView: MapView?,
        shadow: Boolean
    ) {
        super.draw(canvas, mapView, shadow)

        val x = mPositionPixels.x.toFloat()
        val y = mPositionPixels.y.toFloat()

        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 30f
        paint.isFakeBoldText = true

        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val textHeight = bounds.height()
        val textWidth = bounds.width()

        val iconHeight = mIcon.bounds.height()  // Get the icon height

        canvas?.drawText(
            text,
            x - textWidth / 2,
            y - iconHeight - textHeight,  // Adjust the y-position of the text
            paint
        )
    }
}
