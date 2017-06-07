package layout;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.HelperClasses.EmailFieldValidatorHelper;
import at.thelegend27.timemanagementtool.HelperClasses.PasswordFieldValidatorHelper;
import at.thelegend27.timemanagementtool.HelperClasses.RequiredFieldValidatorHelper;
import at.thelegend27.timemanagementtool.HelperClasses.UserUtils;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.TimemanagementActivity;
import at.thelegend27.timemanagementtool.database.DatabaseHelper;
import at.thelegend27.timemanagementtool.database.Department;
import at.thelegend27.timemanagementtool.database.User;
/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFragment extends Fragment implements View.OnFocusChangeListener {

    TimemanagementActivity activity;
    private TextInputLayout usernameWrapper;
    private TextInputLayout emailWrapper;
    private TextInputLayout passwordWrapper;
    private TextInputLayout targetHoursWrapper;
    private RequiredFieldValidatorHelper usernameFieldValidator;
    private EmailFieldValidatorHelper emailFieldValidator;
    private PasswordFieldValidatorHelper passwordFieldValidator;
    private RequiredFieldValidatorHelper targetHoursFieldValidator;
    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText targetHoursEditText;
    private TextInputLayout departmentWrapper;
    private RequiredFieldValidatorHelper departmentFieldValidator;
    private TextInputEditText departmentEditText;

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
        activity = (TimemanagementActivity) getActivity();

        usernameWrapper = (TextInputLayout)view.findViewById(R.id.username_wrapper);
        emailWrapper = (TextInputLayout)view.findViewById(R.id.email_wrapper);
        passwordWrapper = (TextInputLayout)view.findViewById(R.id.password_wrapper);
        targetHoursWrapper = (TextInputLayout)view.findViewById(R.id.target_hour_wrapper);
        usernameFieldValidator = new RequiredFieldValidatorHelper(usernameWrapper);
        emailFieldValidator = new EmailFieldValidatorHelper(emailWrapper);
        passwordFieldValidator = new PasswordFieldValidatorHelper(passwordWrapper, 6);
        targetHoursFieldValidator = new RequiredFieldValidatorHelper(targetHoursWrapper);
        usernameEditText = (TextInputEditText)view.findViewById(R.id.username);
        emailEditText = (TextInputEditText)view.findViewById(R.id.email);
        passwordEditText = (TextInputEditText)view.findViewById(R.id.password);
        targetHoursEditText = (TextInputEditText)view.findViewById(R.id.target_hour);
        usernameEditText.setOnFocusChangeListener(this);
        emailEditText.setOnFocusChangeListener(this);
        passwordEditText.setOnFocusChangeListener(this);
        targetHoursEditText.setOnFocusChangeListener(this);

        departmentWrapper = (TextInputLayout)view.findViewById(R.id.department_name_wrapper);
        departmentFieldValidator = new RequiredFieldValidatorHelper(departmentWrapper);
        departmentEditText = (TextInputEditText)view.findViewById(R.id.department_name);
        departmentEditText.setOnFocusChangeListener(this);

        DatabaseReference md = FirebaseDatabase.getInstance().getReference("Departments");
        md.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Spinner spinner = (Spinner)view.findViewById(R.id.department_selector);
                ArrayList<String> possible_departments = new ArrayList<>();

                for(DataSnapshot dep : dataSnapshot.getChildren()){
                    Department to_check = dep.getValue(Department.class);
                    if(to_check.company.equals(CurrentSession.getInstance().getCompany().name)) {
                        Log.d("ADMIN", "we got a department for our company");
                        possible_departments.add(dep.getKey());
                    }
                }

                ArrayAdapter < String > adapter = new ArrayAdapter<>(activity,
                        android.R.layout.simple_spinner_item, possible_departments);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseHelper.getUsersForCompany(CurrentSession.getInstance().getCompany().name, activity);


        final Button submit = (Button)view.findViewById(R.id.submit_user);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("User" ,"Creating new user");
                final String full_name = usernameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String targetHours = targetHoursEditText.getText().toString();
                String selected_dep = ((Spinner)view.findViewById(R.id.department_selector)).getSelectedItem().toString();

                boolean isUsernameValid = usernameFieldValidator.validate(full_name);
                boolean isEmailValid = emailFieldValidator.validate(email);
                boolean isPasswordValid = passwordFieldValidator.validate(password);
                boolean istargetHoursValid = targetHoursFieldValidator.validate(String.valueOf(targetHours));

                if (!isUsernameValid || !isEmailValid || !isPasswordValid || !istargetHoursValid || (selected_dep == null)) {
                    //go ahead ans submit the form for all things are fine now
                    Toast.makeText(view.getContext(), "Field Validations failed! Please check your inputs", Toast.LENGTH_SHORT).show();
                    return;
                }
                final int target_hours = Integer.parseInt(targetHours);

                DatabaseReference md = FirebaseDatabase.getInstance().getReference("Departments/" + selected_dep);
                md.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User new_user = new User(dataSnapshot.getKey(), null, target_hours, 0, 0, null, full_name, email,
                                CurrentSession.getInstance().getCompany().name, false);
                        UserUtils.registerUser(new_user, password, activity);
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
                String department = departmentEditText.getText().toString();
                String supervisor = ((Spinner)view.findViewById(R.id.supervisor_name)).getSelectedItem().toString();

                boolean isDepartmentValid = departmentFieldValidator.validate(String.valueOf(department));

                if (!isDepartmentValid || (supervisor == null)) {
                    //go ahead ans submit the form for all things are fine now
                    Toast.makeText(view.getContext(), "Field Validations failed! Please check your inputs", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseHelper.createNewDepartment(department, supervisor, activity);
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            return; //we want to validate only fields loosing focus and not fields gaining focus
        }

        int id = v.getId();
        if (id == R.id.username) {
            usernameFieldValidator.validate(usernameEditText.getText().toString());
        } else if (id == R.id.email) {
            emailFieldValidator.validate(emailEditText.getText().toString());
        } else if (id == R.id.password) {
            passwordFieldValidator.validate(passwordEditText.getText().toString());
        } else if (id == R.id.target_hour) {
            targetHoursFieldValidator.validate(targetHoursEditText.getText().toString());
        } else if (id == R.id.department_name) {
            departmentFieldValidator.validate(departmentEditText.getText().toString());
        }
    }
}