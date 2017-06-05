package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.LocalTime;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.R;


public class DashboardFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

        final Button arrive = (Button)view.findViewById(R.id.dashboardButtonArrive);

        arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TIMETRACKING", "arrive pressed");
                arrive.setVisibility(View.GONE);
                CurrentSession.getInstance().startWorkingDay();
                view.findViewById(R.id.init_arrival).setVisibility(View.GONE);
                LocalTime begin = CurrentSession.getInstance().getWorkingDay().getBegin();
                String arrival = String.format("%02d", begin.getHourOfDay()) + ":" + String.format("%02d", begin.getMinuteOfHour());
                ((TextView)view.findViewById(R.id.arrival)).setText(arrival);
                view.findViewById(R.id.arrival).setVisibility(View.VISIBLE);
            }
        });

        final Button leave = (Button)view.findViewById(R.id.dashboardButtonLeave);

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TIMETRACKING", "leave pressed");
                CurrentSession.getInstance().endWorkingDay();
            }
        });
        getActivity().setTitle("Dashboard");
    }
}
