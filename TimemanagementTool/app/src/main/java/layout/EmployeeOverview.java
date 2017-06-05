package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.TimemanagementActivity;
import at.thelegend27.timemanagementtool.database.User;

public class EmployeeOverview extends Fragment {
    private TimemanagementActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_overview, container, false);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(CurrentSession.getInstance().getCompany().name);
        activity = (TimemanagementActivity)getActivity();
        final ArrayList<Map<String, String>> items = new ArrayList<>();

        if(CurrentSession.getInstance().getCurrent_user().isSupervisor) {
            Log.d("OVERVIEW", "we got a supervisor");
            DatabaseReference md = FirebaseDatabase.getInstance().getReference("Users");

            md.orderByChild("department").equalTo(CurrentSession.getInstance().getDepartment().name)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.d("OVERVIEW", "we found a user");
                            Map<String, String> current = new HashMap<>();
                            current.put("line1", dataSnapshot.getValue(User.class).fullName);
                            if(dataSnapshot.getValue(User.class).department != null) {
                                current.put("line2", dataSnapshot.getValue(User.class).department);
                            }else{
                                current.put("line2", "Ceo of "+ dataSnapshot.getValue(User.class).company);
                            }
                            items.add(current);
                            SimpleAdapter adapter = new SimpleAdapter(activity, items,
                                    R.layout.user_info_list_item, new String[]{"line1", "line2"},
                                    new int[]{R.id.user_info_line1, R.id.user_info_line2});
                            ((ListView)view.findViewById(R.id.employee_list)).setAdapter(adapter);
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
        else if(CurrentSession.getInstance().getCurrent_user().isCEO){
            DatabaseReference md = FirebaseDatabase.getInstance().getReference("Users");

            md.orderByChild("company").equalTo(CurrentSession.getInstance().getCompany().name)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.d("OVERVIEW", "we found a user");
                            Map<String, String> current = new HashMap<String, String>();
                            current.put("line1", dataSnapshot.getValue(User.class).fullName);
                            current.put("line2", dataSnapshot.getValue(User.class).department);
                            items.add(current);
                            SimpleAdapter adapter = new SimpleAdapter(activity, items,
                                    R.layout.user_info_list_item,new String[]{"line1", "line2"},
                                    new int[]{R.id.user_info_line1, R.id.user_info_line2});
/*                            user_list.add(dataSnapshot.getValue(User.class).fullName);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                    android.R.layout.simple_list_item_1,user_list);*/
                            ((ListView)view.findViewById(R.id.employee_list)).setAdapter(adapter);
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
}
