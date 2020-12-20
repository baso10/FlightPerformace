package ch.baso.flightperformance.calc

import ch.baso.flightperformance.model.AirplaneType

object CalculatorFactory
{
    fun getCalculator(type: AirplaneType): Calculator
    {
        return when(type)
        {
            AirplaneType.AAT3 -> AT3Calculator()
            AirplaneType.CESSNA_172 -> Cessna172Calculator()
            else -> BaseCalculator()
        }
    }
}