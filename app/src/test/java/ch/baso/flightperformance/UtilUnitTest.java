package ch.baso.flightperformance;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import ch.baso.flightperformance.calc.Calculator;
import ch.baso.flightperformance.data.DataRetriever;
import ch.baso.flightperformance.model.CalculatorResult;
import ch.baso.flightperformance.model.DataPoint;
import ch.baso.flightperformance.util.Util;

import static org.junit.Assert.*;

public class UtilUnitTest
{
    @Test
    public void getDataPoints() {
        List<DataPoint> dataPoints = Util.getDataPoints("-15, 179,403; -5, 186,419; 5, 193,434; 15, 200,450; 25, 207,466; 35, 214,481");
        assertEquals(6, dataPoints.size());

        DataPoint dataPoint = dataPoints.get(0);
        assertEquals(-15, dataPoint.getTemperature());
        assertEquals(179, dataPoint.getRun());
        assertEquals(403, dataPoint.getDistance());

        dataPoint = dataPoints.get(1);
        assertEquals(-5, dataPoint.getTemperature());
        assertEquals(186, dataPoint.getRun());
        assertEquals(419, dataPoint.getDistance());

    }

    @Test
    public void calculator() throws Exception
    {
        Calculator calculator = new Calculator(new DataRetriever()
        {
            @Override
            public String getDataAT3Landing() throws IOException
            {
                try (InputStream is = UtilUnitTest.this.getClass().getResourceAsStream("aat3_landing.txt"))
                {
                    return IOUtils.toString(is, StandardCharsets.UTF_8);
                }
            }

            @Override
            public String getDataAT3Takeoff() throws IOException
            {
                try (InputStream is = UtilUnitTest.this.getClass().getResourceAsStream("aat3_takeoff.txt"))
                {
                    return IOUtils.toString(is, StandardCharsets.UTF_8);
                }
            }
        });

        CalculatorResult result = calculator.calculateLanding(0, 15);

        assertEquals(200, result.getRun());
        assertEquals(450, result.getDistance());

        result = calculator.calculateLanding(1460, 12);

        assertEquals(210, result.getRun());
        assertEquals(472, result.getDistance());

        result = calculator.calculateLanding(1675, 25);

        assertEquals(220, result.getRun());
        assertEquals(496, result.getDistance());

        result = calculator.calculateLanding(9999, 12);

        assertEquals(252, result.getRun());
        assertEquals(567, result.getDistance());

        /////

        result = calculator.calculateTakeoff(1675, 25);

        assertEquals(259, result.getRun());
        assertEquals(555, result.getDistance());

        result = calculator.calculateTakeoff(9999, 12);

        assertEquals(344, result.getRun());
        assertEquals(738, result.getDistance());
    }
}