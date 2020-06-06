package ch.baso.flightperformance.data;

import java.io.IOException;

public interface DataRetriever
{
  String getDataAT3Landing() throws IOException;

  String getDataAT3Takeoff() throws IOException;
}
