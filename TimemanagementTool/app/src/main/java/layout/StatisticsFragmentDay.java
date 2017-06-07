package layout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.TimemanagementActivity;

public class StatisticsFragmentDay extends Fragment implements View.OnClickListener {
    public String userId;

    private TimemanagementActivity activity;

    private BarChart barChart;
    private List<Float> entriesFloatArray = new ArrayList<>();
    private ArrayList<BarEntry> entries ;
    private ArrayList<BarEntry> entriesSetpoint ;
    private ArrayList<String> barEntryLabels ;
    private ArrayList<String> barEntryLabelsSetpoint ;
    private BarDataSet barDayDataSet ;
    private BarDataSet barDayDataSetSetpoint ;
    private BarData barDayData ;
    private Calendar calendar;

    private ImageButton imageButtonPrevDay;
    private TextView textViewDate;
    private ImageButton imageButtonNextDay;

    private DateFormat dateFormat;

    private float groupSpace = 0.06f;
    private float barSpace = 0.02f; // x2 dataset
    private float barWidth = 0.25f; // x2 dataset

    private final ArrayList<Pair<LocalDate, Long>> durations = new ArrayList<>();
    private int numberOfTask = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        activity = (TimemanagementActivity) getActivity();
        imageButtonPrevDay = (ImageButton) getView().findViewById(R.id.image_button_prev);
        imageButtonNextDay = (ImageButton) getView().findViewById(R.id.image_button_next);
        textViewDate = (TextView) getView().findViewById(R.id.text_view_date);

        imageButtonPrevDay.setOnClickListener(this);
        imageButtonNextDay.setOnClickListener(this);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String currDate = dateFormat.format(calendar.getTime());

        durations.clear();
        entriesFloatArray.clear();

        if (userId == null) {
            userId = CurrentSession.getInstance().getCurrent_user().getUid();
        }

        DatabaseReference md = FirebaseDatabase.getInstance().getReference().child("WorkingDays");
        md.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currentDay: dataSnapshot.getChildren()) {
                    Map<String, String> value = (Map<String, String>)currentDay.getValue();
                    if (value.get("user").equals(userId)) {
                        durations.add(new Pair<>(new LocalDate(value.get("day")), Long.parseLong(value.get("duration"))));
                    }

                }

                setupDayBar(currDate);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupDayBar(String date) {
        setDateOnDayTab(date);

        barChart = (BarChart) activity.findViewById(R.id.chart_stat);
        entries = new ArrayList<>();

        entriesSetpoint = new ArrayList<>();
        barEntryLabels = new ArrayList<>();

        AddValuesToEntries(date);

        float tmp_day_entries[];
        tmp_day_entries = new float[entriesFloatArray.size()];
        int i = 0;
        for (float entry: entriesFloatArray) {
            tmp_day_entries[i++] = entry;
        }

        entries.add(new BarEntry(0, tmp_day_entries));

        barDayDataSet = new BarDataSet(entries, "Actual value");
        barDayDataSetSetpoint = new BarDataSet(entriesSetpoint, "Setpoint");

        barDayData = new BarData(barDayDataSet, barDayDataSetSetpoint);

        barDayDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDayDataSetSetpoint.setColor(Color.GRAY);

        barDayData.setBarWidth(barWidth);

        barChart.setData(barDayData);
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawLabels(false);
        barChart.setFitBars(true);
        barChart.getXAxis().removeAllLimitLines();
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setAxisMinimum(-0.1f);
        barChart.getXAxis().setAxisMaximum(0.67f);

        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.setTouchEnabled(false);

        barChart.invalidate();

        barChart.animateY(1000);
    }

    private int getDayTargetHours() {
        if (CurrentSession.getInstance().getCurrent_user() == null) {
            return 8;
        }
        return (CurrentSession.getInstance().getCurrent_user().targetHours / 5);
    }

    private void AddValuesToEntries(String date){

        numberOfTask = 0;
        LocalDate localDate = new LocalDate(date);
        entriesFloatArray.clear();

        for (Pair<LocalDate, Long> duration: durations) { // TODO: tasks for a day
            if (duration.first.equals(localDate)) {
                entriesFloatArray.add((float) (duration.second / 60.));
            }
        }

        entriesSetpoint.add(new BarEntry(0, getDayTargetHours()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.image_button_prev:
                onClickButtonPrevDay();
                break;
            case R.id.image_button_next
                    :
                onClickButtonNextDay();
                break;
            default:
                break;
        }
    }

    private void onClickButtonPrevDay() {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -3);
        } else {
            calendar.add(Calendar.DATE, -1);
        }
        String currDate = dateFormat.format(calendar.getTime());
        setupDayBar(currDate);
    }

    private void onClickButtonNextDay() {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            calendar.add(Calendar.DATE, 3);
        } else {
            calendar.add(Calendar.DATE, 1);
        }
        String currDate = dateFormat.format(calendar.getTime());
        setupDayBar(currDate);
    }

    private void setDateOnDayTab(String date) {
        textViewDate.setText(date);
    }
}

