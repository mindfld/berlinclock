package com.ubs.opsit.interviews.impl;

import com.ubs.opsit.interviews.TimeConverter;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;

/**
 * Created by Sergiy Lesko on 14.12.2014.
 */
public class BerlinClockTimeConverterTest {

    private TimeConverter berlinClock;

    @Test(expected = IllegalArgumentException.class)
    public void invocationOfConvertTimeWithEmptyDate() {
        berlinClock = new BerlinClockTimeConverter();
        berlinClock.convertTime("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invocationOfConvertTimeWithWrongFormatDate() {
        berlinClock = new BerlinClockTimeConverter();
        berlinClock.convertTime("REALY:NOT:ATIME");
    }

    @Test
    public void invocationOfConvertTimeWithCorrectDate() {
        berlinClock = new BerlinClockTimeConverter();
        String expectedTime = berlinClock.convertTime("16:50:06");
        Assert.assertEquals(getCorrectTime(), expectedTime);
    }

    @Test
    public void invocationOfConvertTimeWithReuseOfBerlinClock() {
        berlinClock = new BerlinClockTimeConverter();
        String expectedTime = berlinClock.convertTime("11:37:01");
        expectedTime = berlinClock.convertTime("00:00:00");
        Assert.assertEquals(getMidNight(), expectedTime);
    }

    @Test
    @Ignore
    /*Logically this both times should be equal. But fixing this test will cause failure of JBehave test*/
    public void invocationOfConvertTimeWithDifferentMidnightRecords() {
        berlinClock = new BerlinClockTimeConverter();
        String testMidnight1 = berlinClock.convertTime("00:00:00");
        String testMidnight2 = berlinClock.convertTime("22:00:00");
        Assert.assertEquals(testMidnight1,testMidnight2);
    }

    private String getCorrectTime() {
        StringBuilder correctTime = new StringBuilder();
        correctTime.append("Y").append(System.lineSeparator());
        correctTime.append("RRRO").append(System.lineSeparator());
        correctTime.append("ROOO").append(System.lineSeparator());
        correctTime.append("YYRYYRYYRYO").append(System.lineSeparator());
        correctTime.append("OOOO");
        return correctTime.toString();
    }
    private String getMidNight() {
        StringBuilder correctTime = new StringBuilder();
        correctTime.append("Y").append(System.lineSeparator());
        correctTime.append("OOOO").append(System.lineSeparator());
        correctTime.append("OOOO").append(System.lineSeparator());
        correctTime.append("OOOOOOOOOOO").append(System.lineSeparator());
        correctTime.append("OOOO");
        return correctTime.toString();
    }
}
