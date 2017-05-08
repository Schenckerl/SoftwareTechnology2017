package at.thelegend27.timemanagementtool.Firebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDatabaseHelper {

    private static final String TAG = FirebaseDatabaseHelper.class.getSimpleName();

    private DatabaseReference databaseReference;

    public FirebaseDatabaseHelper(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void createUserInFirebaseDatabase(String userId, FirebaseUserEntity firebaseUserEntity){
        Map<String, FirebaseUserEntity> user = new HashMap<String, FirebaseUserEntity>();
        user.put(userId, firebaseUserEntity);
        databaseReference.child("users").setValue(user);
    }

    public void isUserKeyExist(final String uid, final Context context, final RecyclerView recyclerView){
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
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
}
