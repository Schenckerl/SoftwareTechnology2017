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

public class EmployeeTabHost extends Fragment {
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.tab_host);

        mTabHost.addTab(mTabHost.newTabSpec("employees").setIndicator("Employees"),
               EmployeeOverview.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("supervisors").setIndicator("Supervisors"),
                SupervisorOverview.class, null);

        return mTabHost;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Employees for " + CurrentSession.getInstance().getCompany().name);
    }
}
