package fr.epsi.arosaje

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class LabelledMarker(
    map: MapView?,
    text: String,
    accuracy: Float = 0f,
    private val textColor: Int = Color.GREEN,
    private val textSize: Float = 80f,
    private val textStyle: Int = Typeface.BOLD,
    private val backgroundPadding: Float = 10f
) : Marker(map) {
    var text = ""
    var accuracy = accuracy

    init {
        this.text = text
    }

    override fun draw(canvas: Canvas?, mapView: MapView?, shadow: Boolean) {
        super.draw(canvas, mapView, shadow)

        val x = mPositionPixels.x.toFloat()
        val y = mPositionPixels.y.toFloat()

        val paint = Paint()
        paint.color = textColor
        paint.textSize = textSize
        paint.isFakeBoldText = textStyle and Typeface.BOLD != 0

        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val textHeight = bounds.height()
        val textWidth = bounds.width()

        val iconHeight = mIcon.bounds.height()  // Get the icon height

        val backgroundWidth = textWidth + backgroundPadding * 2
        val backgroundHeight = textHeight + backgroundPadding * 2

        val textX = x - textWidth / 2
        val textY = y + iconHeight.toFloat() + textHeight.toFloat() + 20f  // Adjust the additional offset as needed

        // Draw white background
        val backgroundPaint = Paint()
        backgroundPaint.color = Color.WHITE
        canvas?.drawRect(
            x - backgroundWidth / 2,
            textY - backgroundHeight,
            x + backgroundWidth / 2,
            textY,
            backgroundPaint
        )

        // Draw text
        canvas?.drawText(
            text,
            textX,
            textY,
            paint
        )
    }
}
