package ch.baso.flightperformance.bristell

import ch.baso.flightperformance.calc.BaseCalculator
import ch.baso.flightperformance.calc.Calculator
import ch.baso.flightperformance.model.Airplane
import ch.baso.flightperformance.model.AirplaneMassBalance
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator

open class BristellB23Calculator : BaseCalculator() {

    override fun calculateTakeoff(airplane: Airplane, pressure: Int, temperature: Int, totalWeight: Double): Calculator.TakeoffLanding {
        val lines = airplane.takeOffPerformance

        return calculateTakeoffAndLanding(lines, airplane, pressure, temperature, totalWeight)
    }

    override fun calculateLanding(airplane: Airplane, pressure: Int, temperature: Int, totalWeight: Double): Calculator.TakeoffLanding {
        val lines = airplane.landingPerformance

        return calculateTakeoffAndLanding(lines, airplane, pressure, temperature, totalWeight)
    }
    private fun calculateTakeoffAndLanding(performance: List<String>, airplane: Airplane, pressure: Int, temperature: Int, totalWeight: Double): Calculator.TakeoffLanding {
        val lines = performance

        val sectionIndex = 1
        val pressure1 = if (pressure <= 0) 0 else if (pressure > 6000) 6000 else pressure
        val temperature1 = if (temperature > 35) 35 else if (temperature < -5) -5 else temperature

        val pressureDataPoints = listOf(0, 2000, 4000, 6000)
        val temperatureDataPoints = listOf(-5, -5, 5, 5, 15, 15, 25, 25, 35, 35)
        val tempRunMap = mutableMapOf<Int, MutableList<Int>>(-5 to mutableListOf(), 5 to mutableListOf(), 15 to mutableListOf(), 25 to mutableListOf(), 35 to mutableListOf())
        val tempDistanceMap = mutableMapOf<Int, MutableList<Int>>(-5 to mutableListOf(), 5 to mutableListOf(), 15 to mutableListOf(), 25 to mutableListOf(), 35 to mutableListOf())

        for ((i, press1) in pressureDataPoints.withIndex()) {
            val lineIndex = (sectionIndex - 1) * 9 + i

            val lineArray = lines[lineIndex].split(",")
            for (i in 0..lineArray.size - 2 step 2) {
                val temp = temperatureDataPoints[i]

                val val1 = lineArray[i].trim().toInt() //run
                val val2 = lineArray[i + 1].trim().toInt() //distance
                tempRunMap[temp]?.add(val1)
                tempDistanceMap[temp]?.add(val2)
            }
        }

        //select temp column
        val temperatureIndexes = when {
            temperature1 < -5 -> Pair(-5, -5)
            temperature1 in -5..5 -> Pair(-5, 5)
            temperature1 in 6..15 -> Pair(5, 15)
            temperature1 in 16..25 -> Pair(15, 25)
            temperature1 in 26..35 -> Pair(25, 35)
            else -> Pair(35, 35)
        }

        val firstDataRun = tempRunMap[temperatureIndexes.first]
        val secondDataRun = tempRunMap[temperatureIndexes.second]
        val firstDataDist = tempDistanceMap[temperatureIndexes.first]
        val secondDataDist = tempDistanceMap[temperatureIndexes.second]


        val sp = SplineInterpolator()
        val interpolate1 = sp.interpolate(
                pressureDataPoints.map { t -> t.toDouble() }.toDoubleArray(),
                firstDataRun?.map { t -> t.toDouble() }?.toDoubleArray()
        )
        val interpolate2 = sp.interpolate(
                pressureDataPoints.map { t -> t.toDouble() }.toDoubleArray(),
                secondDataRun?.map { t -> t.toDouble() }?.toDoubleArray()
        )

        val interpolate1Dist = sp.interpolate(
                pressureDataPoints.map { t -> t.toDouble() }.toDoubleArray(),
                firstDataDist?.map { t -> t.toDouble() }?.toDoubleArray()
        )
        val interpolate2Dist = sp.interpolate(
                pressureDataPoints.map { t -> t.toDouble() }.toDoubleArray(),
                secondDataDist?.map { t -> t.toDouble() }?.toDoubleArray()
        )

        val runValue1 = interpolate1.value(pressure1.toDouble())
        val runValue2 = interpolate2.value(pressure1.toDouble())
        val distValue1 = interpolate1Dist.value(pressure1.toDouble())
        val distValue2 = interpolate2Dist.value(pressure1.toDouble())


        val tempDiff = (temperature1 - temperatureIndexes.first) / (temperatureIndexes.second - temperatureIndexes.first).toDouble()

        var runFinal = (runValue1 + ((runValue2 - runValue1) * tempDiff))
        var distFinal = (distValue1 + ((distValue2 - distValue1) * tempDiff))

        return Calculator.TakeoffLanding(runFinal.toInt(), distFinal.toInt())
    }

}