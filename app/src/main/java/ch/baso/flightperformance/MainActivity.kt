package ch.baso.flightperformance

import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ch.baso.flightperformance.calc.CalculatorFactory
import ch.baso.flightperformance.databinding.ActivityMainBinding
import ch.baso.flightperformance.model.Airplane
import ch.baso.flightperformance.model.AirplaneMassBalance
import ch.baso.flightperformance.model.InputData
import ch.baso.flightperformance.model.AirplaneItem
import ch.baso.flightperformance.util.TextWatcherAdapter
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private val gson = Gson()
    private var mbList: MutableList<EditText> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupViews()
        readFromStorage()

    }

    override fun onResume() {
        super.onResume()
        calculate()
    }

    private fun setupViews() {
        binding.btnCalculate.setOnClickListener(this)

        val changeListener = object : TextWatcherAdapter() {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculate()
            }
        }
        binding.etPressureAltitude.addTextChangedListener(changeListener)
        binding.etTemperature.addTextChangedListener(changeListener)

        val airplanes = mutableListOf<AirplaneItem>()
        addAirplane(airplanes, "HB-SRA", R.raw.hb_sra)
        addAirplane(airplanes, "HB-SRD", R.raw.hb_srd)
        addAirplane(airplanes, "HB-SRE", R.raw.hb_sre)
        addAirplane(airplanes, "HB-TEE", R.raw.hb_tee)


        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, airplanes)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spAirplane.adapter = aa
        binding.spAirplane.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //ignore
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position) as AirplaneItem
                showAirplaneViews(selectedItem.airplane)

            }

        }

    }

    private fun showAirplaneViews(airplane: Airplane) {
        mbList.clear()
        binding.massBalanceViews.removeAllViews()
        for (mbItem in airplane.massBalanceList) {
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.HORIZONTAL
            val tv = TextView(this)
            tv.text = mbItem.name

            val ed1 = EditText(this)
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
                    calculate()
                }
            })


            mbList.add(ed1)

            layout.addView(tv, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    .also { it.setMargins(0, 0, 10.toPx(), 0) })
            layout.addView(ed1, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

            binding.massBalanceViews.addView(layout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

            binding.envelopeMassBalance.setBounds(airplane.envelope)

        }

        var inputData: InputData = getInputData()
        for (editText in mbList) {
            val airplaneMassBalance = editText.tag as AirplaneMassBalance
            val storedIntValue = inputData.massBalance.get(airplaneMassBalance.name)
            if (storedIntValue != null && storedIntValue > 0) {
                editText.setText("" + storedIntValue)
            }
        }

        calculate()
    }

    private fun getInputData(): InputData {
        val sharedPref = getPreferences(MODE_PRIVATE)
        val inputDataStr = sharedPref.getString(KEY_INPUT_DATA, null)
        if (inputDataStr != null) {
            return gson.fromJson(inputDataStr, InputData::class.java)
        }

        return InputData()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnCalculate -> {
                calculate()
                try {
                    val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                } catch (e: Exception) {
                    //ignore
                }
            }
        }
    }

    private fun calculate() {
        try {

            val selectedItem = binding.spAirplane.selectedItem
            if (selectedItem == null || selectedItem !is AirplaneItem) {
                Toast.makeText(this, "Invalid item", Toast.LENGTH_LONG).show()
                return
            }

            val airplane = selectedItem.airplane
            val calculator = CalculatorFactory.getCalculator(airplane.type)


            val temperature = try {
                binding.etTemperature.text.toString().toInt()
            } catch (e: NumberFormatException) {
                0
            }
            val pressureFeet = try {
                binding.etPressureAltitude.text.toString().toInt()
            } catch (e: NumberFormatException) {
                0
            }

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

            val calculateMassBalance = calculator.calculateMassBalance(airplane, massBalance)
            airplane.totalWeight = calculateMassBalance.totalWeight

            val calculateLanding = calculator.calculateLanding(airplane, pressureFeet, temperature)
            val calculateTakeoff = calculator.calculateTakeoff(airplane, pressureFeet, temperature)

            binding.tvAirplaneName.text = airplane.name
            binding.tvLandingRun.text = "" + calculateLanding.run + " m / " + calculateLanding.distance + " m"
            binding.tvTakeoffRun.text = "" + calculateTakeoff.run + " m / " + calculateTakeoff.distance + " m"
            binding.tvWeightBalance.text = "" + calculateMassBalance.totalWeight.toInt() + " kg / Moment: " + calculateMassBalance.moment.toInt() + " kgm"

            binding.envelopeMassBalance.setMassBalance(calculateMassBalance);

            saveToStorage()

        } catch (e: Exception) {
            Log.e(TAG, e.toString(), e)
            Toast.makeText(this, "" + e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun readFromStorage(): Boolean {
        var inputData: InputData = getInputData()

        binding.etPressureAltitude.setText("" + inputData.pressure)
        binding.etTemperature.setText("" + inputData.temperature)

        @Suppress("UNCHECKED_CAST")
        val adapter: ArrayAdapter<AirplaneItem> = binding.spAirplane.adapter as ArrayAdapter<AirplaneItem>
        for (position in 0 until adapter.count) {
            val item = adapter.getItem(position)
            if (item != null && item.name == inputData.airplane) {
                binding.spAirplane.setSelection(position)
                break
            }
        }

        return true
    }

    private fun saveToStorage() {
        val inputData = getInputData()

        val temperature = try {
            binding.etTemperature.text.toString().toInt()
        } catch (e: NumberFormatException) {
            inputData.temperature
        }
        val pressureFeet = try {
            binding.etPressureAltitude.text.toString().toInt()
        } catch (e: NumberFormatException) {
            inputData.pressure
        }

        val airplane = try {
            (binding.spAirplane.selectedItem as AirplaneItem).name
        } catch (e: Exception) {
            inputData.airplane
        }

        val massBalance = inputData.massBalance
        for (editText in mbList) {
            val value = try {
                editText.text.toString().toInt()
            } catch (e: NumberFormatException) {
                0
            }
            massBalance[(editText.tag as AirplaneMassBalance).name] = value
        }

        val sharedPref = getPreferences(MODE_PRIVATE)
        val editor = sharedPref.edit()

        inputData.pressure = pressureFeet
        inputData.temperature = temperature
        inputData.airplane = airplane
        inputData.massBalance = massBalance
        editor.putString(KEY_INPUT_DATA, gson.toJson(inputData))
        editor.commit()
    }

    private fun addAirplane(airplanes: MutableList<AirplaneItem>, callSign: String, resId: Int) {
        try {
            airplanes.add(AirplaneItem(callSign, gson.fromJson(resources.openRawResource(resId).bufferedReader(), Airplane::class.java)))
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
    }

    companion object {
        private const val TAG = "FlightPerformance"
        private const val KEY_INPUT_DATA = "inputData"
    }

    fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

}