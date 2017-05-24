package at.thelegend27.timemanagementtool.database;

import java.util.ArrayList;

public class User {
    public int uid;
    public String username;
    public int departmnet;
    public ArrayList<WorkingHour> working_hours;
    public int targetHour;
    public int overtime_pool;
    public int holidays;

    public User(){
    }

    public User(int departmnet, ArrayList<WorkingHour> working_hours, int targetHour,
                int overtime_pool, int holidays, int uid, String username){
        this.departmnet = departmnet;
        this.working_hours = working_hours;
        this.targetHour = targetHour;
        this.overtime_pool = overtime_pool;
        this.holidays = holidays;
        this.uid = uid;
        this.username = username;
    }
}
