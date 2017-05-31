package at.thelegend27.timemanagementtool.HelperClasses;

import android.nfc.Tag;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import at.thelegend27.timemanagementtool.database.Company;
import at.thelegend27.timemanagementtool.database.Department;
import at.thelegend27.timemanagementtool.database.User;

public class CurrentSession {
    private User current_user;
    private Company company;
    private Department department;

    private static CurrentSession instance;

    public static CurrentSession getInstance(){
        if(instance == null){
            instance = new CurrentSession();
        }
        return instance;
    }

    public static void init(final String current_user_id){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("INIT", "initiationg singelton");
        DatabaseReference md = FirebaseDatabase.getInstance().getReference("Users/"+current_user_id);

        md.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User current_user = dataSnapshot.getValue(User.class);
                CurrentSession.getInstance().setUser(current_user);

                if(current_user.isCEO){
                    Log.d("CREATING INSTANCE", "The user to login is CEO");
                    DatabaseReference md = FirebaseDatabase.getInstance().getReference("Companies");
                    md.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.d("INIT", "checking Company");
                            Company to_check = dataSnapshot.getValue(Company.class);
                            to_check.name = dataSnapshot.getKey();

                            Log.d("INIT", "current user id:" + current_user_id);
                            if(to_check.ceo_id.equals(current_user_id)){
                                Log.d("INIT", "We got our company");
                                CurrentSession.getInstance().setCompany(to_check);
                            }
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
                }else{
                    Log.d("INIT", "we got a normal user");
                    DatabaseReference md = FirebaseDatabase.getInstance().getReference("Departments/"+
                            current_user.department);
                    md.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("INIT", "we got our department");
                            Department department = dataSnapshot.getValue(Department.class);
                            CurrentSession.getInstance().setDepartment(department);

                            DatabaseReference md = FirebaseDatabase.getInstance().getReference("Companies/"+department.company_id);
                            md.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d("INIT", "we got our company");
                                    Company company = dataSnapshot.getValue(Company.class);
                                    CurrentSession.getInstance().setCompany(company);
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
  }

    public void setUser(User user){this.current_user = user;}
    public void setCompany(Company company){this.company = company;}
    public void setDepartment(Department department){this.department = department;}

    public User getCurrent_user() {return current_user;}
    public Company getCompany() {return company;}
    public Department getDepartment() {return department;}
}
