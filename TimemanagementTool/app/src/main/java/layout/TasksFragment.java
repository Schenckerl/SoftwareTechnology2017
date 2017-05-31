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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


import at.thelegend27.timemanagementtool.AddNewTaskActivity;
import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.Task;
import at.thelegend27.timemanagementtool.TaskAdapter;
import at.thelegend27.timemanagementtool.TaskDescriptionActivity;
import at.thelegend27.timemanagementtool.database.User;

public class TasksFragment extends Fragment {
    @Nullable
    RecyclerView tasks_list;
    private List<Task> output;
    private TaskAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
   // ArrayList<String> tasksList = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        User user = CurrentSession.getInstance().getCurrent_user();
        setHasOptionsMenu(true);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Tasks");
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        output = new ArrayList<>();
        tasks_list = (RecyclerView) view.findViewById(R.id.tasks_list);
        tasks_list.setHasFixedSize(true);
        updateList();
        return view;
    }

    private void updateList() {
        final User user = CurrentSession.getInstance().getCurrent_user();
        reference.orderByChild("user_id").equalTo(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                output.add(dataSnapshot.getValue(Task.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Task task = dataSnapshot.getValue(Task.class);
                int index = getTaskIndex(task);

                output.set(index, task);

                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Task task = dataSnapshot.getValue(Task.class);
                int index = getTaskIndex(task);
                output.remove(index);
                adapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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



        adapter = new TaskAdapter(output);
        tasks_list.setAdapter(adapter);

        adapter.setListener(new TaskAdapter.Listener() {
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

    private int getTaskIndex(Task task) {
        int index = -1;
        for(int i = 0; i < output.size(); i++) {
            if(output.get(i).getTask_id() == task.getTask_id()) {
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
        Toast toast = Toast.makeText(getContext(), "Task \"" + output.get(position).getTask_name() + "\" done!", Toast.LENGTH_SHORT);
        toast.show();
        reference.child(output.get(position).getTask_id()).removeValue();

    }


}
