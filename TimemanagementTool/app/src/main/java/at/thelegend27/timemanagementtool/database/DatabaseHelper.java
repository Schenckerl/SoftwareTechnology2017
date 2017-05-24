package at.thelegend27.timemanagementtool.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.HelperClasses.UserUtils;

/**
 * Created by dominik on 17.05.17.
 */

public class DatabaseHelper {
    public static void createNewCompany(User ceo, String company_name){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        Company new_company = new Company(company_name, UUID.randomUUID().toString(), ceo.uid);
        db.child("Companies").child(company_name).setValue(new_company.toMap());

        UserUtils.createNewDbUser(ceo);
    }

    public static void createNewDepartment(String department_name, String department_supervisor){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Department new_department = new Department(CurrentSession.getInstance().getCompany().id, department_supervisor);
        db.child("Departments").child(department_name).setValue(new_department);
    }
}
