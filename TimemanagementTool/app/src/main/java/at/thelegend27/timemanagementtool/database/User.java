package at.thelegend27.timemanagementtool.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String email;
    public String uid;
    public String fullname;
    public int department;
    public ArrayList<String> working_hours;
    public int targetHour;
    public int overtime_pool;
    public int holidays;
    public boolean isCEO;

    public User(){
    }

    public User(int departmnet, ArrayList<String> working_hours, int targetHour,
                int overtime_pool, int holidays, String uid, String fullname, String email){
        this.department = departmnet;
        this.working_hours = working_hours;
        this.targetHour = targetHour;
        this.overtime_pool = overtime_pool;
        this.holidays = holidays;
        this.uid = uid;
        this.fullname = fullname;
        this.email = email;
        this.isCEO = false;
    }

    public void setCeo(){
        this.isCEO = true;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("fullName", fullname);
        ret.put("email", email);
        ret.put("department", department);
        ret.put("targetHours", targetHour);
        ret.put("overtimePool", overtime_pool);
        ret.put("holidays", holidays);
        ret.put("isCEO", isCEO);

        return ret;
    }
}
