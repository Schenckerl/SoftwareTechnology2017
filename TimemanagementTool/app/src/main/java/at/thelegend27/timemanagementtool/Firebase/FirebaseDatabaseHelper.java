package at.thelegend27.timemanagementtool.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseDatabaseHelper {
    private static final String TAG = FirebaseDatabaseHelper.class.getSimpleName();

    private Context mContext;

    public FirebaseDatabaseHelper(Context context){
        mContext = context;
    }

    public FirebaseUser getCurrentFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void updateUserProfile(FirebaseUser user, String name) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User profile updated.");
                    Toast.makeText(mContext, "User profile updated successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "User profile not updated. Try again!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}
