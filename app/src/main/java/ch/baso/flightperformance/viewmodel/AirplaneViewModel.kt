package ch.baso.flightperformance.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.baso.flightperformance.model.Airplane

class AirplaneViewModel : ViewModel() {
    val airplane = MutableLiveData<Airplane>()

    fun select(item: Airplane) {
        airplane.value = item
    }
}