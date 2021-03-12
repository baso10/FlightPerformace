package ch.baso.flightperformance.cessna

import ch.baso.flightperformance.calc.BaseCalculator
import ch.baso.flightperformance.calc.Calculator
import ch.baso.flightperformance.model.Airplane
import ch.baso.flightperformance.model.AirplaneMassBalance
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator

open class Cessna172Calculator : BaseCalculator() {

    override fun calculateTakeoff(airplane: Airplane, pressure: Int, temperature: Int, totalWeight: Double): Calculator.TakeoffLanding {
        val lines = airplane.takeOffPerformance
        val totalWeightPound = totalWeight * 2.20462

        val sectionIndex = if (totalWeightPound == 0.0) 1 else if (totalWeightPound <= 2200) 3 else if (totalWeightPound <= 2400) 2 else 1
        val pressure1 = if (pressure < 0) 0 else if (pressure > 8000) 8000 else pressure
        val temperature1 = if (temperature > 40) 40 else if (temperature < 0) 0 else temperature


        val pressureDataPoints = listOf(0, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000)
        val temperatureDataPoints = listOf(0, 0, 10, 10, 20, 20, 30, 30, 40, 40)
        val tempRunMap = mutableMapOf<Int, MutableList<Int>>(0 to mutableListOf(), 10 to mutableListOf(), 20 to mutableListOf(), 30 to mutableListOf(), 40 to mutableListOf())
        val tempDistanceMap = mutableMapOf<Int, MutableList<Int>>(0 to mutableListOf(), 10 to mutableListOf(), 20 to mutableListOf(), 30 to mutableListOf(), 40 to mutableListOf())

        for ((i, press1) in pressureDataPoints.withIndex()) {
            val lineIndex = (sectionIndex - 1) * 9 + i

            val lineArray = lines[lineIndex].split(",")
            for (i in 0..lineArray.size - 2 step 2) {
                val temp = temperatureDataPoints[i]

                val val1 = lineArray[i].toInt() //run
                val val2 = lineArray[i + 1].toInt() //distance
                tempRunMap[temp]?.add(val1)
                tempDistanceMap[temp]?.add(val2)
            }
        }

        //select temp column
        val temperatureIndexes = when {
            temperature1 <= 0 -> Pair(0, 0)
            temperature1 in 1..10 -> Pair(0, 10)
            temperature1 in 11..20 -> Pair(10, 20)
            temperature1 in 21..30 -> Pair(20, 30)
            temperature1 in 31..40 -> Pair(30, 40)
            else -> Pair(40, 40)
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


        var tempDiff = (temperature1 - temperatureIndexes.first) / (temperatureIndexes.second - temperatureIndexes.first).toDouble()
        tempDiff = if (Double.NaN.equals(tempDiff)) 0.0 else tempDiff

        var runFinal = (runValue1 + ((runValue2 - runValue1) * tempDiff)) * 0.3048 //to meters
        var distFinal = (distValue1 + ((distValue2 - distValue1) * tempDiff)) * 0.3048 //to meters

        return Calculator.TakeoffLanding(runFinal.toInt(), distFinal.toInt())
    }

    override fun calculateLanding(airplane: Airplane, pressure: Int, temperature: Int, totalWeight: Double): Calculator.TakeoffLanding {
        val lines = airplane.landingPerformance

        val sectionIndex = 1
        val pressure1 = if (pressure <= 0) 0 else if (pressure > 8000) 8000 else pressure
        val temperature1 = if (temperature > 40) 40 else if (temperature <= 0) 1 else temperature

        val pressureDataPoints = listOf(0, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000)
        val temperatureDataPoints = listOf(0, 0, 10, 10, 20, 20, 30, 30, 40, 40)
        val tempRunMap = mutableMapOf<Int, MutableList<Int>>(0 to mutableListOf(), 10 to mutableListOf(), 20 to mutableListOf(), 30 to mutableListOf(), 40 to mutableListOf())
        val tempDistanceMap = mutableMapOf<Int, MutableList<Int>>(0 to mutableListOf(), 10 to mutableListOf(), 20 to mutableListOf(), 30 to mutableListOf(), 40 to mutableListOf())

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
            temperature1 <= 0 -> Pair(0, 10)
            temperature1 in 1..10 -> Pair(0, 10)
            temperature1 in 11..20 -> Pair(10, 20)
            temperature1 in 21..30 -> Pair(20, 30)
            temperature1 in 31..40 -> Pair(30, 40)
            else -> Pair(40, 40)
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

        var runFinal = (runValue1 + ((runValue2 - runValue1) * tempDiff)) * 0.3048 //to meters
        var distFinal = (distValue1 + ((distValue2 - distValue1) * tempDiff)) * 0.3048 //to meters

        return Calculator.TakeoffLanding(runFinal.toInt(), distFinal.toInt())
    }
}