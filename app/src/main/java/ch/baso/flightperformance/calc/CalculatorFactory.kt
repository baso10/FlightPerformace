package ch.baso.flightperformance.calc

import ch.baso.flightperformance.aat3.AT3Calculator
import ch.baso.flightperformance.bristell.BristellB23Calculator
import ch.baso.flightperformance.cessna.Cessna172Calculator
import ch.baso.flightperformance.model.AirplaneType

object CalculatorFactory
{
    fun getCalculator(type: AirplaneType): Calculator
    {
        return when(type)
        {
            AirplaneType.AAT3 -> AT3Calculator()
            AirplaneType.CESSNA_172 -> Cessna172Calculator()
            AirplaneType.BRISTELL_B23 -> BristellB23Calculator()
            else -> BaseCalculator()
        }
    }
}