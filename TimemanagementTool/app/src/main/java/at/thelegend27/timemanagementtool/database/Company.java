package at.thelegend27.timemanagementtool.database;

import java.util.ArrayList;

/**
 * Created by dominik on 17.05.17.
 */

public class Company {
    public String name;
    public int id;
    public ArrayList<User> users;
    public ArrayList<Department> departments;
    public int ceo_id;

    public Company(){

    }

    public Company(String name, int id, ArrayList<User> users, ArrayList<Department> departments,
                   int ceo_id){
        this.name = name;
        this.id = id;
        this.users = users;
        this.departments = departments;
        this.ceo_id = ceo_id;
    }
}
