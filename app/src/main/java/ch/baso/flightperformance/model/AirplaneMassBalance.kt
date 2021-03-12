package ch.baso.flightperformance.model

class AirplaneMassBalance()
{
    var name: String = ""
    var arm: Double = 0.0
    var mass: Double = 0.0
    var moment: Double = 0.0
    var toKg: Double = 1.0

    constructor(name: String, arm: Double, mass: Double, moment: Double, toKg: Double) : this() {
        this.name = name
        this.arm = arm;
        this.mass = mass
        this.moment = moment
        this.toKg = toKg
    }
}
