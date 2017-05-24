package at.thelegend27.timemanagementtool.database;

import android.app.UiAutomation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String email;
    public String uid;
    public String fullname;
    public int departmnet;
    public ArrayList<WorkingHour> working_hours;
    public int targetHour;
    public int overtime_pool;
    public int holidays;

    public User(){
    }

    public User(int departmnet, ArrayList<WorkingHour> working_hours, int targetHour,
                int overtime_pool, int holidays, String uid, String fullname, String email){
        this.departmnet = departmnet;
        this.working_hours = working_hours;
        this.targetHour = targetHour;
        this.overtime_pool = overtime_pool;
        this.holidays = holidays;
        this.uid = uid;
        this.fullname = fullname;
        this.email = email;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("fullName", fullname);
        ret.put("email", email);
        ret.put("department", departmnet);
        ret.put("targetHours", targetHour);
        ret.put("overtimePool", overtime_pool);
        ret.put("holidays", holidays);

        return ret;
    }
}
