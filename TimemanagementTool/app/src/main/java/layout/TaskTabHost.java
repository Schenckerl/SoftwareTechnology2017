package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.R;

/**
 * Created by Dominik on 06.06.2017.
 */

public class TaskTabHost extends Fragment {
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.tab_host);

        mTabHost.addTab(mTabHost.newTabSpec("mine").setIndicator("Mine"),
                TasksFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("open").setIndicator("All Open"),
                OpenTasksFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("closed").setIndicator("All Closed"),
                ClosedTasksFragment.class, null);

        return mTabHost;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
    }
}
