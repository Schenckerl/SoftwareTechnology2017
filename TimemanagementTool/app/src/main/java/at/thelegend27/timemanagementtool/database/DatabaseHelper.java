package at.thelegend27.timemanagementtool.database;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.HelperClasses.UserUtils;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.TimemanagementActivity;
import layout.AdminFragment;

/**
 * Created by dominik on 17.05.17.
 */

public class DatabaseHelper {
    public static void createNewCompany(User ceo, String company_name){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        Company new_company = new Company(company_name, ceo.uid);
        db.child("Companies").child(company_name).setValue(new_company.toMap());

        UserUtils.createNewDbUser(ceo);
    }

    public static void initDepartment(String company){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Department new_department = new Department(company, FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.child("Departments").child("Default "+ company +" Department").setValue(new_department);
    }

    public static void createNewDepartment(final String department_name, final String department_supervisor, final Context context){
        final DatabaseReference md = FirebaseDatabase.getInstance().getReference("Users");

        md.orderByChild("fullName").equalTo(department_supervisor).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //updating user
                User update = dataSnapshot.getValue(User.class);
                update.isSupervisor = true;
                update.department = department_name;

                md.child(dataSnapshot.getKey()).setValue(update);

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                Department new_department = new Department(CurrentSession.getInstance().getCompany().name, dataSnapshot.getKey());
                db.child("Departments").child(department_name).setValue(new_department);

                Toast.makeText(context, "Department created", Toast.LENGTH_LONG).show();
                ((TimemanagementActivity)context).displaySelectedScreen(R.layout.fragment_dashboard);
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

    public static void getUsersForCompany(String company, final Activity context){
        Log.d("GET", "getting users for company: " + company);
        final ArrayList<String> possible_users = new ArrayList<>();

        final Spinner spinner = (Spinner)(context.findViewById(R.id.supervisor_name));

        DatabaseReference md = FirebaseDatabase.getInstance().getReference("Users");
        md.orderByChild("company").equalTo(company).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("User", "found user");
                if(!dataSnapshot.getValue(User.class).isSupervisor) {
                    possible_users.add(dataSnapshot.getValue(User.class).fullName);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item, possible_users);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);
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
