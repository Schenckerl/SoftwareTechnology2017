package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.thelegend27.timemanagementtool.R;

/**
 * Created by markusfriedl on 07/06/2017.
 */

public class StatisticsTabHost extends Fragment {
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.tab_host);

        mTabHost.addTab(mTabHost.newTabSpec("day").setIndicator("Day"),
                StatisticsFragmentDay.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("week").setIndicator("Week"),
                StatisticsFragmentWeek.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("month").setIndicator("Month"),
                StatisticsFragmentMonth.class, null);

        return mTabHost;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Statistics");
    }
}
