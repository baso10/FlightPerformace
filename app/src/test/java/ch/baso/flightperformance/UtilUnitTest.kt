package ch.baso.flightperformance

import ch.baso.flightperformance.util.Util.getDataPoints
import org.junit.Assert
import org.junit.Test

class UtilUnitTest {

    @Test
    fun getDataPoints() {
        val dataPoints = getDataPoints("-15, 179,403; -5, 186,419; 5, 193,434; 15, 200,450; 25, 207,466; 35, 214,481")
        Assert.assertEquals(6, dataPoints.size)
        var dataPoint = dataPoints[0]
        Assert.assertEquals(-15, dataPoint.temperature)
        Assert.assertEquals(179, dataPoint.run)
        Assert.assertEquals(403, dataPoint.distance)
        dataPoint = dataPoints[1]
        Assert.assertEquals(-5, dataPoint.temperature)
        Assert.assertEquals(186, dataPoint.run)
        Assert.assertEquals(419, dataPoint.distance)
    }
}