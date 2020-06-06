package ch.baso.flightperformance.model;

public class CalculatorResult
{
  private int run;
  private int distance;

  public int getRun()
  {
    return run;
  }

  public void setRun(int run)
  {
    this.run = run;
  }

  public int getDistance()
  {
    return distance;
  }

  public void setDistance(int distance)
  {
    this.distance = distance;
  }

  @Override
  public String toString()
  {
    return "CalculatorResult{" +
            "run=" + run +
            ", distance=" + distance +
            '}';
  }
}
