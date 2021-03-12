package ch.baso.flightperformance.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.baso.flightperformance.calc.Calculator

class CalculatorViewModel : ViewModel() {
    val calculator = MutableLiveData<Unit>()
    val calculatorResult = MutableLiveData<Calculator.Result>()

    fun calculate() {
        calculator.value = Unit
    }

    fun calculateDone(result: Calculator.Result) {
        calculatorResult.value = result
    }
}