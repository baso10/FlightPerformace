package ch.baso.flightperformance.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import ch.baso.flightperformance.model.AirplaneEnvelope
import java.util.*

class EnvelopeView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val TAG = EnvelopeView::class.java.simpleName
    var points: MutableList<Point> = ArrayList()
    var topMax: Double = 0.0;
    var bottomMax: Double = 0.0;
    var leftMax: Double = 0.0;
    var rightMax: Double = 0.0;
    private var paintLines: Paint = Paint()
    private var paintCenterPoint: Paint = Paint()
    private var mFrame: Rect = Rect()
    private var totalWeight: Double = 0.0
    private var totalMoment: Double = 0.0

    private fun setupPaint() {

        val paintLines = Paint()
        paintLines.color = Color.BLACK
        paintLines.isAntiAlias = true
        paintLines.strokeWidth = 5f
        paintLines.style = Paint.Style.STROKE
        paintLines.strokeJoin = Paint.Join.ROUND
        paintLines.strokeCap = Paint.Cap.ROUND
        this.paintLines = paintLines

        val paintCenterPoint = Paint()
        paintCenterPoint.color = Color.RED
        paintCenterPoint.style = Paint.Style.FILL
        this.paintCenterPoint = paintCenterPoint
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d(TAG, "  onDraw called")

        val maxWidth = rightMax - leftMax
        val maxHeight = topMax - bottomMax

        val screenWidth = width
        val screenHeight = height


        // Draw a frame around the picture.
        canvas.drawRect(mFrame, paintLines)
        val path = Path()
        var firstPoint = Point()
        for ((index, point) in points.withIndex()) {
            val x = (screenWidth * ((point.x - leftMax) / maxWidth)).toInt()
            val y = (screenHeight * ((topMax - point.y) / maxHeight)).toInt()
            if (index == 0) {
                firstPoint = point
                path.moveTo(x.toFloat(), y.toFloat())
            } else {
                path.lineTo(x.toFloat(), y.toFloat())
            }
        }
        if (firstPoint != null) {
            val x = (screenWidth * ((firstPoint.x - leftMax) / maxWidth)).toInt()
            val y = (screenHeight * ((topMax - firstPoint.y) / maxHeight)).toInt()
            path.lineTo(x.toFloat(), y.toFloat())
        }
        canvas.drawPath(path, paintLines)


        val x = (screenWidth * ((totalMoment - leftMax) / maxWidth)).toInt()
        val y = (screenHeight * ((topMax - totalWeight) / maxHeight)).toInt()
        canvas.drawCircle(x.toFloat(), y.toFloat(), 20f, paintCenterPoint)
    }

    /**
     * Note: Called whenever the view changes size.
     * Since the view starts out with no size, this is also called after
     * the view has been inflated and has a valid size.
     */
    override fun onSizeChanged(width: Int, height: Int,
                               oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)


        // Calculate the rect a frame around the picture.
        val inset = 0
        mFrame = Rect(inset, inset, width - inset, height - inset)
    }

    fun setMassBalance(totalWeight: Double, totalMoment: Double) {
        var totalWeightVal = totalWeight
        if (totalWeightVal > topMax) {
            totalWeightVal = topMax;
        }
        if (totalWeightVal < bottomMax) {
            totalWeightVal = bottomMax;
        }
        var totalMomentVal = totalMoment
        if (totalMomentVal > rightMax) {
            totalMomentVal = rightMax;
        }
        if (totalMomentVal < leftMax) {
            totalMomentVal = leftMax;
        }
        this.totalWeight = totalWeightVal
        this.totalMoment = totalMomentVal
        invalidate()
    }

    fun setBounds(envelope: AirplaneEnvelope) {

        this.bottomMax = envelope.rect.bottom
        this.topMax = envelope.rect.top
        this.leftMax = envelope.rect.left
        this.rightMax = envelope.rect.right

        points.clear()
        points.addAll(envelope.allowed)
        invalidate()
    }

    init {
        setupPaint()
    }
}