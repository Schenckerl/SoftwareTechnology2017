package at.thelegend27.timemanagementtool.HelperClasses;

import at.thelegend27.timemanagementtool.database.Company;
import at.thelegend27.timemanagementtool.database.Department;
import at.thelegend27.timemanagementtool.database.User;

public class CurrentSession {
    private User current_user;
    private Company company;
    private Department department;

    private static CurrentSession instance;

    public CurrentSession getInstance(){
        if(instance == null){
            instance = new CurrentSession();
        }
        return instance;
    }

    public void setUser(User user){this.current_user = user;}
    public void setCompany(Company company){this.company = company;}
    public void setDepartment(Department department){this.department = department;}

}
