package at.thelegend27.timemanagementtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.database.User;

public class AddNewTaskActivity extends AppCompatActivity {
    EditText taskName, taskDescription;
    Button addButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        taskDescription = (EditText)findViewById(R.id.addTaskDescription);
        taskName = (EditText)findViewById(R.id.addTaskName);
        addButton = (Button)findViewById(R.id.addTaskButton);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Tasks");
                setTask();

            }
        });




    }

    private void setTask() {
        final User user = CurrentSession.getInstance().getCurrent_user();
        String date = new DateTime().toString("yyyy-MM-dd");
        String id = reference.push().getKey();
        Task task = new Task(user.fullName, date, taskName.getText().toString(), taskDescription.getText().toString(), user.uid, id);
        Map<String, Object> taskValues = task.toMap();
        Map<String, Object> newTask = new HashMap<>();
        newTask.put(id, newTask);

        reference.updateChildren(newTask);

    }

}
