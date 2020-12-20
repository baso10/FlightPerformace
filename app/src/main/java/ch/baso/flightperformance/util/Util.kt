package ch.baso.flightperformance.util

import android.widget.ArrayAdapter
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import ch.baso.flightperformance.model.DataPoint
import java.util.*


object Util {

    @JvmStatic
    fun getDataPoints(line: String): List<DataPoint> {
        val result: MutableList<DataPoint> = ArrayList()
        val split = line.split(";").toTypedArray()
        for (dataSet in split) {
            val dataPoints = dataSet.split(",").toTypedArray()
            val dataPoint = DataPoint(dataPoints[0].trim { it <= ' ' }.toInt(), dataPoints[1].trim { it <= ' ' }.toInt(), dataPoints[2].trim { it <= ' ' }.toInt())
            result.add(dataPoint)
        }
        return result
    }
}