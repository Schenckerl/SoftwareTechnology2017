package at.thelegend27.timemanagementtool.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dominik on 17.05.17.
 */

public class DatabaseHelper {

    public static void addNewCompany(Company to_add){
        DatabaseReference md = FirebaseDatabase.getInstance().getReference("Companies");
        md.child(to_add.name).setValue(to_add);
    }

    public static void addNewDepartment(String company, Department to_add){

    }
}
