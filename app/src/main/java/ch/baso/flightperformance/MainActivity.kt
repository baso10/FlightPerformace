package ch.baso.flightperformance

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ch.baso.flightperformance.calc.CalculatorFactory
import ch.baso.flightperformance.databinding.ActivityMainBinding
import ch.baso.flightperformance.model.Airplane
import ch.baso.flightperformance.model.AirplaneItem
import ch.baso.flightperformance.model.InputData
import ch.baso.flightperformance.util.InputDataStorageController
import ch.baso.flightperformance.util.TextWatcherAdapter
import ch.baso.flightperformance.view.MassBalanceParametersView
import ch.baso.flightperformance.view.ViewFactory
import ch.baso.flightperformance.viewmodel.AirplaneViewModel
import ch.baso.flightperformance.viewmodel.CalculatorViewModel
import ch.baso.flightperformance.viewmodel.MassBalanceParametersViewModel
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private val gson = Gson()

    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
    private val airplaneViewModel: AirplaneViewModel by viewModels()
    private val massBalanceViewModel: MassBalanceParametersViewModel by viewModels()
    private val calculatorViewModel: CalculatorViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        InputDataStorageController.init(this)

        setupViews()
        readFromStorage()
        setupListeners()

    }

    override fun onResume() {
        super.onResume()
        calculate()
    }

    private fun setupListeners() {
        massBalanceViewModel.massBalance.observe(this, Observer { massBalanceList ->
            calculate()
        })
        binding.btnCalculate.setOnClickListener(this)

        val changeListener = object : TextWatcherAdapter() {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculate()
            }
        }
        binding.etPressureAltitude.addTextChangedListener(changeListener)
        binding.etTemperature.addTextChangedListener(changeListener)

    }

    private fun setupViews() {
        val airplanes = mutableListOf<AirplaneItem>()
        addAirplane(airplanes, "HB-SRE", R.raw.hb_sre)
        addAirplane(airplanes, "HB-TEE", R.raw.hb_tee)
        addAirplane(airplanes, "HB-KGN", R.raw.hb_kgn)


        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, airplanes)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spAirplane.adapter = aa
        binding.spAirplane.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //ignore
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position) as AirplaneItem

                val fragment = ViewFactory.getFragment(selectedItem.airplane.type)

                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.airplaneFragmentView, fragment)
                        .commit()

                airplaneViewModel.select(selectedItem.airplane)

                calculate()
            }

        }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.massBalanceViews, MassBalanceParametersView())
                .commit()

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

            val airplaneItem = binding.spAirplane.selectedItem as AirplaneItem
            if (airplaneItem == null) {
                //Toast.makeText(this, "Invalid item", Toast.LENGTH_SHORT).show()
                return
            }
            val airplane = airplaneItem.airplane

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

            val massBalanceInputList = massBalanceViewModel.massBalance.value ?: mutableListOf()

            val inputData = InputData(airplane, massBalanceInputList, temperature, pressureFeet)

            //calculate
            val calculator = CalculatorFactory.getCalculator(airplane.type)
            val result = calculator.calculate(inputData)

            //publish result
            calculatorViewModel.calculateDone(result)

            //store variables
            InputDataStorageController.getInstance().storeData(inputData)

        } catch (e: Exception) {
            Log.e(TAG, e.toString(), e)
            Toast.makeText(this, "" + e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun readFromStorage(): Boolean {
        val inputData = InputDataStorageController.getInstance().getData()

        binding.etPressureAltitude.setText("" + inputData.pressure)
        binding.etTemperature.setText("" + inputData.temperature)

        @Suppress("UNCHECKED_CAST")
        val adapter: ArrayAdapter<AirplaneItem> = binding.spAirplane.adapter as ArrayAdapter<AirplaneItem>
        for (position in 0 until adapter.count) {
            val item = adapter.getItem(position)
            if (item != null && item.name == inputData.airplane.name) {
                binding.spAirplane.setSelection(position)
                break
            }
        }

        return true
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

    }

}