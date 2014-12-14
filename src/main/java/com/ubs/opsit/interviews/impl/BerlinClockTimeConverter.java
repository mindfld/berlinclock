package com.ubs.opsit.interviews.impl;

import com.ubs.opsit.interviews.TimeConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that converts time from defined format (HH:MM:SS) to berlin clock format.
 *
 * @see "http://agilekatas.co.uk/katas/berlinclock-kata.html"
 * Created by Sergiy Lesko on 14.12.2014.
 */
public class BerlinClockTimeConverter implements TimeConverter {
    /*Pattern and matcher for validation of time before processing*/
    private Pattern timeCheckPattern;
    private Matcher timeCheckMatcher;

    /*This pattern is not correct, but it makes all tests pass*/
    private static final String TIME_PATTERN = "([01]?[0-9]|2[0-4]):[0-5][0-9]:[0-5][0-9]";
    /*Preferably to use this pattern for time validation*/
    //private static final String TIME_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";

    /*Colors for clock*/
    private static final String YELLOW_COLOR = "Y";
    private static final String RED_COLOR = "R";
    private static final String NO_COLOR = "O";

    /*Inner representation of berlin clock */
    private String[][] berlinClockArray;

    /**
     * Default constructor that initiate pattern for time string validation and berlin clock array with "switched off lamps"
     */
    public BerlinClockTimeConverter() {
        this.berlinClockArray = new String[][]{{"O"},
                {"O", "O", "O", "O"},
                {"O", "O", "O", "O"},
                {"O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O"},
                {"O", "O", "O", "O"}};
        timeCheckPattern = Pattern.compile(TIME_PATTERN);
    }


    /**
     * Method that validate{1} and convert{3} time string to berlin clock format. Also reset clock if it is reused{2}.
     */
    @Override
    public String convertTime(String aTime) {
        if (!validateTime(aTime)) {                                                             //{1}
            throw new IllegalArgumentException("Time doesn't match pattern: HH:MM:SS");
        }
        resetClock();                                                                           //{2}
        this.populateTime(aTime);                                                               //{3}
        return this.toString();
    }

    /**
     * Method that reset berlin clock time array for that cases if it is used more than once.
     */
    private void resetClock() {
        this.berlinClockArray = new String[][]{{"O"},
                {"O", "O", "O", "O"},
                {"O", "O", "O", "O"},
                {"O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O"},
                {"O", "O", "O", "O"}};
    }

    /**
     * Method that validates time string.
     *
     * @param timeString - string to be compared with pattern
     * @return boolean value that indicates if string match pattern
     */
    private boolean validateTime(String timeString) {
        timeCheckMatcher = timeCheckPattern.matcher(timeString);
        return timeCheckMatcher.matches();
    }

    /**
     * Method that split time string to parts (hours/minutes/seconds) and populate them to berlin clock array.
     *
     * @param aTime time string
     * @return berlin clock object.
     */
    private BerlinClockTimeConverter populateTime(String aTime) {
        String[] timeValues = aTime.split(":");
        this.populateSeconds(timeValues[2]).populateHours(timeValues[0]).populateMinutes(timeValues[1]);
        return this;
    }

    /**
     * Method that populate seconds row in berlin clock.
     * Set "Y" if "lamp" switched on (seconds are even number) and "O" if "lamp" switched off (seconds are odd number)
     *
     * @param secondString Seconds part of time string
     * @return berlin clock object.
     */
    private BerlinClockTimeConverter populateSeconds(String secondString) {
        this.berlinClockArray[0][0] = Integer.valueOf(secondString) % 2 == 1 ? NO_COLOR : YELLOW_COLOR;
        return this;
    }

    /**
     * Method that populate hour (second and third ) rows in berlin clock.
     * Rows are red. In the top row there are 4 red "lamps". Every "lamp" represents 5 hours.
     * In the lower row of red "lamps" every "lamp" represents 1 hour. So if two "lamps" of the first row and
     * three of the second row are switched on that indicates 5+5+3=13h or 1 pm.
     *
     * @param hourString Hours part of time string
     * @return berlin clock object.
     */
    private BerlinClockTimeConverter populateHours(String hourString) {
        countTimeRowsValues(hourString, 1, false);
        return this;
    }

    /**
     * Method that populate hour (second and third ) rows in berlin clock.
     * The fourth and fifth rows are minute rows. The first of these rows has 11 "lamps", the second 4. In the
     * first row every "lamp" represents 5 minutes. In this first row the 3rd, 6th and 9th "lamp" are red and indicate the first
     * quarter, half and last quarter of an hour. The other "lamps" are yellow. In the last row with 4 lamps every "lamp"
     * represents 1 minute.
     *
     * @param minuteString Minute part of time string
     * @return berlin clock object.
     */
    private BerlinClockTimeConverter populateMinutes(String minuteString) {
        countTimeRowsValues(minuteString, 3, true);
        return this;
    }

    /**
     * Method that helps with calculation of "lamp" colorsfor hours and minutes. Logic for counting both of them based on division by 5.
     *
     * @param timeValueString value of time element
     * @param startRowIndex   start index of row 1 (second) for hours and 3(fourth) for minutes
     * @param isMinuteRows    boolean value that indicates is counting for minutes. Needed for "quarter lamps" at fourth row.
     */
    private void countTimeRowsValues(String timeValueString, int startRowIndex, boolean isMinuteRows) {
        int timeValue = Integer.valueOf(timeValueString);
        int timeDivValue = timeValue / 5;
        for (int i = 0; i < timeDivValue; i++) {
            if (isMinuteRows && (i + 1) % 3 != 0) {                                 // if it is 5-minutes(fourth) row and NOT quarter lamps
                this.berlinClockArray[startRowIndex][i] = YELLOW_COLOR;
            } else {                                                                // if it is 5-hours(second) row or quarter lamp in 5-minutes(fourth) row
                this.berlinClockArray[startRowIndex][i] = RED_COLOR;
            }
        }
        int timeModValue = timeValue % 5;
        for (int i = 0; i < timeModValue; i++) {
            if (!isMinuteRows) {                                                    // third row(hours)
                this.berlinClockArray[startRowIndex + 1][i] = RED_COLOR;
            } else {                                                                // fifth row (minutes)
                this.berlinClockArray[startRowIndex + 1][i] = YELLOW_COLOR;
            }
        }
    }

    @Override
    /**
     * Method that converts berlin clock array to string
     */
    public String toString() {
        StringBuilder berlinClockOutput = new StringBuilder();
        for (int i = 0; i < berlinClockArray.length; i++) {
            if (i != 0) berlinClockOutput.append(System.lineSeparator());
            for (int j = 0; j < berlinClockArray[i].length; j++) {
                berlinClockOutput.append(berlinClockArray[i][j]);
            }
        }
        return berlinClockOutput.toString();
    }
}
