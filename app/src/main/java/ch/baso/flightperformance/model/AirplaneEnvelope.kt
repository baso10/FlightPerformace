package ch.baso.flightperformance.model

import android.graphics.Point

class AirplaneEnvelope {
    var rect: Rect = Rect(0.0, 0.0, 0.0, 0.0)
    var allowed: List<Point> = ArrayList()
    var macLe: Double = 0.0
    var mac: Double = 0.0

    data class Rect(val bottom: Double, val top: Double, val left: Double, val right: Double)
}