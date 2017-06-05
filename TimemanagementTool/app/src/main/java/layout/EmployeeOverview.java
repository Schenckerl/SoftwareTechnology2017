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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.database.User;

public class EmployeeOverview extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_overview, container, false);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Employees for Company " + CurrentSession.getInstance().getCompany().name);
        final ArrayList<String> user_list = new ArrayList<>();

        if(CurrentSession.getInstance().getCurrent_user().isSupervisor) {
            Log.d("OVERVIEW", "we goa a supervisor");
            DatabaseReference md = FirebaseDatabase.getInstance().getReference("Users");

            md.orderByChild("department").equalTo(CurrentSession.getInstance().getDepartment().name)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
                            user_list.add(dataSnapshot.getValue(User.class).fullName);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                    android.R.layout.simple_list_item_1,user_list);
                            ((ListView)getActivity().findViewById(R.id.employee_list)).setAdapter(adapter);
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
