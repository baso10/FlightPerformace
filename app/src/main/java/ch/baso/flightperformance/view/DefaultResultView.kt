package ch.baso.flightperformance.view

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.baso.flightperformance.calc.Calculator
import ch.baso.flightperformance.databinding.FragmentDefaultResultViewBinding
import ch.baso.flightperformance.util.Util.toPx
import ch.baso.flightperformance.viewmodel.CalculatorViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [DefaultResultView.newInstance] factory method to
 * create an instance of this fragment.
 */
open class DefaultResultView : Fragment() {

    protected lateinit var binding: FragmentDefaultResultViewBinding
    protected val calculatorViewModel: CalculatorViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentDefaultResultViewBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calculatorViewModel.calculatorResult.observe(viewLifecycleOwner, {
            try {
                showResult(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    protected open fun showResult(result: Calculator.Result) {
        val airplane = result.airplane
        val calculateTakeoff = result.takeoff
        val calculateLanding = result.landing
        val calculateMassBalance = result.massBalance

        binding.layoutBefore.removeAllViews()
        binding.layoutAfter.removeAllViews()

        //name
        val layoutName = LinearLayout(activity)
        val tvName = TextView(activity)
        tvName.text = airplane.name
        tvName.textSize = 24F

        layoutName.addView(tvName, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        binding.layoutBefore.addView(layoutName, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        //TKOF info
        val layoutTakeoffInfo = LinearLayout(activity)
        val tvTakeoffInfo = TextView(activity)
        tvTakeoffInfo.text = "Please manually adjust values for grass/wet/slope/wind conditions"

        layoutTakeoffInfo.addView(tvTakeoffInfo, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .also { it.setMargins(0, 0, 10.toPx(), 0) })

        binding.layoutBefore.addView(layoutTakeoffInfo, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        //takeoff
        val layoutTakeOff = LinearLayout(activity)
        layoutTakeOff.orientation = LinearLayout.HORIZONTAL
        val tvTakeOff1 = TextView(activity)
        tvTakeOff1.text = "Takeoff: "
        val tvTakeOff = TextView(activity)
        tvTakeOff.text = "" + calculateTakeoff.run + " m / " + calculateTakeoff.distance + " m"

        layoutTakeOff.addView(tvTakeOff1, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .also { it.setMargins(0, 0, 10.toPx(), 0) })
        layoutTakeOff.addView(tvTakeOff, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        binding.layoutBefore.addView(layoutTakeOff, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        //landing
        val layoutLanding = LinearLayout(activity)
        layoutLanding.orientation = LinearLayout.HORIZONTAL
        val tvLanding1 = TextView(activity)
        tvLanding1.text = "Landing: "
        val tvLanding = TextView(activity)
        tvLanding.text = "" + calculateLanding.run + " m / " + calculateLanding.distance + " m"

        layoutLanding.addView(tvLanding1, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .also { it.setMargins(0, 0, 10.toPx(), 0) })
        layoutLanding.addView(tvLanding, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        binding.layoutBefore.addView(layoutLanding, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))


        //weightBalance
        val layoutWeightBalance = LinearLayout(activity)
        layoutWeightBalance.orientation = LinearLayout.HORIZONTAL
        val tvWeightBalance1 = TextView(activity)
        tvWeightBalance1.text = "Weight: "
        val tvWeightBalance = TextView(activity)
        tvWeightBalance.text = "" + calculateMassBalance.totalWeight.toInt() + " kg "

        layoutWeightBalance.addView(tvWeightBalance1, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .also { it.setMargins(0, 0, 10.toPx(), 0) })
        layoutWeightBalance.addView(tvWeightBalance, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        binding.layoutBefore.addView(layoutWeightBalance, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        //moment
        val layoutMoment = LinearLayout(activity)
        layoutWeightBalance.orientation = LinearLayout.HORIZONTAL
        val tvMoment = TextView(activity)
        tvMoment.text = "Moment: "
        val tvMomentValue = TextView(activity)
        tvMomentValue.text = "" + calculateMassBalance.moment.toInt() + " kg.m"

        layoutMoment.addView(tvMoment, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .also { it.setMargins(0, 0, 10.toPx(), 0) })
        layoutMoment.addView(tvMomentValue, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        binding.layoutBefore.addView(layoutMoment, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        //xcg
        val layoutXcg = LinearLayout(activity)
        layoutXcg.orientation = LinearLayout.HORIZONTAL
        val tvXcg = TextView(activity)
        tvXcg.text = "X(cg): "
        val tvXcgValue = TextView(activity)
        tvXcgValue.text = "" + String.format("%.2f", calculateMassBalance.xGc) + " m"
        tvXcgValue.text = tvXcgValue.text.toString() + " / X(% mac): " + String.format("%.2f", calculateMassBalance.gcPercent) + " %"

        layoutXcg.addView(tvXcg, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .also { it.setMargins(0, 0, 10.toPx(), 0) })
        layoutXcg.addView(tvXcgValue, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        binding.layoutBefore.addView(layoutXcg, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))


        envelopeView(result)

        afterView(result)
    }

    protected open fun envelopeView(result: Calculator.Result) {
        val airplane = result.airplane
        val calculateMassBalance = result.massBalance


        binding.envelopeMassBalance.setBounds(airplane.envelope)
        binding.envelopeMassBalance.setMassBalance(calculateMassBalance.totalWeight, calculateMassBalance.moment)

    }

    protected open fun afterView(result: Calculator.Result) {
        val airplane = result.airplane
        val massBalanceList = result.massBalance.massBalanceList

        var armUnit = "m"

        val tableLayout = TableLayout(activity)

        val tvCol1 = TextView(activity)
        tvCol1.text = "Item"
        val tvCol2 = TextView(activity)
        tvCol2.text = "Weight\n(kg)"
        tvCol2.gravity = Gravity.CENTER_HORIZONTAL
        val tvCol3 = TextView(activity)
        tvCol3.text = "Arm\n(" + armUnit + ")"
        tvCol3.gravity = Gravity.CENTER_HORIZONTAL
        val tvCol4 = TextView(activity)
        tvCol4.text = "Moment\n(kg.m)"
        tvCol4.gravity = Gravity.CENTER_HORIZONTAL

        val tableRow = TableRow(activity)
        tableRow.addView(tvCol1)
        tableRow.addView(tvCol2)
        tableRow.addView(tvCol3)
        tableRow.addView(tvCol4)

        tableLayout.addView(tableRow)

        for (mbItem in massBalanceList) {
            val tableRow = TableRow(activity)

            var name = mbItem.name
            if(name.length > 15)
            {
                var newName = ""
                var split = name.split(" ")
                var splitCount = 0;
                for(word in split) {
                    newName += word + " "
                    if(name.length > (15 + (splitCount * 15)))
                    {
                        newName += "\n"
                        splitCount++
                    }
                }
                name = newName
            }

            var armValue = mbItem.arm
            if(airplane.momentInInches) {
                armValue *= 0.0254
            }

            val tvCol1 = TextView(activity)
            tvCol1.text = name
            tvCol1.setPadding(0, 0, 0, 5.toPx())
            val tvCol2 = TextView(activity)
            tvCol2.text = "" + mbItem.mass.toInt()
            tvCol2.gravity = Gravity.RIGHT
            tvCol2.setPadding(0, 0, 10.toPx(), 0)
            val tvCol3 = TextView(activity)
            tvCol3.text = "" + String.format("%.3f", armValue)
            tvCol3.gravity = Gravity.RIGHT
            tvCol3.setPadding(0, 0, 10.toPx(), 0)
            val tvCol4 = TextView(activity)
            tvCol4.text = "" + String.format("%.3f", mbItem.moment)
            tvCol4.gravity = Gravity.RIGHT
            tvCol4.setPadding(0, 0, 10.toPx(), 0)

            tableRow.addView(tvCol1)
            tableRow.addView(tvCol2)
            tableRow.addView(tvCol3)
            tableRow.addView(tvCol4)

            tableLayout.addView(tableRow)

        }

        binding.layoutAfter.addView(tableLayout, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

    }

}