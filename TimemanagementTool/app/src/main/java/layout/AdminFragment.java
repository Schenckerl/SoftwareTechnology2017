package layout;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.HelperClasses.UserUtils;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.database.DatabaseHelper;
import at.thelegend27.timemanagementtool.database.Department;
import at.thelegend27.timemanagementtool.database.User;
import at.thelegend27.timemanagementtool.database.WorkingHour;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFragment extends Fragment {

    public AdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Admin Section");

        DatabaseReference md = FirebaseDatabase.getInstance().getReference("Departments");
        md.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Spinner spinner = (Spinner)view.findViewById(R.id.department_selector);
                ArrayList<String> possible_departments = new ArrayList<>();

                for(DataSnapshot dep : dataSnapshot.getChildren()){
                    possible_departments.add(dep.getKey());
                }

                ArrayAdapter < String > adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, possible_departments);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("INT ADMIN", "item has been selected");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button submit = (Button)view.findViewById(R.id.submit_user);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("User" ,"Creating new user");
                final String full_name = ((EditText)view.findViewById(R.id.username)).getText().toString();
                final String email = ((EditText)view.findViewById(R.id.email)).getText().toString();
                final String password = ((EditText)view.findViewById(R.id.password)).getText().toString();
                final int target_hours = Integer.parseInt(((EditText)view.findViewById(R.id.target_hour)).getText().toString());
                String selected_dep = ((Spinner)view.findViewById(R.id.department_selector)).getSelectedItem().toString();
                DatabaseReference md = FirebaseDatabase.getInstance().getReference("Departments/" + selected_dep);
                md.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User new_user = new User(dataSnapshot.getKey(), null, target_hours, 0, 0, null, full_name, email,
                                CurrentSession.getInstance().getCompany().name);

                        UserUtils.registerUser(new_user, password, getActivity());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        final Button submit_dep  = (Button)view.findViewById(R.id.submit_dep);
        submit_dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String department =( (EditText)view.findViewById(R.id.department_name)).getText().toString();
                String supervisor = ((Spinner)view.findViewById(R.id.supervisor_name)).getSelectedItem().toString();
                DatabaseHelper.createNewDepartment(department, supervisor);
            }
        });
    }

}