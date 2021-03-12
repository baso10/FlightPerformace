package ch.baso.flightperformance.view

import androidx.fragment.app.Fragment
import ch.baso.flightperformance.bristell.BristellB23View
import ch.baso.flightperformance.model.AirplaneType

object ViewFactory {
    fun getFragment(type: AirplaneType): Fragment {
        return when (type) {
            AirplaneType.AAT3 -> DefaultResultView()
            AirplaneType.CESSNA_172 -> DefaultResultView()
            AirplaneType.BRISTELL_B23 -> BristellB23View()
            else -> DefaultResultView()
        }
    }
}