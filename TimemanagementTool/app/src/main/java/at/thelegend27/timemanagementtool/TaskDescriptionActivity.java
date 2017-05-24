package at.thelegend27.timemanagementtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskDescriptionActivity extends AppCompatActivity {
    public static final String EXTRA_TASKNO = "taskNo";
    private List<Task> output;
    TextView author, date, taskname, description;
    private String description_sample = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque" +
            "laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto " +
            "beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit," +
            " sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem" +
            " ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et " +
            "dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit " +
            "laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse" +
            " quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur? Lorem ipsum dolor sit amet, " +
            "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis " +
            "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in " +
            "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui" +
            " officia deserunt mollit anim id est laborum.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_description);
        Bundle bundle = getIntent().getExtras();
        output = new ArrayList<>();
        output = bundle.getParcelableArrayList("output");
        int taskNo = (Integer)getIntent().getExtras().get(EXTRA_TASKNO);

        author = (TextView)findViewById(R.id.task_author);
        date = (TextView)findViewById(R.id.task_date);
        description = (TextView)findViewById(R.id.task_description);
        taskname = (TextView)findViewById(R.id.task_name);
        author.setText(output.get(taskNo).getAuthor());
        date.setText(output.get(taskNo).getDate());
        taskname.setText(output.get(taskNo).getTaskname());
        description.setText(description_sample);
        //description.setMovementMethod(new ScrollingMovementMethod());


    }
}
