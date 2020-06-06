package ch.baso.flightperformance.data;

import android.app.Activity;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import ch.baso.flightperformance.R;

public class DataRetrieverImpl implements DataRetriever
{
  private Activity activity;

  public DataRetrieverImpl(Activity activity)
  {
    this.activity = activity;
  }

  @Override
  public String getDataAT3Landing() throws IOException
  {
    try (InputStream is = activity.getResources().openRawResource(R.raw.aat3_landing))
    {
      return IOUtils.toString(is, StandardCharsets.UTF_8);
    }
  }

  @Override
  public String getDataAT3Takeoff() throws IOException
  {
    try (InputStream is = activity.getResources().openRawResource(R.raw.aat3_takeoff))
    {
      return IOUtils.toString(is, StandardCharsets.UTF_8);
    }
  }
}
