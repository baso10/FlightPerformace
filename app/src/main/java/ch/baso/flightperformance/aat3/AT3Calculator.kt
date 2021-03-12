package ch.baso.flightperformance.aat3

import ch.baso.flightperformance.calc.BaseCalculator
import ch.baso.flightperformance.calc.Calculator
import ch.baso.flightperformance.model.Airplane
import ch.baso.flightperformance.model.DataPoint
import ch.baso.flightperformance.util.Util

class AT3Calculator : BaseCalculator() {
    override fun calculateLanding(airplane: Airplane, pressure: Int, temperature: Int, totalWeight: Double): Calculator.TakeoffLanding {
        val lines = airplane.landingPerformance
        val dataPoints0 = Util.getDataPoints(lines[0])
        val dataPoints1460 = Util.getDataPoints(lines[1])
        val dataPoints3281 = Util.getDataPoints(lines[2])
        val dataPoints4921 = Util.getDataPoints(lines[3])
        val dataPoints6562 = Util.getDataPoints(lines[4])
        val result = if (pressure < 1460) {
            val diffPercent = ((pressure - 0) / (1460 - 0).toFloat()).toDouble()
            calcWithPoints(dataPoints0, dataPoints1460, diffPercent, temperature)
        } else if (pressure < 3281) {
            val diffPercent = ((pressure - 1460) / (3281 - 1460).toFloat()).toDouble()
            calcWithPoints(dataPoints1460, dataPoints3281, diffPercent, temperature)
        } else if (pressure < 4921) {
            val diffPercent = ((pressure - 3281) / (4921 - 3281).toFloat()).toDouble()
            calcWithPoints(dataPoints3281, dataPoints4921, diffPercent, temperature)
        } else if (pressure < 6562) {
            val diffPercent = ((pressure - 4921) / (6562 - 4921).toFloat()).toDouble()
            calcWithPoints(dataPoints4921, dataPoints6562, diffPercent, temperature)
        } else {
            val diffPercent = 0.0
            calcWithPoints(dataPoints6562, dataPoints6562, diffPercent, temperature)
        }
        return result
    }

    override fun calculateTakeoff(airplane: Airplane, pressure: Int, temperature: Int, totalWeight: Double): Calculator.TakeoffLanding {
        val lines = airplane.takeOffPerformance
        val dataPoints0 = Util.getDataPoints(lines[0])
        val dataPoints1460 = Util.getDataPoints(lines[1])
        val dataPoints3281 = Util.getDataPoints(lines[2])
        val dataPoints4921 = Util.getDataPoints(lines[3])
        val dataPoints6562 = Util.getDataPoints(lines[4])

        val result = if (pressure < 1460) {
            val diffPercent = ((pressure - 0) / (1460 - 0).toFloat()).toDouble()
            calcWithPoints(dataPoints0, dataPoints1460, diffPercent, temperature)
        } else if (pressure < 3281) {
            val diffPercent = ((pressure - 1460) / (3281 - 1460).toFloat()).toDouble()
            calcWithPoints(dataPoints1460, dataPoints3281, diffPercent, temperature)
        } else if (pressure < 4921) {
            val diffPercent = ((pressure - 3281) / (4921 - 3281).toFloat()).toDouble()
            calcWithPoints(dataPoints3281, dataPoints4921, diffPercent, temperature)
        } else if (pressure < 6562) {
            val diffPercent = ((pressure - 4921) / (6562 - 4921).toFloat()).toDouble()
            calcWithPoints(dataPoints4921, dataPoints6562, diffPercent, temperature)
        } else {
            val diffPercent = 0.0
            calcWithPoints(dataPoints6562, dataPoints6562, diffPercent, temperature)
        }
        return result
    }

    private fun calcWithPoints(dataPoints1: List<DataPoint>, dataPoints2: List<DataPoint>, diffPercent: Double, temperature: Int): Calculator.TakeoffLanding {
        //take value from pressure 0 and add diff to 1460
        var startingDataPoint: DataPoint? = null
        var startingDataPointTempNext: DataPoint? = null
        val dataPointListIterator = dataPoints1.listIterator()
        while (dataPointListIterator.hasNext()) {
            val dp = dataPointListIterator.next()
            if (temperature >= dp.temperature) {
                startingDataPoint = dp
                startingDataPointTempNext = if (dataPointListIterator.hasNext()) dataPointListIterator.next() else null
                if (startingDataPointTempNext != null && dataPointListIterator.hasPrevious()) {
                    dataPointListIterator.previous()
                }
            }
        }
        var startingNextDataPoint: DataPoint? = null
        var startingNextDataPointTempNext: DataPoint? = null
        val dataPointListIterator1 = dataPoints2.listIterator()
        while (dataPointListIterator1.hasNext()) {
            val dp = dataPointListIterator1.next()
            if (temperature >= dp.temperature) {
                startingNextDataPoint = dp
                startingNextDataPointTempNext = if (dataPointListIterator1.hasNext()) dataPointListIterator1.next() else null
                if (startingNextDataPointTempNext != null && dataPointListIterator1.hasPrevious()) {
                    dataPointListIterator1.previous()
                }
            }
        }

        //temperature 1 diff add
        var startingDataPointLandingRun = startingDataPoint!!.run
        var startingDataPointLandingDistance = startingDataPoint.distance
        if (startingDataPointTempNext != null) {
            var diffTemperature = (temperature - startingDataPoint.temperature).toDouble()
            diffTemperature = diffTemperature / (startingDataPointTempNext.temperature - startingDataPoint.temperature).toDouble()
            val landingRunDiff = ((startingDataPointTempNext.run - startingDataPoint.run) * diffTemperature).toInt()
            val landingDistanceDiff = ((startingDataPointTempNext.distance - startingDataPoint.distance) * diffTemperature).toInt()
            startingDataPointLandingRun += landingRunDiff
            startingDataPointLandingDistance += landingDistanceDiff
        }

        //temperature 2 diff add
        var startingNextDataPointLandingRun = startingNextDataPoint!!.run
        var startingNextDataPointLandingDistance = startingNextDataPoint.distance
        if (startingNextDataPointTempNext != null) {
            var diffTemperature = (temperature - startingNextDataPoint.temperature).toDouble()
            diffTemperature = diffTemperature / (startingNextDataPointTempNext.temperature - startingNextDataPoint.temperature).toDouble()
            val landingRunDiff = ((startingNextDataPointTempNext.run - startingNextDataPoint.run) * diffTemperature).toInt()
            val landingDistanceDiff = ((startingNextDataPointTempNext.distance - startingNextDataPoint.distance) * diffTemperature).toInt()
            startingNextDataPointLandingRun += landingRunDiff
            startingNextDataPointLandingDistance += landingDistanceDiff
        }

        //diff add
        val landingRunDiff = ((startingNextDataPointLandingRun - startingDataPointLandingRun) * diffPercent).toInt()
        val landingDistanceDiff = ((startingNextDataPointLandingDistance - startingDataPointLandingDistance) * diffPercent).toInt()
        val run = startingDataPointLandingRun + landingRunDiff
        val distance = startingDataPointLandingDistance + landingDistanceDiff

        return Calculator.TakeoffLanding(run, distance)
    }
}