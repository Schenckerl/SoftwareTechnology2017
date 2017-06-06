package at.thelegend27.timemanagementtool.HelperClasses;
import android.content.Context;
import android.util.Log;
import at.thelegend27.timemanagementtool.LoginActivity;
import layout.DashboardFragment;

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
