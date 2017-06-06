package layout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.database.User;

public class StatisticsFragment extends Fragment implements TabHost.OnTabChangeListener {
    private BarChart barChart;
    private float[] dayEntriesFloatArray ;
    private float[][] weekEntriesFloatArray ;
    private ArrayList<BarEntry> dayEntries ;
    private ArrayList<BarEntry> dayEntriesSetpoint ;
    private ArrayList<BarEntry> weekEntries ;
    private ArrayList<BarEntry> weekEntriesSetpoint ;
    private ArrayList<BarEntry> monthEntries ;
    private ArrayList<String> barDayEntryLabels ;
    private ArrayList<String> barDayEntryLabelsSetpoint ;
    private ArrayList<String> barWeekEntryLabels ;
    private ArrayList<String> barWeekEntryLabelsSetpoint ;
    private ArrayList<String> barMonthEntryLabels ;
    private BarDataSet barDayDataSet ;
    private BarDataSet barDayDataSetSetpoint ;
    private BarDataSet barWeekDataSet ;
    private BarDataSet barWeekDataSetSetpoint ;
    private BarDataSet barMonthDataSet ;
    private BarData barDayData ;
    private BarData barWeekData ;
    private BarData barMonthData ;
    private TabHost tabHost;
    private User currentUser;

    private float groupSpace = 0.06f;
    private float barSpace = 0.02f; // x2 dataset
    private float barWidth = 0.25f; // x2 dataset


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Statistics");

        tabHost = (TabHost) view.findViewById(R.id.statistics_tabHost);
        tabHost.setup();
        tabHost.setOnTabChangedListener(this);

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab One");
        spec.setContent(R.id.tab_day);
        spec.setIndicator("Day");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Tab Two");
        spec.setContent(R.id.tab_week);
        spec.setIndicator("Week");
        tabHost.addTab(spec);

        //Tab 3
        spec = tabHost.newTabSpec("Tab Three");
        spec.setContent(R.id.tab_month);
        spec.setIndicator("Month");
        tabHost.addTab(spec);

        currentUser = CurrentSession.getInstance().getCurrent_user();

        if (currentUser == null) {
            Log.d("STATISTICS", "no currentuser available");
        }
    }

    private void setupDayBar() {
        barChart = (BarChart) getActivity().findViewById(R.id.chart_day);
        dayEntries = new ArrayList<>();
        dayEntriesFloatArray = new float[3];
        dayEntriesSetpoint = new ArrayList<>();
        barDayEntryLabels = new ArrayList<>();

        AddValuesToDayEntries();
        AddValuesToDayBarEntryLabels();
        dayEntries.add(new BarEntry(0, dayEntriesFloatArray));

        barDayDataSet = new BarDataSet(dayEntries, "Actual value");
        barDayDataSetSetpoint = new BarDataSet(dayEntriesSetpoint, "Setpoint");

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

        barChart.animateY(2000);
    }

    private void setupWeekBar() {
        barChart = (BarChart) getActivity().findViewById(R.id.chart_week);
        weekEntries = new ArrayList<>();
        weekEntriesFloatArray = new float[5][3];
        weekEntriesSetpoint = new ArrayList<>();
        barWeekEntryLabels = new ArrayList<>();

        AddValuesToWeekEntries();
        AddValuesToWeekBarEntryLabels();

        barWeekDataSet = new BarDataSet(weekEntries, "Actual value");
        barWeekDataSetSetpoint = new BarDataSet(weekEntriesSetpoint, "Setpoint");

        barWeekData = new BarData(barWeekDataSet, barWeekDataSetSetpoint);
        barWeekData.setBarWidth(barWidth);

        barWeekDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barWeekDataSetSetpoint.setColor(Color.GRAY);

        barDayData.setBarWidth(barWidth);

        barChart.setData(barWeekData);
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawLabels(false);
        barChart.setFitBars(true);
        barChart.getXAxis().removeAllLimitLines();
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setAxisMinimum(0f);
        barChart.getXAxis().setAxisMaximum(3f);

        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.setTouchEnabled(false);

        barChart.invalidate();

        barChart.animateY(2000);
    }

    private int getDayTargetHours() {
        if (currentUser == null) {
            Log.d("STATISTICS", "Current user is null");
            return 8;
        }
        return (currentUser.targetHours / 5);
    }

    private void AddValuesToDayEntries(){

        for (int i = 0; i < 3; i++) { // TODO: tasks for a day
            dayEntriesFloatArray[i] = (float) 3*(1 + i);
        }

        dayEntriesSetpoint.add(new BarEntry(0, getDayTargetHours()));
    }

    private void AddValuesToWeekEntries(){

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) { // TODO: tasks for a day
                weekEntriesFloatArray[i][j] = (float) i * (1 + j);
            }
            weekEntries.add(new BarEntry(i, weekEntriesFloatArray[i]));

            weekEntriesSetpoint.add(new BarEntry(i, getDayTargetHours()));
        }
    }

    private void AddValuesToDayBarEntryLabels(){

        barDayEntryLabels.add("Task 1");
        barDayEntryLabels.add("Task 2");
        barDayEntryLabels.add("Task 3");
    }

    private void AddValuesToWeekBarEntryLabels(){

        barWeekEntryLabels.add("Monday");
        barWeekEntryLabels.add("Tuesday");
        barWeekEntryLabels.add("Wednesday");
        barWeekEntryLabels.add("Thursday");
        barWeekEntryLabels.add("Friday");
    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId == "Tab One") {
            setupDayBar();
        }
        else if (tabId == "Tab Two") {
            setupWeekBar();
        }
        else if (tabId == "Tab Three") {

        }
    }
}

