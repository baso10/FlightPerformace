package ch.baso.flightperformance.model

class Airplane {
    var name: String = ""
    var type: AirplaneType = AirplaneType.UNKNOWN
    var numOfSeats = 0
    var fuelType: String = ""
    var takeOffPerformance: List<String> = ArrayList()
    var landingPerformance: List<String> = ArrayList()
    var massBalanceList: List<AirplaneMassBalance> = ArrayList()
    var emptyWeight: Double = 0.0
    var emptyMoment: Double = 0.0
    var momentInInches: Boolean = false
    var envelope: AirplaneEnvelope = AirplaneEnvelope();

}