package layout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


import at.thelegend27.timemanagementtool.AddNewTaskActivity;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.Task;
import at.thelegend27.timemanagementtool.TaskAdaptor;
import at.thelegend27.timemanagementtool.TaskDescriptionActivity;

public class TasksFragment extends Fragment {
    @Nullable
    RecyclerView tasks_list;
    private List<Task> output;
    private TaskAdaptor adaptor;
   // ArrayList<String> tasksList = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        output = new ArrayList<>();



        tasks_list = (RecyclerView) view.findViewById(R.id.tasks_list);
        tasks_list.setHasFixedSize(true);
        //change R.layout.yourlayoutfilename for each of your fragments
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.add_new_task, menu);
        return;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        tasks_list.setLayoutManager(llm);
        fillTasks();


        adaptor = new TaskAdaptor(output);
        tasks_list.setAdapter(adaptor);

        adaptor.setListener(new TaskAdaptor.Listener() {
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), TaskDescriptionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("output", (ArrayList<? extends Parcelable>) output);
                intent.putExtras(bundle);
                intent.putExtra(TaskDescriptionActivity.EXTRA_TASKNO, position);
                getActivity().startActivity(intent);
            }
        });

        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Tasks");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0 :
                removeTask(item.getGroupId());
                break;
            case 1:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void fillTasks() { // must to be replaced with firebase

        for(int i = 0; i < 10; i++) {
            output.add(new Task("Author " + i, "Date "+ i, "Task name " + i, i));
        }
    }

    private int getTaskIndex(Task task) {
        int index = -1;
        for(int i = 0; i < output.size(); i++) {
            if(output.get(i).getId() == task.getId()) {
                index = i;
                break;
            }
        }
        return index;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_task:
                Intent intent = new Intent(getActivity(), AddNewTaskActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void removeTask(int position) {
        Toast toast = Toast.makeText(getContext(), "Task \"" + output.get(position).getTaskname() + "\" done!", Toast.LENGTH_SHORT);
        toast.show();
        adaptor.removeAt(position);

    }


}
