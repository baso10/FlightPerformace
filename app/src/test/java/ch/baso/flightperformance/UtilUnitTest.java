package ch.baso.flightperformance;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import ch.baso.flightperformance.calc.AT3Calculator;
import ch.baso.flightperformance.calc.Calculator;
import ch.baso.flightperformance.calc.Cessna172Calculator;
import ch.baso.flightperformance.model.Airplane;
import ch.baso.flightperformance.model.DataPoint;
import ch.baso.flightperformance.util.Util;

import static org.junit.Assert.assertEquals;

public class UtilUnitTest
{
  @Test
  public void getDataPoints()
  {
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

  private Airplane loadAAt3()
  {
    Airplane airplane = new Airplane();
    airplane.setName("AAT3");
    airplane.setNumOfSeats(2);
    airplane.setFuelType("UL91");
    airplane.setLandingPerformance(Arrays.asList(
            "-15, 179,403; -5, 186,419; 5, 193,434; 15, 200,450; 25, 207,466; 35, 214,481",
            "-18,188,423;-8,195,439;2,203,456;12,210,472;22,217,489;32,224,505",
            "-21,197,444;-11,205,461;-1,213,479;9,220,496;19,228,513;29,236,530",
            "-25,207,467;-15,215,485;-5,224,503;5,232,521;15,240,539;25,248,557",
            "-28,218,491;-18,227,540;-8,235,529;2,243,548;12,252,567;22,260,586"));
    airplane.setTakeOffPerformance(Arrays.asList(
            "-15, 188,403; -5, 195,419; 5, 203,434; 15, 210,450; 25, 225,482; 35, 240,515",
            "-18,187,400;-8,202,432;2,217,466;12,233,500;22,250,536;32,267,573",
            "-21,206,442;-11,221,474;-1,241,526;9,259,554;19,277,594;29,297,636",
            "-25,229,491;-15,248,537;-5,267,573;5,288,617;15,309,662;25,331,708",
            "-28,254,545;-18,276,591;-8,298,638;2,321,687;12,344,738;22,412,884"));

    return airplane;
  }

  private Airplane loadCessna172()
  {
    Airplane airplane = new Airplane();
    airplane.setName("Cessna 172");
    airplane.setNumOfSeats(4);
    airplane.setFuelType("AVGASS");
    airplane.setLandingPerformance(Arrays.asList(
            "545,1290,565,1320,585,1350,605,1380,625,1415",
            "565,1320,585,1350,605,1385,625,1420,650,1450",
            "585,1355,610,1385,630,1420,650,1455,670,1490",
            "610,1385,630,1425,655,1460,675,1495,695,1530",
            "630,1425,655,1460,675,1495,700,1535,725,1570",
            "655,1460,680,1500,705,1535,725,1575,750,1615",
            "680,1500,705,1540,730,1580,755,1620,780,1660",
            "705,1545,730,1585,760,1625,785,1665,810,1705",
            "735,1585,760,1630,790,1670,815,1715,840,1755"));
    airplane.setTakeOffPerformance(Arrays.asList(
            "860,1465,925,1575,995,1690,1070,1810,1150,1945",
            "940,1600,1010,1720,1090,1850,1170,1990,1260,2135",
            "1025,1755,1110,1890,1195,2035,1285,2190,1380,2355",
            "1125,1925,1215,2080,1310,2240,1410,2420,1515,2605",
            "1235,2120,1335,2295,1440,2480,1550,2685,1660,2880",
            "1355,2345,1465,2545,1585,2755,1705,2975,1825,3205",
            "1495,2605,1615,2830,1745,3075,1875,3320,2010,3585",
            "1645,2910,1785,3170,1920,3440,2065,3730,2215,4045",
            "1820,3265,1970,3575,2120,3880,2280,4225,2450,4615",

            "745,1275,800,1370,860,1470,925,1570,995,1685",
            "810,1390,875,1495,940,1605,1010,1720,1085,1845",
            "885,1520,955,1635,1030,1760,1110,1890,1190,2030",
            "970,1665,1050,1795,1130,1930,1215,2080,1305,2230",
            "1065,1830,1150,1975,1240,2130,1335,2295,1430,2455",
            "1170,2015,1265,2180,1360,2355,1465,2530,1570,2715",
            "1285,2230,1390,2410,1500,2610,1610,2805,1725,3015",
            "1415,2470,1530,2685,1650,2900,1770,3125,1900,3370",
            "1560,2755,1690,3000,1815,3240,1950,3500,2095,3790",

            "610,1055,655,1130,705,1205,760,1290,815,1380",
            "665,1145,720,1230,770,1315,830,1410,890,1505",
            "725,1250,785,1340,845,1435,905,1540,975,1650",
            "795,1365,860,1465,925,1570,995,1685,1065,1805",
            "870,1490,940,1605,1010,1725,1090,1855,1165,1975",
            "955,1635,1030,1765,1110,1900,1195,2035,1275,2175",
            "1050,1800,1130,1940,1220,2090,1310,2240,1400,2395",
            "1150,1985,1245,2145,1340,2305,1435,2475,1540,2650",
            "1270,2195,1370,2375,1475,2555,1580,2745,1695,2950"));
    airplane.setTotalWeight(1100);

    return airplane;
  }

  @Test
  public void at3Calculator() throws Exception
  {
    Airplane airplane = loadAAt3();
    AT3Calculator calculator = new AT3Calculator();
    Calculator.TakeoffLanding result;

    result = calculator.calculateLanding(airplane, 0, 15);

    assertEquals(200, result.getRun());
    assertEquals(450, result.getDistance());

    result = calculator.calculateLanding(airplane, 1460, 12);

    assertEquals(210, result.getRun());
    assertEquals(472, result.getDistance());

    result = calculator.calculateLanding(airplane, 1675, 25);

    assertEquals(220, result.getRun());
    assertEquals(496, result.getDistance());

    result = calculator.calculateLanding(airplane, 9999, 12);

    assertEquals(252, result.getRun());
    assertEquals(567, result.getDistance());

    result = calculator.calculateLanding(airplane, 9999, 100);

    assertEquals(260, result.getRun());
    assertEquals(586, result.getDistance());

    /////

    result = calculator.calculateTakeoff(airplane, 1675, 25);

    assertEquals(259, result.getRun());
    assertEquals(555, result.getDistance());

    result = calculator.calculateTakeoff(airplane, 9999, 12);

    assertEquals(344, result.getRun());
    assertEquals(738, result.getDistance());

    result = calculator.calculateTakeoff(airplane, 9999, 120);

    assertEquals(412, result.getRun());
    assertEquals(884, result.getDistance());
  }

  @Test
  public void cessna172Calculator() throws Exception
  {
    Airplane airplane = loadCessna172();
    Calculator calculator = new Cessna172Calculator();
    Calculator.TakeoffLanding result;

    result = calculator.calculateLanding(airplane, 0, 15);

    assertEquals(175, result.getRun());
    assertEquals(406, result.getDistance());

    result = calculator.calculateLanding(airplane, 1460, 12);

    assertEquals(183, result.getRun());
    assertEquals(418, result.getDistance());

    result = calculator.calculateLanding(airplane, 1675, 25);

    assertEquals(192, result.getRun());
    assertEquals(434, result.getDistance());

    result = calculator.calculateLanding(airplane, 9999, 12);

    assertEquals(233, result.getRun());
    assertEquals(499, result.getDistance());

    result = calculator.calculateLanding(airplane, 9999, 100);

    assertEquals(256, result.getRun());
    assertEquals(534, result.getDistance());

    /////

    result = calculator.calculateTakeoff(airplane, 1675, 25);

    assertEquals(366, result.getRun());
    assertEquals(624, result.getDistance());

    result = calculator.calculateTakeoff(airplane, 9999, 12);

    assertEquals(609, result.getRun());
    assertEquals(1108, result.getDistance());

    result = calculator.calculateTakeoff(airplane, 9999, 120);

    assertEquals(746, result.getRun());
    assertEquals(1406, result.getDistance());

    result = calculator.calculateTakeoff(airplane, -100, 120);

    assertEquals(350, result.getRun());
    assertEquals(592, result.getDistance());

    result = calculator.calculateTakeoff(airplane, 0, 0);

    assertEquals(262, result.getRun());
    assertEquals(446, result.getDistance());
  }

}