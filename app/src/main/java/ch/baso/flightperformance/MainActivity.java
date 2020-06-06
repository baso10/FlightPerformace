package ch.baso.flightperformance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ch.baso.flightperformance.calc.Calculator;
import ch.baso.flightperformance.data.DataRetrieverImpl;
import ch.baso.flightperformance.model.CalculatorResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

  private static final String TAG = "FlightPerformance";

  private static final String KEY_PRESSURE = "pressure";
  private static final String KEY_TEMPERATURE = "temperature";

  private Button btnCalculate;
  private EditText etPressureAltitude;
  private EditText etTemperature;
  private TextView tvTakeoffRun;
  private TextView tvTakeoffDistance;
  private TextView tvLandingRun;
  private TextView tvLandingDistance;

  private Calculator calculator;


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setupViews();

    calculator = new Calculator(new DataRetrieverImpl(this));

    readFromStorage();

    calculate();
  }

  private boolean readFromStorage()
  {
    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    int pressure = sharedPref.getInt(KEY_PRESSURE, 0);
    int temperature = sharedPref.getInt(KEY_TEMPERATURE, 0);

    if (pressure > 0 && temperature > 0)
    {
      etPressureAltitude.setText("" + pressure);
      etTemperature.setText("" + temperature);
      return true;
    }
    return false;
  }

  private void setupViews()
  {
    btnCalculate = findViewById(R.id.btnCalculate);
    btnCalculate.setOnClickListener(this);

    etPressureAltitude = findViewById(R.id.etPressureAltitude);
    etTemperature = findViewById(R.id.etTemperature);

    tvTakeoffRun = findViewById(R.id.tvTakeoffRun);
    tvTakeoffDistance = findViewById(R.id.tvTakeoffDistance);
    tvLandingRun = findViewById(R.id.tvLandingRun);
    tvLandingDistance = findViewById(R.id.tvTLandingDistance);
  }

  @Override
  public void onClick(View v)
  {
    switch (v.getId())
    {
      case R.id.btnCalculate:
        calculate();
        break;
    }
  }

  private void calculate()
  {
    try
    {
      String temperatureStr = etTemperature.getText().toString();
      int temperature = temperatureStr.trim().length() > 0 ? Integer.parseInt(temperatureStr) : 0;
      String pressureStr = etPressureAltitude.getText().toString();
      int pressureFeet = pressureStr.trim().length() > 0 ? Integer.parseInt(pressureStr) : -1;

      if(pressureFeet == -1)
      {
        return;
      }

      SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPref.edit();
      editor.putInt(KEY_PRESSURE, pressureFeet);
      editor.putInt(KEY_TEMPERATURE, temperature);
      editor.commit();

      CalculatorResult calculateLanding = calculator.calculateLanding(pressureFeet, temperature);

      tvLandingRun.setText("" + calculateLanding.getRun() + " m");
      tvLandingDistance.setText("" + calculateLanding.getDistance() + " m");

      CalculatorResult calculateTakeoff = calculator.calculateTakeoff(pressureFeet, temperature);

      tvTakeoffRun.setText("" + calculateTakeoff.getRun() + " m");
      tvTakeoffDistance.setText("" + calculateTakeoff.getDistance() + " m");

    }
    catch (Exception e)
    {
      Log.e(TAG, e.toString());
      Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
    }

  }

}