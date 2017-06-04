package at.thelegend27.timemanagementtool.database;

import org.joda.time.DateTime;

class Time{
    public int hours;
    public int minutes;

    public Time(int hours, int minutes){
        this.hours = hours;
        this.minutes = minutes;
    }
}

public class WorkingHour {
    public DateTime day;
    public Time begin;
    public Time end = null;
    public Time break_duration = null;

    public WorkingHour(){
        this.day = new DateTime();
        this.begin = new Time(this.day.getHourOfDay(),this.day.getHourOfDay());
    }
}
