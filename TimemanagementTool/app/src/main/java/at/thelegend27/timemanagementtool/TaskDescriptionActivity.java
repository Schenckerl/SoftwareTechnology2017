package at.thelegend27.timemanagementtool;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class TaskDescriptionActivity extends AppCompatActivity {
    public static final String EXTRA_TASKNO = "taskNo";
    private List<Task> output;
    TextView author, date, taskname, description;
    private int position;
    AlertDialog.Builder ad;
    boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_description);

        Bundle bundle = getIntent().getExtras();
        output = new ArrayList<>();
        output = bundle.getParcelableArrayList("output");

        int taskNo = (Integer)getIntent().getExtras().get(EXTRA_TASKNO);
        position = taskNo;
        author = (TextView)findViewById(R.id.task_author);
        date = (TextView)findViewById(R.id.task_date);
        description = (TextView)findViewById(R.id.task_description);
        taskname = (TextView)findViewById(R.id.task_name);
        author.setText(output.get(taskNo).getAuthor_id());
        date.setText(output.get(taskNo).getDate());
        taskname.setText(output.get(taskNo).getTask_name());
        description.setText(output.get(taskNo).getTask_description());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.task_done: {
                ad = new AlertDialog.Builder(this);
                ad.setTitle("Confirmation");
                ad.setMessage("Confirm, that task is done");
                ad.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
                        reference.child(output.get(position).getTask_id()).removeValue();
                        finish();
                        isFinish = true;
                    }
                });
                ad.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                ad.show();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
