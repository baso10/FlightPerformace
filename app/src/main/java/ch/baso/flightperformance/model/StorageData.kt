package ch.baso.flightperformance.model

class StorageData {
    var temperature: Int = 0
    var pressure: Int = 0
    var airplane = ""
    var massBalance = mutableMapOf<String, Int>()
}