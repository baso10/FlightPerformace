package ch.baso.flightperformance.bristell

import androidx.fragment.app.Fragment
import ch.baso.flightperformance.calc.Calculator
import ch.baso.flightperformance.view.DefaultResultView

/**
 * A simple [Fragment] subclass.
 * Use the [BristellB23View.newInstance] factory method to
 * create an instance of this fragment.
 */
class BristellB23View : DefaultResultView() {


    override fun envelopeView(result: Calculator.Result) {
        val airplane = result.airplane
        val calculateMassBalance = result.massBalance


        binding.envelopeMassBalance.setBounds(airplane.envelope)
        binding.envelopeMassBalance.setMassBalance(calculateMassBalance.totalWeight, calculateMassBalance.xGc * 1000)

    }
}