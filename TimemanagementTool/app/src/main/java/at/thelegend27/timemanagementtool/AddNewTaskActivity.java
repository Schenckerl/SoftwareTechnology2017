package at.thelegend27.timemanagementtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewTaskActivity extends AppCompatActivity {
    EditText taskName, taskDescription;
    Button addButton;


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
                // add new task to firebase
            }
        });

    }
}
