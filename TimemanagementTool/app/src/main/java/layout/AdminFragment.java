package layout;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

        final Button submit = (Button)view.findViewById(R.id.submit_user);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User new_user;
                Log.d("User" ,"Creating new user");
                String full_name = ((EditText)view.findViewById(R.id.username)).getText().toString();
                String email = ((EditText)view.findViewById(R.id.email)).getText().toString();
                String password = ((EditText)view.findViewById(R.id.password)).getText().toString();
                int target_hours = Integer.parseInt(((EditText)view.findViewById(R.id.target_hour)).getText().toString());
                int department = Integer.parseInt(((EditText)view.findViewById(R.id.department)).getText().toString());

                new_user = new User(department, null, target_hours, 0, 0, null, full_name, email);

                UserUtils.registerUser(new_user, password);
            }
        });

        final Button submit_dep  = (Button)view.findViewById(R.id.submit_dep);
        submit_dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String department =( (EditText)view.findViewById(R.id.department_name)).getText().toString();
                String supervisor = ((EditText)view.findViewById(R.id.department_supervisor)).getText().toString();
                DatabaseHelper.createNewDepartment(department, supervisor);
            }
        });
    }

}