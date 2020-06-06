package ch.baso.flightperformance.util;

import java.util.ArrayList;
import java.util.List;

import ch.baso.flightperformance.model.DataPoint;

public class Util
{

  public static List<DataPoint> getDataPoints(String line)
  {
    List<DataPoint> result = new ArrayList<>();
    String[] split = line.split(";");
    for (String dataSet : split)
    {
      String[] dataPoints = dataSet.split(",");

      DataPoint dataPoint = new DataPoint(
              Integer.parseInt(dataPoints[0].trim()),
              Integer.parseInt(dataPoints[1].trim()),
              Integer.parseInt(dataPoints[2].trim())
      );
      result.add(dataPoint);
    }

    return result;
  }
}
