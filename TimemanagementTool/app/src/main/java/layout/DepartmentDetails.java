package layout;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.TimemanagementActivity;
import at.thelegend27.timemanagementtool.database.Department;
import at.thelegend27.timemanagementtool.database.User;

public class DepartmentDetails extends Fragment {

    public String dep;
    private TimemanagementActivity activity;

    public DepartmentDetails() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.department_detail, container, false);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        activity = (TimemanagementActivity) getActivity();
        activity.setTitle("Details");
        ((TextView)view.findViewById(R.id.department_info_header)).setText(dep);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Departments/"+ dep);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/"+ dataSnapshot.getValue(Department.class).supervisor);
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ((TextView) view.findViewById(R.id.supervisor_name)).setText(dataSnapshot.getValue(User.class).fullName);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final ArrayList<Map<String, String>> items = new ArrayList<>();

        DatabaseReference md = FirebaseDatabase.getInstance().getReference("Users");
        md.orderByChild("department").equalTo(dep).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> current = new HashMap<>();
                current.put("line1", dataSnapshot.getValue(User.class).fullName);
                current.put("line2", dataSnapshot.getValue(User.class).department);
                items.add(current);
                SimpleAdapter adapter = new SimpleAdapter(activity, items,
                        R.layout.user_info_list_item, new String[]{"line1", "line2"},
                        new int[]{R.id.user_info_line1, R.id.user_info_line2});
                ((ListView)view.findViewById(R.id.users_for_department)).setAdapter(adapter);
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
    }
}
