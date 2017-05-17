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
        md.push().setValue(to_add);
    }

    public static void addDepartmentToCompany(Department dep, Company comp){
        DatabaseReference md = FirebaseDatabase.getInstance().getReference("Companies");

        md.orderByChild("name").equalTo("TL").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("we found our company");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
