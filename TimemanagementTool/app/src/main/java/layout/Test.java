package layout;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.database.Company;
import at.thelegend27.timemanagementtool.database.DatabaseHelper;
import at.thelegend27.timemanagementtool.database.Department;
import at.thelegend27.timemanagementtool.database.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class Test extends Fragment {

    public Test() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Testing Fragment");
        final Button new_comp = (Button) getActivity().findViewById(R.id.newComp);
        new_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText comp_text = (EditText)getActivity().findViewById(R.id.comp_input);
                Log.d("Button", "creating new company with name " + comp_text.getText());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.setValue("Companies", null);
                DatabaseHelper.addNewCompany(new Company(comp_text.getText().toString(),88, null, null, 5));
            }
        });

        final Button new_dep = (Button)getView().findViewById(R.id.newDep) ;
        new_dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.addDepartmentToCompany(null,null);
            }
        });




    }

}
