package ch.baso.flightperformance.calc

import ch.baso.flightperformance.model.Airplane
import ch.baso.flightperformance.model.AirplaneMassBalance
import ch.baso.flightperformance.model.InputData
import java.math.RoundingMode

open class BaseCalculator : Calculator {

    override fun calculate(inputData: InputData): Calculator.Result {
        val calculateMassBalance = calculateMassBalance(inputData.airplane, inputData.massBalanceList)
        val calculateTakeoff = calculateTakeoff(inputData.airplane, inputData.pressure, inputData.temperature, calculateMassBalance.totalWeight)
        val calculateLanding = calculateLanding(inputData.airplane, inputData.pressure, inputData.temperature, calculateMassBalance.totalWeight)

        return Calculator.Result(inputData.airplane, calculateMassBalance, calculateTakeoff, calculateLanding)
    }
    protected open fun calculateMassBalance(airplane: Airplane, massBalanceList: List<AirplaneMassBalance>): Calculator.MassBalance {
        var totalWeight = airplane.emptyWeight
        var totalMoment = airplane.emptyMoment
        var emptyArm = (airplane.emptyMoment / airplane.emptyWeight).toBigDecimal().setScale(3, RoundingMode.HALF_UP).toDouble()

        val resultList = mutableListOf<AirplaneMassBalance>()
        resultList += AirplaneMassBalance("Empty", emptyArm, airplane.emptyWeight, airplane.emptyMoment, 1.0)

        for (mass in massBalanceList) {
            val itemTotalWeight = mass.mass * mass.toKg
            var itemTotalMoment = mass.mass * mass.toKg * mass.arm
            if(airplane.momentInInches)
            {
                itemTotalMoment *= 0.0254 //convert to (kg meters) 1 inch = 0.0254m. Mass in in kg, arm is in inch
            }
            resultList += AirplaneMassBalance(mass.name, mass.arm, itemTotalWeight, itemTotalMoment, mass.toKg)

            totalMoment += itemTotalMoment
            totalWeight += itemTotalWeight

        }

        val mac = if (airplane.envelope.mac != 0.0 ) airplane.envelope.mac else 1.0
        val xGc = totalMoment / totalWeight
        val gcPercent = (xGc - airplane.envelope.macLe) / mac * 100


        return Calculator.MassBalance(resultList, totalWeight, totalMoment, xGc, gcPercent)
    }

    protected open fun calculateLanding(airplane: Airplane, pressure: Int, temperature: Int, totalWeight: Double): Calculator.TakeoffLanding {
        return Calculator.TakeoffLanding(0, 0)
    }

    protected open fun calculateTakeoff(airplane: Airplane, pressure: Int, temperature: Int, totalWeight: Double): Calculator.TakeoffLanding {
        return Calculator.TakeoffLanding(0, 0)
    }
}