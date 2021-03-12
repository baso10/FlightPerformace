package ch.baso.flightperformance.calc

import ch.baso.flightperformance.model.Airplane
import ch.baso.flightperformance.model.AirplaneMassBalance
import ch.baso.flightperformance.model.InputData

interface Calculator {
    fun calculate(inputData: InputData) : Result;

    data class Result(val airplane: Airplane, val massBalance: MassBalance, val takeoff: TakeoffLanding, val landing: TakeoffLanding)

    data class TakeoffLanding(val run: Int, val distance: Int)
    data class MassBalance(val massBalanceList: List<AirplaneMassBalance>, val totalWeight: Double, val moment: Double, var xGc: Double = 0.0, var gcPercent: Double = 0.0)

}