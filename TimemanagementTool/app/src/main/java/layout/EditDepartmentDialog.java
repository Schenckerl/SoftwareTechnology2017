package layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.TimemanagementActivity;
import at.thelegend27.timemanagementtool.database.Department;
import at.thelegend27.timemanagementtool.database.User;

public class EditDepartmentDialog extends DialogFragment{
    private TimemanagementActivity activity;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = (TimemanagementActivity)getActivity();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.edit_department, null);

        final ArrayList<String> possible_users = new ArrayList<>();

        final Spinner spinner = (Spinner)(view.findViewById(R.id.new_supervisor));

        DatabaseReference md = FirebaseDatabase.getInstance().getReference("Users");
        md.orderByChild("company").equalTo(CurrentSession.getInstance().getCompany().name)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("User", "found user");
                        if(!dataSnapshot.getValue(User.class).isSupervisor) {
                            possible_users.add(dataSnapshot.getValue(User.class).fullName);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity,android.R.layout.simple_spinner_item, possible_users);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(adapter);
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

        builder.setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("EDIT", "confirm clicked");
                final String username = spinner.getSelectedItem().toString();
                Log.d("EDIT", "new supervisor: "+ username);
                final DatabaseReference md = FirebaseDatabase.getInstance().getReference("Users");
                md.orderByChild("fullName").equalTo(username).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(final DataSnapshot userSnapshot, String s) {
                        User new_supervisor = userSnapshot.getValue(User.class);
                        new_supervisor.isSupervisor = true;
                        String department = ((TextView)activity.findViewById(R.id.department_info_header)).getText().toString();
                        new_supervisor.department = department;
                        md.child(userSnapshot.getKey()).setValue(new_supervisor);

                        Log.d("Edit", "department is " + department);
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Departments/"+ department);
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Department dep = dataSnapshot.getValue(Department.class);
                                final String old_supervisor = dep.supervisor;
                                dep.supervisor = userSnapshot.getKey();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Departments");
                                ref.child(dataSnapshot.getKey()).setValue(dep);

                                DatabaseReference old = FirebaseDatabase.getInstance().getReference("Users/"+old_supervisor);
                                old.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User old = dataSnapshot.getValue(User.class);
                                        old.isSupervisor = false;
                                        md.child(old_supervisor).setValue(old);
                                        ((TextView)activity.findViewById(R.id.supervisor_name)).setText(username);
                                        Toast.makeText(activity,"Department has been changed", Toast.LENGTH_LONG).show();
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
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditDepartmentDialog.this.getDialog().cancel();
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
