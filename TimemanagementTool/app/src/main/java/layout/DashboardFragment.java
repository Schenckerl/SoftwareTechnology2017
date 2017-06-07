package layout;

import android.os.Bundle;
import android.os.health.TimerStat;
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
import at.thelegend27.timemanagementtool.TimemanagementActivity;


public class DashboardFragment extends Fragment {
    private static TimemanagementActivity activity;
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
        activity = (TimemanagementActivity)getActivity();
        //you can set the title for your toolbar here for different fragments different titles

        final Button arrive = (Button)view.findViewById(R.id.dashboardButtonArrive);
        final Button leave = (Button)view.findViewById(R.id.dashboardButtonLeave);
        final Button break_button = (Button)view.findViewById(R.id.dashboardButtonBreak);

        if(CurrentSession.getInstance().getWorkingDay()!= null){
            Log.d("TIMETRACKING", "we want to continue a working day");
            if(CurrentSession.getInstance().getWorkingDay().end != null){
                Log.d("TIMETRAKCING", "we are already finished for today");
                showFinishedInfo();
            }else {
                ReinitiateWorkingDay();
            }
        }
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
                break_button.setVisibility(View.VISIBLE);
                leave.setVisibility(View.VISIBLE);
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TIMETRACKING", "leave pressed");
                CurrentSession.getInstance().endWorkingDay();
                long minutes_worked = CurrentSession.getInstance().getWorkingDay().duration;
                String worked = String.format("%02d",minutes_worked/60) + ":" + String.format("%02d", minutes_worked%60);
                String break_time = CurrentSession.getInstance().getWorkingDay().break_time + " minutes";
                leave.setVisibility(View.GONE);
                break_button.setVisibility(View.GONE);
                (getActivity().findViewById(R.id.dashboardButtonBreak)).setVisibility(View.GONE);
                ((TextView)getActivity().findViewById(R.id.dashboardTextDay))
                        .setText(worked);
                ((TextView)getActivity().findViewById(R.id.dashboardTextBreak))
                        .setText(break_time);
                getActivity().findViewById(R.id.workingDaySummary).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.finished_description).setVisibility(View.VISIBLE);
            }
        });

        break_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TIMETRACKING", "break pressed");
            }
        });
        getActivity().setTitle("Dashboard");
    }

    private void ReinitiateWorkingDay(){
        getActivity().findViewById(R.id.dashboardButtonArrive).setVisibility(View.GONE);
        getActivity().findViewById(R.id.init_arrival).setVisibility(View.GONE);
        getActivity().findViewById(R.id.dashboardButtonBreak).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.dashboardButtonLeave).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.arrival).setVisibility(View.VISIBLE);

        LocalTime begin = CurrentSession.getInstance().getWorkingDay().getBegin();
        String arrival = String.format("%02d", begin.getHourOfDay()) + ":" + String.format("%02d", begin.getMinuteOfHour());
        ((TextView)activity.findViewById(R.id.arrival)).setText(arrival);
    }

    private void showFinishedInfo(){
        getActivity().findViewById(R.id.init_arrival).setVisibility(View.GONE);
        getActivity().findViewById(R.id.dashboardButtonArrive).setVisibility(View.GONE);
        getActivity().findViewById(R.id.dashboardButtonLeave).setVisibility(View.GONE);
        getActivity().findViewById(R.id.dashboardButtonBreak).setVisibility(View.GONE);
        getActivity().findViewById(R.id.workingDaySummary).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.finished_description).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.arrival).setVisibility(View.VISIBLE);

        long minutes_worked = CurrentSession.getInstance().getWorkingDay().duration;
        String worked = String.format("%02d",minutes_worked/60) + ":" + String.format("%02d", minutes_worked%60);
        String break_time = CurrentSession.getInstance().getWorkingDay().break_time + " minutes";

        LocalTime begin = CurrentSession.getInstance().getWorkingDay().getBegin();
        String arrival = String.format("%02d", begin.getHourOfDay()) + ":" + String.format("%02d", begin.getMinuteOfHour());
        ((TextView)activity.findViewById(R.id.arrival)).setText(arrival);

        ((TextView)getActivity().findViewById(R.id.dashboardTextDay))
                .setText(worked);
        ((TextView)getActivity().findViewById(R.id.dashboardTextBreak))
                .setText(break_time);
    }
}
