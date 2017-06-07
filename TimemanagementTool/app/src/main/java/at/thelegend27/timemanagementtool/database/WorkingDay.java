package at.thelegend27.timemanagementtool.database;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

public class WorkingDay {
    public DateTime begin;
    public DateTime end;
    public String key = null;
    public long duration = 0;
    public int break_time = 0;

    public WorkingDay(){
        this.begin = new DateTime(DateTimeZone.forID("Europe/Berlin"));
    }

    public WorkingDay(String begin){
        this.begin = DateTime.parse(begin);
    }

    public WorkingDay(String begin, String end, String key, long duration){
        this.begin = DateTime.parse(begin);
        this.end = DateTime.parse(end);
        this.key = key;
        this.duration = duration;
    }
    public LocalTime getBegin(){
        return begin.toLocalTime();
    }
}
