package ch.baso.flightperformance.model

import ch.baso.flightperformance.model.Airplane

class AirplaneItem(val name: String, val airplane: Airplane)
{
    override fun toString() = name
}