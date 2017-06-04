package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import at.thelegend27.timemanagementtool.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployeeOverview.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmployeeOverview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeOverview extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Employees for Company " + CurrentSession.getInstance().getCompany().name);
        return inflater.inflate(R.layout.fragment_employee_overview, container, false);
    }
}
