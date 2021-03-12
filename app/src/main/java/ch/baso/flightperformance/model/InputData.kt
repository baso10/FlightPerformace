package ch.baso.flightperformance.model

class InputData() {
    var airplane: Airplane = Airplane()
    var massBalanceList: List<AirplaneMassBalance> = ArrayList()
    var temperature: Int = 0
    var pressure: Int = 0

    constructor(airplane: Airplane, massBalanceList: List<AirplaneMassBalance>, temperature: Int, pressure: Int) : this() {
        this.airplane = airplane
        this.massBalanceList = massBalanceList
        this.temperature = temperature
        this.pressure = pressure
    }
}