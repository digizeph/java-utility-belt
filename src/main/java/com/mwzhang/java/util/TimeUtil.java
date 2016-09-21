package com.mwzhang.java.util;

import com.mwzhang.java.util.io.Output;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Arrays;

/**
 * Created by Mingwei Zhang on 5/2/14.
 * <p>
 * Utility functions for time related tasks.
 */
public class TimeUtil {
    /**
     * Convert UNIX time (a integer) to a Joda DateTime object.
     *
     * @param unixtime the unix time
     * @return a DateTime object converted from UNIX time stamp
     */
    public static DateTime getTimeFromUnix(long unixtime) {
        return new DateTime(unixtime * 1000);
    }

    /**
     * Get the local time. Current setting for time zone is Pacific time "America/Los_Angeles"
     *
     * @return the DateTime variable with local timezone setting.
     */
    public static DateTime getLocalTime() {
        return new DateTime().toDateTime(DateTimeZone.forID("America/Los_Angeles"));
    }

    /**
     * Transform a date string from "YYYY-MM-DD" format to a DateTime object.
     *
     * @param time the input string for the date
     * @return a corresponding DateTime object
     */
    public static DateTime stringToDateTime(String time) {
        String[] list = new String[0];
        if (time.contains("_")) {
            list = time.split("_");
        } else if (time.contains("-")) {
            list = time.split("-");
        }

        Output.pl(Arrays.toString(list));
        if (list.length != 3) {
            Output.pl("Wrong size");
            return null;
        }
        int year = Integer.parseInt(list[0]);
        int month = Integer.parseInt(list[1]);
        int day = Integer.parseInt(list[2]);
        DateTime startTime;
        try {
            startTime = new DateTime(year, month, day, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return startTime;
    }
}
