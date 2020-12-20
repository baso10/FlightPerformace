package ch.baso.flightperformance.calc

import ch.baso.flightperformance.model.Airplane
import ch.baso.flightperformance.model.AirplaneMassBalance

interface Calculator {
    fun calculateLanding(airplane: Airplane, pressure: Int, temperature: Int): TakeoffLanding
    fun calculateTakeoff(airplane: Airplane, pressure: Int, temperature: Int): TakeoffLanding
    fun calculateMassBalance(airplane: Airplane, massBalanceList: List<AirplaneMassBalance>): MassBalance

    data class TakeoffLanding(val run: Int, val distance: Int)
    data class MassBalance(val totalWeight: Double, val moment: Double)
}