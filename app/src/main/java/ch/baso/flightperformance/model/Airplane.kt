package ch.baso.flightperformance.model

import ch.baso.flightperformance.calc.Calculator

class Airplane {
    var name: String? = null
    var type: AirplaneType = AirplaneType.UNKNOWN
    var numOfSeats = 0
    var fuelType: String? = null
    var takeOffPerformance: List<String> = ArrayList()
    var landingPerformance: List<String> = ArrayList()
    var massBalanceList: List<AirplaneMassBalance> = ArrayList()
    var emptyWeight: Double = 0.0
    var emptyMoment: Double = 0.0
    var momentInInches: Boolean = false
    var envelope: AirplaneEnvelope = AirplaneEnvelope();
    var totalWeight: Double = 0.0

}