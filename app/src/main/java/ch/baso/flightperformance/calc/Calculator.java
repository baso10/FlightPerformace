package ch.baso.flightperformance.calc;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import ch.baso.flightperformance.data.DataRetriever;
import ch.baso.flightperformance.model.DataPoint;
import ch.baso.flightperformance.util.Util;
import ch.baso.flightperformance.model.CalculatorResult;

public class Calculator
{
  private DataRetriever dataRetriever;

  public Calculator(DataRetriever dataRetriever)
  {
    this.dataRetriever = dataRetriever;
  }

  public CalculatorResult calculateLanding(int pressure, int temperature) throws IOException
  {
    CalculatorResult result;
    String data = dataRetriever.getDataAT3Landing();
    String[] lines = data.split("\n");

    List<DataPoint> dataPoints0 = Util.getDataPoints(lines[0]);
    List<DataPoint> dataPoints1460 = Util.getDataPoints(lines[1]);
    List<DataPoint> dataPoints3281 = Util.getDataPoints(lines[2]);
    List<DataPoint> dataPoints4921 = Util.getDataPoints(lines[3]);
    List<DataPoint> dataPoints6562 = Util.getDataPoints(lines[4]);

    if (pressure < 1460)
    {
      double diffPercent = (pressure - 0) / (float) (1460 - 0);
      result = calcWithPoints(dataPoints0, dataPoints1460, diffPercent, temperature);
    }
    else if (pressure < 3281)
    {
      double diffPercent = (pressure - 1460) / (float) (3281 - 1460);
      result = calcWithPoints(dataPoints1460, dataPoints3281, diffPercent, temperature);
    }
    else if (pressure < 4921)
    {
      double diffPercent = (pressure - 3281) / (float) (4921 - 3281);
      result = calcWithPoints(dataPoints3281, dataPoints4921, diffPercent, temperature);
    }
    else if (pressure < 6562)
    {
      double diffPercent = (pressure - 4921) / (float) (6562 - 4921);
      result = calcWithPoints(dataPoints4921, dataPoints6562, diffPercent, temperature);
    }
    else
    {
      double diffPercent = 0;
      result = calcWithPoints(dataPoints6562, dataPoints6562, diffPercent, temperature);
    }

    return result;
  }

  public CalculatorResult calculateTakeoff(int pressure, int temperature) throws IOException
  {
    CalculatorResult result;
    String data = dataRetriever.getDataAT3Takeoff();
    String[] lines = data.split("\n");

    List<DataPoint> dataPoints0 = Util.getDataPoints(lines[0]);
    List<DataPoint> dataPoints1460 = Util.getDataPoints(lines[1]);
    List<DataPoint> dataPoints3281 = Util.getDataPoints(lines[2]);
    List<DataPoint> dataPoints4921 = Util.getDataPoints(lines[3]);
    List<DataPoint> dataPoints6562 = Util.getDataPoints(lines[4]);

    if (pressure < 1460)
    {
      double diffPercent = (pressure - 0) / (float) (1460 - 0);
      result = calcWithPoints(dataPoints0, dataPoints1460, diffPercent, temperature);
    }
    else if (pressure < 3281)
    {
      double diffPercent = (pressure - 1460) / (float) (3281 - 1460);
      result = calcWithPoints(dataPoints1460, dataPoints3281, diffPercent, temperature);
    }
    else if (pressure < 4921)
    {
      double diffPercent = (pressure - 3281) / (float) (4921 - 3281);
      result = calcWithPoints(dataPoints3281, dataPoints4921, diffPercent, temperature);
    }
    else if (pressure < 6562)
    {
      double diffPercent = (pressure - 4921) / (float) (6562 - 4921);
      result = calcWithPoints(dataPoints4921, dataPoints6562, diffPercent, temperature);
    }
    else
    {
      double diffPercent = 0;
      result = calcWithPoints(dataPoints6562, dataPoints6562, diffPercent, temperature);
    }

    return result;
  }

  private CalculatorResult calcWithPoints(List<DataPoint> dataPoints1, List<DataPoint> dataPoints2, double diffPercent, int temperature)
  {
    CalculatorResult result = new CalculatorResult();
    //take value from pressure 0 and add diff to 1460
    DataPoint startingDataPoint = null;
    DataPoint startingDataPointTempNext = null;
    ListIterator<DataPoint> dataPointListIterator = dataPoints1.listIterator();
    while (dataPointListIterator.hasNext())
    {
      DataPoint dp = dataPointListIterator.next();
      if (temperature >= dp.getTemperature())
      {
        startingDataPoint = dp;
        startingDataPointTempNext = dataPointListIterator.hasNext() ? dataPointListIterator.next() : null;
        if (dataPointListIterator.hasPrevious())
        {
          dataPointListIterator.previous();
        }
      }
    }

    DataPoint startingNextDataPoint = null;
    DataPoint startingNextDataPointTempNext = null;
    ListIterator<DataPoint> dataPointListIterator1 = dataPoints2.listIterator();
    while (dataPointListIterator1.hasNext())
    {
      DataPoint dp = dataPointListIterator1.next();
      if (temperature >= dp.getTemperature())
      {
        startingNextDataPoint = dp;
        startingNextDataPointTempNext = dataPointListIterator1.hasNext() ? dataPointListIterator1.next() : null;
        if (dataPointListIterator1.hasPrevious())
        {
          dataPointListIterator1.previous();
        }
      }
    }

    //temperature 1 diff add
    int startingDataPointLandingRun = startingDataPoint.getRun();
    int startingDataPointLandingDistance = startingDataPoint.getDistance();
    if (startingDataPointTempNext != null)
    {
      double diffTemperature = temperature - startingDataPoint.getTemperature();
      diffTemperature = diffTemperature / (double) (startingDataPointTempNext.getTemperature() - startingDataPoint.getTemperature());
      int landingRunDiff = (int) ((startingDataPointTempNext.getRun() - startingDataPoint.getRun()) * diffTemperature);
      int landingDistanceDiff = (int) ((startingDataPointTempNext.getDistance() - startingDataPoint.getDistance()) * diffTemperature);
      startingDataPointLandingRun += landingRunDiff;
      startingDataPointLandingDistance += landingDistanceDiff;
    }

    //temperature 2 diff add
    int startingNextDataPointLandingRun = startingNextDataPoint.getRun();
    int startingNextDataPointLandingDistance = startingNextDataPoint.getDistance();
    if (startingNextDataPointTempNext != null)
    {
      double diffTemperature = temperature - startingNextDataPoint.getTemperature();
      diffTemperature = diffTemperature / (double) (startingNextDataPointTempNext.getTemperature() - startingNextDataPoint.getTemperature());
      int landingRunDiff = (int) ((startingNextDataPointTempNext.getRun() - startingNextDataPoint.getRun()) * diffTemperature);
      int landingDistanceDiff = (int) ((startingNextDataPointTempNext.getDistance() - startingNextDataPoint.getDistance()) * diffTemperature);
      startingNextDataPointLandingRun += landingRunDiff;
      startingNextDataPointLandingDistance += landingDistanceDiff;
    }

    //diff add
    int landingRunDiff = (int) ((startingNextDataPointLandingRun - startingDataPointLandingRun) * diffPercent);
    int landingDistanceDiff = (int) ((startingNextDataPointLandingDistance - startingDataPointLandingDistance) * diffPercent);
    int run = startingDataPointLandingRun + landingRunDiff;
    int distance = startingDataPointLandingDistance + landingDistanceDiff;

    result.setRun(run);
    result.setDistance(distance);

    return result;
  }

}
