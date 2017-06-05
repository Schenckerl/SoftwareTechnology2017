package at.thelegend27.timemanagementtool.HelperClasses;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.util.Log;
import android.widget.Toast;

import at.thelegend27.timemanagementtool.Firebase.FirebaseApplication;
import at.thelegend27.timemanagementtool.LoginActivity;
import at.thelegend27.timemanagementtool.TimemanagementActivity;

/**
 * Created by dominik on 04.06.17.
 */

public class checkerThread extends Thread{


    public static boolean cancle;
    private Context context;

    public checkerThread(Context context){
        this.context = context;
    }

    @Override
    public void run() {
        Log.d("WORKER", "started");
        while(!cancle) {
            if(CurrentSession.getInstance().getCompany() != null){
                Log.d("WORKER", "loading is complete");
                CurrentSession.getInstance().loaded = true;
                LoginActivity.completeLogin(context);
                break;
            }
        }
    }
}
