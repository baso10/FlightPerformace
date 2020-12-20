package ch.baso.flightperformance.calc

import ch.baso.flightperformance.model.Airplane
import ch.baso.flightperformance.model.AirplaneMassBalance

open class BaseCalculator : Calculator {
    override fun calculateMassBalance(airplane: Airplane, massBalanceList: List<AirplaneMassBalance>): Calculator.MassBalance {
        var totalWeight = airplane.emptyWeight
        var totalMoment = airplane.emptyMoment
        for (mass in massBalanceList) {
            totalWeight += mass.mass * mass.toKg
            totalMoment += mass.mass * mass.toKg * mass.arm
        }
        if(airplane.momentInInches)
        {
            totalMoment *= 0.0254 //convert to (kg meters) 1 inch = 0.0254m. Mass in in kg, arm is in inch
        }

        return Calculator.MassBalance(totalWeight, totalMoment)
    }
    override fun calculateLanding(airplane: Airplane, pressure: Int, temperature: Int): Calculator.TakeoffLanding {
        return Calculator.TakeoffLanding(0, 0)
    }

    override fun calculateTakeoff(airplane: Airplane, pressure: Int, temperature: Int): Calculator.TakeoffLanding {
        return Calculator.TakeoffLanding(0, 0)
    }
}