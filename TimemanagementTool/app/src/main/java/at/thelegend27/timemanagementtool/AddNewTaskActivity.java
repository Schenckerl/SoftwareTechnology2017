package at.thelegend27.timemanagementtool;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.HelperClasses.RequiredFieldValidatorHelper;
import at.thelegend27.timemanagementtool.database.User;

public class AddNewTaskActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    TextInputEditText taskName, taskDescription;
    private RequiredFieldValidatorHelper taskNameFieldValidator;
    TextInputLayout taskNameWrapper;
    TextView deadline;
    Button addButton, setDate;
    FirebaseDatabase database;
    DatabaseReference reference;
    int DIALOG_DATE = 1;
    int myYear ;
    int myMonth ;
    int myDay ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        taskDescription = (TextInputEditText) findViewById(R.id.addTaskDescription);
        taskName = (TextInputEditText) findViewById(R.id.addTaskName);
        addButton = (Button)findViewById(R.id.addTaskButton);
        setDate = (Button)findViewById(R.id.addTaskDeadlineButton);
        deadline = (TextView)findViewById(R.id.addTaskDeadline);

        taskNameWrapper = (TextInputLayout)findViewById(R.id.addTaskName_wrapper);
        taskNameFieldValidator = new RequiredFieldValidatorHelper(taskNameWrapper);
        taskName.setOnFocusChangeListener(this);

        String date = new DateTime().toString("yyyy-MM-dd");
        deadline.setText(date);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isTaskNameValid = taskNameFieldValidator.validate(taskName.getText().toString());
                if (!isTaskNameValid) {
                    //go ahead ans submit the form for all things are fine now
                    Toast.makeText(getBaseContext(), "Field Validations failed! Please check your inputs", Toast.LENGTH_SHORT).show();
                    return;
                }

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Tasks");
                setTask();

            }
        });


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


    private void setTask() {
        final User user = CurrentSession.getInstance().getCurrent_user();
        String id = reference.push().getKey();
        Task task = new Task(user.getFullName(), deadline.getText().toString(),
                taskName.getText().toString(), taskDescription.getText().toString(), user.uid, id, false);
        Map<String, Object> taskValues = task.toMap();
        Map<String, Object> newTask = new HashMap<>();
        newTask.put(id, taskValues);
        reference.updateChildren(newTask);
        this.finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            return; //we want to validate only fields loosing focus and not fields gaining focus
        }

        int id = v.getId();
        if (id == R.id.addTaskName) {
            taskNameFieldValidator.validate(taskName.getText().toString());
        }
    }
}
