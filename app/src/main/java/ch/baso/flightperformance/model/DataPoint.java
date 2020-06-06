package ch.baso.flightperformance.model;

public class DataPoint
{
  private final int temperature;
  private final int run;
  private final int distance;

  public DataPoint(int temperature, int run, int distance)
  {
    this.temperature = temperature;
    this.run = run;
    this.distance = distance;
  }

  public int getTemperature()
  {
    return temperature;
  }

  public int getRun()
  {
    return run;
  }

  public int getDistance()
  {
    return distance;
  }

  @Override
  public String toString()
  {
    return "DataPoint{" +
            "temperature=" + temperature +
            ", run=" + run +
            ", distance=" + distance +
            '}';
  }
}
