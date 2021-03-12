package ch.baso.flightperformance

import ch.baso.flightperformance.aat3.AT3Calculator
import ch.baso.flightperformance.calc.Calculator
import ch.baso.flightperformance.calc.Calculator.TakeoffLanding
import ch.baso.flightperformance.cessna.Cessna172Calculator
import ch.baso.flightperformance.model.Airplane
import ch.baso.flightperformance.model.AirplaneMassBalance
import ch.baso.flightperformance.model.InputData
import ch.baso.flightperformance.util.Util.getDataPoints
import org.junit.Assert
import org.junit.Test
import java.util.*

class UtilUnitTest {

    @Test
    fun getDataPoints() {
        val dataPoints = getDataPoints("-15, 179,403; -5, 186,419; 5, 193,434; 15, 200,450; 25, 207,466; 35, 214,481")
        Assert.assertEquals(6, dataPoints.size)
        var dataPoint = dataPoints[0]
        Assert.assertEquals(-15, dataPoint.temperature)
        Assert.assertEquals(179, dataPoint.run)
        Assert.assertEquals(403, dataPoint.distance)
        dataPoint = dataPoints[1]
        Assert.assertEquals(-5, dataPoint.temperature)
        Assert.assertEquals(186, dataPoint.run)
        Assert.assertEquals(419, dataPoint.distance)
    }

    private fun loadAAt3(): Airplane {
        val airplane = Airplane()
        airplane.name = "AAT3"
        airplane.numOfSeats = 2
        airplane.fuelType = "UL91"
        airplane.landingPerformance = Arrays.asList(
                "-15, 179,403; -5, 186,419; 5, 193,434; 15, 200,450; 25, 207,466; 35, 214,481",
                "-18,188,423;-8,195,439;2,203,456;12,210,472;22,217,489;32,224,505",
                "-21,197,444;-11,205,461;-1,213,479;9,220,496;19,228,513;29,236,530",
                "-25,207,467;-15,215,485;-5,224,503;5,232,521;15,240,539;25,248,557",
                "-28,218,491;-18,227,540;-8,235,529;2,243,548;12,252,567;22,260,586")
        airplane.takeOffPerformance = Arrays.asList(
                "-15, 188,403; -5, 195,419; 5, 203,434; 15, 210,450; 25, 225,482; 35, 240,515",
                "-18,187,400;-8,202,432;2,217,466;12,233,500;22,250,536;32,267,573",
                "-21,206,442;-11,221,474;-1,241,526;9,259,554;19,277,594;29,297,636",
                "-25,229,491;-15,248,537;-5,267,573;5,288,617;15,309,662;25,331,708",
                "-28,254,545;-18,276,591;-8,298,638;2,321,687;12,344,738;22,412,884")
        airplane.emptyWeight = 383.7
        airplane.emptyMoment = 89.018
        return airplane
    }

    private fun loadCessna172(): Airplane {
        val airplane = Airplane()
        airplane.name = "Cessna 172"
        airplane.numOfSeats = 4
        airplane.fuelType = "AVGASS"
        airplane.landingPerformance = Arrays.asList(
                "545,1290,565,1320,585,1350,605,1380,625,1415",
                "565,1320,585,1350,605,1385,625,1420,650,1450",
                "585,1355,610,1385,630,1420,650,1455,670,1490",
                "610,1385,630,1425,655,1460,675,1495,695,1530",
                "630,1425,655,1460,675,1495,700,1535,725,1570",
                "655,1460,680,1500,705,1535,725,1575,750,1615",
                "680,1500,705,1540,730,1580,755,1620,780,1660",
                "705,1545,730,1585,760,1625,785,1665,810,1705",
                "735,1585,760,1630,790,1670,815,1715,840,1755")
        airplane.takeOffPerformance = Arrays.asList(
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
                "1270,2195,1370,2375,1475,2555,1580,2745,1695,2950")
        airplane.emptyWeight = 800.0
        airplane.emptyMoment = 876.1984
        airplane.momentInInches = true
        return airplane
    }

    @Test
    fun at3Calculator() {
        val airplane = loadAAt3()
        val calculator = AT3Calculator()
        var result: TakeoffLanding

        var inputs = arrayOf(
                intArrayOf(0, 15, 200, 450),
                intArrayOf(1460, 12, 210, 472),
                intArrayOf(1675, 25, 220, 496),
                intArrayOf(9999, 12, 252, 567),
                intArrayOf(9999, 100, 260, 586)
        )
        for (i in inputs.indices) {
            val input = inputs[i]
            result = calculator.calculate(InputData(airplane, ArrayList(), input[1], input[0])).landing
            Assert.assertEquals(input[2], result.run)
            Assert.assertEquals(input[3], result.distance)
        }

        /////
        inputs = arrayOf(
                intArrayOf(1675, 25, 259, 555),
                intArrayOf(9999, 12, 344, 738),
                intArrayOf(9999, 120, 412, 884),
        )
        for (i in inputs.indices) {
            val input = inputs[i]
            result = calculator.calculate(InputData(airplane, ArrayList(), input[1], input[0])).takeoff
            Assert.assertEquals(input[2], result.run)
            Assert.assertEquals(input[3], result.distance)
        }

    }

    @Test
    fun cessna172Calculator() {
        val airplane = loadCessna172()
        val calculator: Calculator = Cessna172Calculator()
        var result: TakeoffLanding
        var inputs = arrayOf(
                intArrayOf(0, 15, 175, 406),
                intArrayOf(1460, 12, 183, 418),
                intArrayOf(1675, 25, 192, 434),
                intArrayOf(9999, 12, 233, 499),
                intArrayOf(9999, 100, 256, 534)
        )
        for (i in inputs.indices) {
            val input = inputs[i]
            result = calculator.calculate(InputData(airplane, ArrayList(), input[1], input[0])).landing
            Assert.assertEquals(input[2], result.run)
            Assert.assertEquals(input[3], result.distance)
        }

        /////
        inputs = arrayOf(
                intArrayOf(1675, 25, 366, 624),
                intArrayOf(9999, 12, 609, 1108),
                intArrayOf(9999, 120, 746, 1406),
                intArrayOf(-100, 120, 350, 592),
                intArrayOf(0, 0, 262, 446)
        )

        //required for total weight calculation which should be 2550 pounds
        val weights: MutableList<AirplaneMassBalance> = ArrayList()
        weights.add(AirplaneMassBalance("Test weight", 1.0, 357.0, 400.0, 1.0))
        for (i in inputs.indices) {
            val input = inputs[i]
            result = calculator.calculate(InputData(airplane, weights, input[1], input[0])).takeoff
            Assert.assertEquals(input[2], result.run)
            Assert.assertEquals(input[3], result.distance)
        }
    }
}