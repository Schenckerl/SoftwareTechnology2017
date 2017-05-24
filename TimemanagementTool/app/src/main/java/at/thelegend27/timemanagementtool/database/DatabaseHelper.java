package at.thelegend27.timemanagementtool.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

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
}
