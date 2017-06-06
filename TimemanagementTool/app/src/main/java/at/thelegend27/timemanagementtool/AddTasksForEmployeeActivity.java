package at.thelegend27.timemanagementtool;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.database.Department;
import at.thelegend27.timemanagementtool.database.User;

public class AddTasksForEmployeeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner setDepartment, setEmployee;
    EditText taskName, taskDescription;
    TextView deadline;
    Map<String, String> usersId = new HashMap<String, String>();
    String idOfSelectedUser;
    Button addButton, setDate;
    FirebaseDatabase database;
    DatabaseReference reference;
    int DIALOG_DATE = 1;
    int myYear ;
    int myMonth ;
    int myDay ;
    final User user = CurrentSession.getInstance().getCurrent_user();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Create Task for Employee");
        setContentView(R.layout.activity_add_tasks_for_employee);
        setDate = (Button)findViewById(R.id.addTaskDeadlineButtonAdmin);
        deadline = (TextView)findViewById(R.id.addTaskDeadlineAdmin);
        setDepartment = (Spinner)findViewById(R.id.department_task_id);
        setDepartment.setOnItemSelectedListener(this);
        taskDescription = (EditText)findViewById(R.id.addTaskDescriptionAdmin);
        setEmployee = (Spinner)findViewById(R.id.employee_task_id);
        addButton = (Button)findViewById(R.id.addTaskButtonAdmin);
        taskName = (EditText)findViewById(R.id.addTaskNameAdmin);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTask();
            }
        });
        final String date = new DateTime().toString("yyyy-MM-dd");
        deadline.setText(date);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Departments");
        if(user.isCEO) {
            reference.orderByChild("company").equalTo(user.getCompany()).
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final List<String> departments = new ArrayList<String>();
                            for (DataSnapshot departmentsSnapshot : dataSnapshot.getChildren()) {
                                String departmentName = departmentsSnapshot.getKey();
                                departments.add(departmentName);
                            }
                            ArrayAdapter<String> departments_adapter = new ArrayAdapter<String>
                                    (AddTasksForEmployeeActivity.this,
                                            android.R.layout.simple_spinner_item, departments);
                            departments_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            setDepartment.setAdapter(departments_adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
        else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Departments/"+CurrentSession.getInstance().getDepartment().name);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final List<String> department = new ArrayList<String>();
                    department.add(dataSnapshot.getKey());
                    ArrayAdapter<String> departments_adapter = new ArrayAdapter<String>
                            (AddTasksForEmployeeActivity.this,
                                    android.R.layout.simple_spinner_item, department);
                    departments_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    setDepartment.setAdapter(departments_adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            String date = new DateTime().toString("yyyy-MM-dd");
            String[] dateParse = date.split("-");
            myYear = Integer.parseInt(dateParse[0]);
            myMonth = Integer.parseInt(dateParse[1]) - 1;
            myDay = Integer.parseInt(dateParse[2]);

            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myYear, myMonth, myDay);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear + 1;
            myDay = dayOfMonth;
            String day = myDay + "";
            String month = myMonth + "";
            if(myDay < 10) {
                day = "0";
                String buf = myDay + "";
                day += buf;
            }
            if(myMonth < 10) {
                month = "0";
                String buf = myMonth + "";
                month += buf;
            }
            deadline.setText(myYear + "-" + month + "-" + day);
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedDepartment = parent.getItemAtPosition(position).toString();
        reference = database.getReference("Users");
        reference.orderByChild("department").equalTo(selectedDepartment).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List<String> users = new ArrayList<String>();
                        for(DataSnapshot departmentsSnapshot : dataSnapshot.getChildren()) {
                            String userName = departmentsSnapshot.child("fullName").getValue(String.class);
                            usersId.put(departmentsSnapshot.getKey(), userName);
                            users.add(userName);

                        }
                        ArrayAdapter<String> users_adapter = new ArrayAdapter<String>
                                (AddTasksForEmployeeActivity.this,
                                        android.R.layout.simple_spinner_item, users);
                        users_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        setEmployee.setAdapter(users_adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void setTask() {
        getUserId();
        reference = database.getReference("Tasks");
        String id = reference.push().getKey();
        Task task = new Task(user.getFullName(), deadline.getText().toString(), taskName.getText().toString(), taskDescription.getText().toString(), idOfSelectedUser, id);
        Map<String, Object> taskValues = task.toMap();
        Map<String, Object> newTask = new HashMap<>();
        newTask.put(id, taskValues);
        reference.updateChildren(newTask);
        this.finish();
    }

    private void getUserId() {
        String userName = setEmployee.getSelectedItem().toString();
        for(Map.Entry<String, String> entry : usersId.entrySet()) {
            if(entry.getValue() == userName)
                idOfSelectedUser = entry.getKey();
        }
    }
}
