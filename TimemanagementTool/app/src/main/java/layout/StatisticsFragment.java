package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import at.thelegend27.timemanagementtool.R;

public class StatisticsFragment extends Fragment implements TabHost.OnTabChangeListener {
    private BarChart barChart;
    private ArrayList<BarEntry> dayEntries ;
    private ArrayList<BarEntry> weekEntries ;
    private ArrayList<BarEntry> monthEntries ;
    private ArrayList<String> barDayEntryLabels ;
    private ArrayList<String> barWeekEntryLabels ;
    private ArrayList<String> barMonthEntryLabels ;
    private BarDataSet barDayDataSet ;
    private BarDataSet barWeekDataSet ;
    private BarDataSet barMonthDataSet ;
    private BarData barDayData ;
    private BarData barWeekData ;
    private BarData barMonthData ;
    private TabHost tabHost;

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

    }

    private void setupDayBar() {
        barChart = (BarChart) getActivity().findViewById(R.id.chart_day);
        dayEntries = new ArrayList<>();
        barDayEntryLabels = new ArrayList<String>();
        AddValuesToDayEntries();
        AddValuesToDayBarEntryLabels();
        barDayDataSet = new BarDataSet(dayEntries, "");
        barDayData = new BarData(barDayEntryLabels, barDayDataSet);
        barDayDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(barDayData);
        barChart.animateY(3000);
    }

    private void setupWeekBar() {
        barChart = (BarChart) getActivity().findViewById(R.id.chart_week);
        weekEntries = new ArrayList<>();
        barWeekEntryLabels = new ArrayList<String>();
        AddValuesToWeekEntries();
        AddValuesToWeekBarEntryLabels();
        barWeekDataSet = new BarDataSet(weekEntries, "");
        barWeekData = new BarData(barWeekEntryLabels, barWeekDataSet);
        barWeekDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(barWeekData);
        barChart.animateY(3000);
    }

    public void AddValuesToDayEntries(){

        for (int i = 0; i < 3; i++) {
            dayEntries.add(new BarEntry(3*(1 + i), i));
        }
    }

    public void AddValuesToWeekEntries(){

        for (int i = 0; i < 5; i++) {
            weekEntries.add(new BarEntry(i, i));
        }
    }

    public void AddValuesToDayBarEntryLabels(){

        barDayEntryLabels.add("Task 1");
        barDayEntryLabels.add("Task 2");
        barDayEntryLabels.add("Task 3");
    }

    public void AddValuesToWeekBarEntryLabels(){

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
