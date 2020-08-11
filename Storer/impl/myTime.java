package Storer.impl;

import java.util.Calendar;

public class myTime {
    public static String formated_time (long time){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int weekday = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int milli = cal.get(Calendar.MILLISECOND);
        String formated_Date = ""+hour+":"+minute+":"+second+"-"+day+"/"+month+"/"+year;
        return formated_Date;
    }
    public static String getDayAndTime (long time){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int weekday = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int milli = cal.get(Calendar.MILLISECOND);

        String Weekday = "";
        switch (weekday){
            case Calendar.MONDAY:
                Weekday = "monday";
                break;
            case Calendar.TUESDAY:
                Weekday = "tuesday";
                break;
            case Calendar.WEDNESDAY:
                Weekday = "wednesday";
                break;
            case Calendar.THURSDAY:
                Weekday = "thursday";
                break;
            case Calendar.FRIDAY:
                Weekday = "friday";
                break;
            case Calendar.SATURDAY:
                Weekday = "saturday";
                break;
            case Calendar.SUNDAY:
                Weekday = "sunday";
                break;
        }

        String formated_Date = Weekday+" "+hour+":"+minute+":"+second;
        return formated_Date;
    }
}
