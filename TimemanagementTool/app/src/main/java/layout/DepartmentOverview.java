package layout;

import android.app.LauncherActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.TimemanagementActivity;

/**
 * Created by Dominik on 05.06.2017.
 */

public class DepartmentOverview extends Fragment {

    private TimemanagementActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.department_overview, container, false);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        activity = (TimemanagementActivity)getActivity();
        final ArrayList<String> list = new ArrayList<>();

        DatabaseReference md  = FirebaseDatabase.getInstance().getReference("Departments");

        md.orderByChild("company").equalTo(CurrentSession.getInstance().getCompany().name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.add(dataSnapshot.getKey());
                ArrayAdapter<String> adapter  = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, list);
                ((ListView)view.findViewById(R.id.department_list)).setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final ListView department_list = (ListView)view.findViewById(R.id.department_list);

        department_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEPARTMENT Overview", "item has been selected " + department_list.getItemAtPosition(position).toString());
                activity.showDepDetail(department_list.getItemAtPosition(position).toString());
            }
        });
    }
}
