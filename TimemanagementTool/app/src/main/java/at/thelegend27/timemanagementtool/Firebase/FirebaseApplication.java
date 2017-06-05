package at.thelegend27.timemanagementtool.Firebase;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.HelperClasses.UserUtils;
import at.thelegend27.timemanagementtool.LoginActivity;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.RelogActivity;
import at.thelegend27.timemanagementtool.SignUpActivity;
import at.thelegend27.timemanagementtool.TimemanagementActivity;
import at.thelegend27.timemanagementtool.database.DatabaseHelper;
import at.thelegend27.timemanagementtool.database.User;

/**
 * Created by markusfriedl on 08/05/2017.
 */

public class FirebaseApplication extends Application {

    private static final String TAG = FirebaseApplication.class.getSimpleName();
    public FirebaseAuth firebaseAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth = FirebaseAuth.getInstance();
    }

    public String getFirebaseUserAuthenticateId() {
        String userId = null;
        if (firebaseAuth.getCurrentUser() != null) {
            userId = firebaseAuth.getCurrentUser().getUid();
        }
        return userId;
    }

    public void checkUserLogin(final Context context) {
        if (firebaseAuth.getCurrentUser() != null) {
            Intent RelogIntent = new Intent(context, RelogActivity.class);
            context.startActivity(RelogIntent);
            try {
                Thread.sleep(500);
            }catch (Exception e){
                e.printStackTrace();
            }

            CurrentSession.getInstance().init(firebaseAuth.getCurrentUser().getUid(), context);
//            Intent timemanagementIntent = new Intent(context, TimemanagementActivity.class);
//            context.startActivity(timemanagementIntent);
        }
    }

    public void isUserCurrentlyLogin(final Context context) {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (null != user) {
                    Intent timemanagementIntent = new Intent(context, TimemanagementActivity.class);
                    context.startActivity(timemanagementIntent);
                } else {
                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    context.startActivity(loginIntent);
                }
            }
        };
    }

    public void createNewUser(final Context context, final String email, String password, final TextView errorMessage, final String name,
                              final String company_name) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    String uid;
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            errorMessage.setText("Failed to login. Invalid user");
                        } else {
                            Toast.makeText(context, "User has been created", Toast.LENGTH_LONG).show();
                            Intent timemanagementIntent = new Intent(context, TimemanagementActivity.class);
                            context.startActivity(timemanagementIntent);

                            User new_user = new User(null, null, 40, 0, 0, task.getResult().getUser().getUid(),name , email, company_name, false);
                            new_user.uid = task.getResult().getUser().getUid();
                            new_user.setCeo();
                            DatabaseHelper.createNewCompany(new_user, company_name);
                            DatabaseHelper.initDepartment(company_name);

                            UserUtils.createNewDbUser(new_user);
                            CurrentSession.getInstance().init(new_user.uid, context);
                        }

                        SignUpActivity.visibilityProgressbarSignUp(View.INVISIBLE);
                    }
                });
    }

    public void loginAUser(final Context context, String email, String password, final TextView errorMessage) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            errorMessage.setText("Failed to login");
                        } else {
                            Log.d("Login", "User logged in fetching information");

                            String current_user_id = task.getResult().getUser().getUid();

                            CurrentSession.getInstance().init(current_user_id, context);
                        }

                        //LoginActivity.visibilityProgressbarLogin(View.INVISIBLE);
                    }
                });
    }

    public void updateUserProfile(final Context context, FirebaseUser user, String name, final ProgressBar progressBar) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User profile updated.");
                    Toast.makeText(context, "User profile updated successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "User profile not updated. Try again!", Toast.LENGTH_LONG).show();
                }

                if (progressBar != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void changeUserPassword(final Context context, final FirebaseUser user,
                                   String oldPassword, final String password,
                                   final ProgressBar progressBar) {

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

        user.reauthenticate(credential)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                    Toast.makeText(context, "User password updated successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "Error: User password not updated. Try again!", Toast.LENGTH_LONG).show();
                                }
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    } else {
                        Log.d(TAG, "Error auth failed");
                        Toast.makeText(context, "Error: authentication failed. Old password is wrong!", Toast.LENGTH_LONG).show();

                        if (progressBar != null) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
    }

    public FirebaseUser getCurrentFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void logoutCurrentFirebaseUser() {
        FirebaseAuth.getInstance().signOut();
    }

}
