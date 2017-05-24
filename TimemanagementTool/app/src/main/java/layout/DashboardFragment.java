package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.Toast;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.TimemanagementActivity;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class DashboardFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_dashboard, container, false);


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Dashboard");

        final TextClock arrivedTime = (TextClock)getView().findViewById(R.id.dashboardTextClockArrived);
        final TextClock breakTime = (TextClock)getView().findViewById(R.id.dashboardTextClockBreak);
        final Button checkInButton = (Button)getView().findViewById(R.id.dashboardButtonArrive);
        Button checkOutButton = (Button)getView().findViewById(R.id.dashboardButtonLeave);
        Button breakButton = (Button)getView().findViewById(R.id.dashboardButtonBreak);

        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                Date date = new Date();
                String dateTime = dateFormat.format(date);
                arrivedTime.setText(dateTime);
                TimemanagementActivity act = (TimemanagementActivity)getActivity();//.createCheckedInFragment();
                act.createCheckedInFragment();
                return;
            }
        });
        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                Date date = new Date();
                String dateTime = dateFormat.format(date);
                breakTime.setText(dateTime);
                return;
            }
        });
        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });
    }
}
