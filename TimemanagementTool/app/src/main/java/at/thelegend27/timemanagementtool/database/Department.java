package at.thelegend27.timemanagementtool.database;

import java.util.ArrayList;

public class Department {
    public ArrayList<User> users;
    public int supervisor_id;
    public int company_id;

    public Department(){

    }

    public Department(ArrayList<User> users, int supervisor_id, int company_id){
        this.users = users;
        this.supervisor_id = supervisor_id;
        this.company_id = company_id;
    }
}
