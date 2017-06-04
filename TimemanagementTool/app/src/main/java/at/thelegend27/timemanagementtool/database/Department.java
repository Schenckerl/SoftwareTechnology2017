package at.thelegend27.timemanagementtool.database;

import java.util.ArrayList;

public class Department {
    public String company;
    public String supervisor;

    public Department(){

    }

    public Department(String company_id, String supervisor){
        this.company = company_id;
        this.supervisor = supervisor;
    }
}
