package ch.baso.flightperformance.view

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.baso.flightperformance.R
import ch.baso.flightperformance.model.Airplane
import ch.baso.flightperformance.model.AirplaneMassBalance
import ch.baso.flightperformance.model.InputData
import ch.baso.flightperformance.model.StorageData
import ch.baso.flightperformance.util.InputDataStorageController
import ch.baso.flightperformance.util.Util.toPx
import ch.baso.flightperformance.viewmodel.AirplaneViewModel
import ch.baso.flightperformance.viewmodel.MassBalanceParametersViewModel

class MassBalanceParametersView : Fragment() {

    private val airplaneViewModel: AirplaneViewModel by activityViewModels()
    private val massBalanceViewModel: MassBalanceParametersViewModel by activityViewModels()
    private var mbList: MutableList<EditText> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mass_balance_parameters_view_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        airplaneViewModel.airplane.observe(viewLifecycleOwner, {
            try {
                showAirplaneViews(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    private fun showAirplaneViews(airplane: Airplane) {
        mbList.clear()
        val mainLayout: LinearLayout = view as LinearLayout
        mainLayout.removeAllViews()
        for (mbItem in airplane.massBalanceList) {
            val layout = LinearLayout(activity)
            layout.orientation = LinearLayout.HORIZONTAL
            val tv = TextView(activity)
            tv.text = mbItem.name

            val ed1 = EditText(activity)
            ed1.inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED)
            ed1.tag = mbItem
            ed1.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {

                }

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    notifyChanges()
                }
            })


            mbList.add(ed1)

            layout.addView(tv, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    .also { it.setMargins(0, 0, 10.toPx(), 0) })
            layout.addView(ed1, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

            mainLayout.addView(layout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        }

        var storageData: InputData = InputDataStorageController.getInstance().getData()
        for (editText in mbList) {
            val airplaneMassBalance = editText.tag as AirplaneMassBalance
            val storedIntValue = storageData.massBalanceList.find { it.name.equals(airplaneMassBalance.name) }
            if (storedIntValue != null && storedIntValue.mass > 0) {
                editText.setText("" + storedIntValue.mass.toInt())
            }
        }

        notifyChanges()
    }

    private fun notifyChanges() {
        val massBalance = mutableListOf<AirplaneMassBalance>()
        for (editText in mbList) {
            val value = try {
                editText.text.toString().toInt()
            } catch (e: NumberFormatException) {
                0
            }
            val mb = (editText.tag as AirplaneMassBalance)
            mb.mass = value.toDouble()

            massBalance.add(mb)
        }
        massBalanceViewModel.massBalanceChanged(massBalance)
    }

    companion object {
        fun newInstance() = MassBalanceParametersView()
    }

}