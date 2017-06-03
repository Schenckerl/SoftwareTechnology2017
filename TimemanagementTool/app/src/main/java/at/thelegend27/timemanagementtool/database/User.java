package at.thelegend27.timemanagementtool.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String email;
    public String uid;
    public String fullName;
    public String department;
    public String company;
    public ArrayList<String> working_hours;
    public int targetHours;
    public int overtimePool;
    public int holidays;
    public boolean isCEO;
    public boolean isSupervisor;

    public User(){
    }

    public User(String departmnet, ArrayList<String> working_hours, int targetHour,
                int overtime_pool, int holidays, String uid, String fullname, String email,
                String company, boolean isSupervisor){
        this.department = departmnet;
        this.working_hours = working_hours;
        this.targetHours = targetHour;
        this.overtimePool = overtime_pool;
        this.holidays = holidays;
        this.uid = uid;
        this.fullName = fullname;
        this.email = email;
        this.isCEO = false;
        this.company = company;
        this.isSupervisor = isSupervisor;
    }

    public void setCeo(){
        this.isCEO = true;
    }



    public Map<String, Object> toMap(){
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("fullName", fullName);
        ret.put("email", email);
        ret.put("department", department);
        ret.put("targetHours", targetHours);
        ret.put("overtimePool", overtimePool);
        ret.put("holidays", holidays);
        ret.put("isCEO", isCEO);
        ret.put("company", company);

        return ret;

    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDepartment() {
        return department;
    }

    public String getCompany() {
        return company;
    }

    public ArrayList<String> getWorking_hours() {
        return working_hours;
    }

    public int getTargetHours() {
        return targetHours;
    }

    public int getOvertimePool() {
        return overtimePool;
    }

    public int getHolidays() {
        return holidays;
    }

    public boolean isCEO() {
        return isCEO;
    }

    public boolean isSupervisor() {
        return isSupervisor;
    }
}
