package com.attendance.action.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Time {
    private static final int evenWeekOffset = 263;
    public static DateTimeFormatter shortFormat = DateTimeFormatter.ofPattern("HH:mm");
    public LocalTime start = null;
    public int after;
    public int before;


    static boolean evenWeek(LocalDate date) {
        return (date.getDayOfYear() - evenWeekOffset) % 7 == 0;
    }


    public Time(JSONObject json) {
        LocalDate today = java.time.LocalDate.now();
        String weekDay = today.getDayOfWeek().toString();
        String evenWeekDay = weekDay+ "_EVEN";
        String oddWeekDay = weekDay+ "_ODD";


        JSONArray daysJSONArray = json.getJSONArray("days");
        List<Object> days = daysJSONArray.toList();

        boolean contain = false, containEven = false, containOdd = false;
        for(Object day : days) {
            if (day.equals(weekDay)) {
                contain = true;
            } else if(day.equals(evenWeekDay)) {
                containEven = true;
            } else if(day.equals(oddWeekDay)) {
                containOdd = true;
            }
        }

        boolean isEven = evenWeek(today);
        String todayState;
        if(isEven && containEven) {
            todayState = evenWeekDay;
        } else if(!isEven && containOdd) {
            todayState = oddWeekDay;
        } else {
            todayState = weekDay;
        }

        start = LocalTime.parse(json.getString(todayState));
        after = json.getInt("limit_after");
        before = json.getInt("limit_before");
    }

    public boolean isNow() {
        LocalTime now = java.time.LocalTime.now();

        int diff = (now.getHour() - start.getHour()) * 60 + now.getMinute() - start.getMinute();

        if(diff >= 0) {
            return diff <= after;
        } else {
            return -diff <= before;
        }
    }
}
