package ch.baso.flightperformance.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.baso.flightperformance.model.AirplaneMassBalance

class MassBalanceParametersViewModel : ViewModel() {
    val massBalance = MutableLiveData<List<AirplaneMassBalance>>()

    fun massBalanceChanged(input: List<AirplaneMassBalance>) {
        massBalance.value = input
    }
}