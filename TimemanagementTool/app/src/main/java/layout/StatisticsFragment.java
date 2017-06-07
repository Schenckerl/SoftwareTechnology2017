package layout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.database.User;

public class StatisticsFragment extends Fragment implements TabHost.OnTabChangeListener, View.OnClickListener {
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
    private Calendar calendar;

    private ImageButton imageButtonPrevDay;
    private TextView textViewDate;
    private ImageButton imageButtonNextDay;

    private ImageButton imageButtonPrevWeek;
    private TextView textViewWeek;
    private ImageButton imageButtonNextWeek;

    private ImageButton imageButtonPrevMonth;
    private TextView textViewMonth;
    private ImageButton imageButtonNextMonth;

    private DateFormat dateFormat;

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

        imageButtonPrevDay = (ImageButton) getView().findViewById(R.id.image_button_prev_day);
        imageButtonNextDay = (ImageButton) getView().findViewById(R.id.image_button_next_day);
        textViewDate = (TextView) getView().findViewById(R.id.text_view_date);
        imageButtonPrevWeek = (ImageButton) getView().findViewById(R.id.image_button_prev_week);
        imageButtonNextWeek = (ImageButton) getView().findViewById(R.id.image_button_next_week);
        textViewWeek = (TextView) getView().findViewById(R.id.text_view_week);
        imageButtonPrevMonth = (ImageButton) getView().findViewById(R.id.image_button_prev_month);
        imageButtonNextMonth = (ImageButton) getView().findViewById(R.id.image_button_next_month);
        textViewMonth = (TextView) getView().findViewById(R.id.text_view_month);

        imageButtonPrevDay.setOnClickListener(this);
        imageButtonNextDay.setOnClickListener(this);

        imageButtonPrevWeek.setOnClickListener(this);
        imageButtonNextWeek.setOnClickListener(this);

        imageButtonPrevMonth.setOnClickListener(this);
        imageButtonNextMonth.setOnClickListener(this);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDate = dateFormat.format(calendar.getTime());

        setupDayBar(currDate);
    }

    private void setupDayBar(String date) {
        setDateOnDayTab(date);

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

        barChart.animateY(1000);
    }

    private void setupWeekBar(String fromDate, String tillDate) {
        setDateOnWeekTab(fromDate, tillDate);

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

        barChart.animateY(1000);
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
        if (textViewDate == null) {
            return;
        }

        if (tabId == "Tab One") {
            calendar = Calendar.getInstance();
            String currDate = dateFormat.format(calendar.getTime());
            setupDayBar(currDate);
        }
        else if (tabId == "Tab Two") {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            calendar.add(Calendar.DATE, 1);
            String firstDayWeek = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DATE, 4);
            String lastDayWeek = dateFormat.format(calendar.getTime());
            setupWeekBar(firstDayWeek, lastDayWeek);
        }
        else if (tabId == "Tab Three") {

        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.image_button_prev_day:
                onClickButtonPrevDay();
                break;
            case R.id.image_button_next_day:
                onClickButtonNextDay();
                break;
            case R.id.image_button_prev_week:
                onClickButtonPrevWeek();
                break;
            case R.id.image_button_next_week:
                onClickButtonNextWeek();
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

    private void onClickButtonPrevWeek() {
        calendar.add(Calendar.DAY_OF_WEEK, -11);
        String firstDayWeek = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_WEEK, 4);
        String lastDayWeek = dateFormat.format(calendar.getTime());
        setupWeekBar(firstDayWeek, lastDayWeek);
    }

    private void onClickButtonNextWeek() {
        calendar.add(Calendar.DAY_OF_WEEK, 3);
        String firstDayWeek = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_WEEK, 4);
        String lastDayWeek = dateFormat.format(calendar.getTime());
        setupWeekBar(firstDayWeek, lastDayWeek);
    }

    private void setDateOnWeekTab(String fromDate, String tillDate) {
        textViewWeek.setText(fromDate + " - " + tillDate);
    }

    private void onClickButtonPrevMonth() {
        calendar.add(Calendar.DAY_OF_WEEK, -11);
        String firstDayWeek = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_WEEK, 4);
        String lastDayWeek = dateFormat.format(calendar.getTime());
        setupWeekBar(firstDayWeek, lastDayWeek);
    }

    private void onClickButtonNextMonth() {
        calendar.add(Calendar.DAY_OF_WEEK, 3);
        String firstDayWeek = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_WEEK, 4);
        String lastDayWeek = dateFormat.format(calendar.getTime());
        setupWeekBar(firstDayWeek, lastDayWeek);
    }

    private void setDateOnMonthTab(String date) {
        textViewMonth.setText(date);
    }
}

