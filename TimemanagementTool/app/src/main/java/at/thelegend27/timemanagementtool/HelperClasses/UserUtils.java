package at.thelegend27.timemanagementtool.HelperClasses;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import at.thelegend27.timemanagementtool.database.User;

import static android.content.ContentValues.TAG;

/**
 * Created by dominik on 24.05.17.
 */

public class UserUtils {

    public static void registerUser(final User to_register, String password){
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(to_register.email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        to_register.uid = task.getResult().getUser().getUid();
                        Log.d(TAG, "ui: " + to_register.uid);
                        createNewDbUser(to_register);
                    }
                });
    }

    private static void createNewDbUser(User to_create){
        Log.d(TAG, "creating new Db user");
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Users").child(to_create.uid).setValue(to_create.toMap());
    }
}
