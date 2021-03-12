package ch.baso.flightperformance.util

import android.app.Activity
import ch.baso.flightperformance.model.Airplane
import ch.baso.flightperformance.model.AirplaneMassBalance
import ch.baso.flightperformance.model.InputData
import ch.baso.flightperformance.model.StorageData

class InputDataStorageController private constructor(context: Activity) {

    private val KEY_INPUT_DATA = "inputData"
    private val localStorage: LocalStorage = LocalStorage.init(context, KEY_INPUT_DATA)

    fun storeData(inputData: InputData) {
        val storageData = localStorage.getData(StorageData::class.java) ?: StorageData()

        val massBalance = storageData.massBalance
        inputData.massBalanceList.forEach { massBalance[it.name] = it.mass.toInt() }

        storageData.pressure = inputData.pressure
        storageData.temperature = inputData.temperature
        storageData.airplane = inputData.airplane.name
        storageData.massBalance = massBalance

        localStorage.storeData(storageData)
    }

    fun getData(): InputData {
        val storageData = localStorage.getData(StorageData::class.java) ?: return InputData()

        val result = InputData()
        result.temperature = storageData.temperature
        result.pressure = storageData.pressure
        result.airplane = Airplane().apply { name = storageData.airplane }
        result.massBalanceList = storageData.massBalance.map { AirplaneMassBalance().apply { name = it.key; mass = it.value.toDouble() } }

        return result
    }

    companion object : SingletonHolder<InputDataStorageController, Activity>(::InputDataStorageController)

}