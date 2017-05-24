package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import at.thelegend27.timemanagementtool.Firebase.FirebaseDatabaseHelper;
import at.thelegend27.timemanagementtool.R;

public class EditProfileFragment extends Fragment {
    private EditText editProfileName;
    private EditText editProfilePassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Edit Profile");

        editProfileName = (EditText)getView().findViewById(R.id.profile_name);
        editProfilePassword = (EditText)getView().findViewById(R.id.profile_password);

        Button saveEditButton = (Button)getView().findViewById(R.id.save_edit_button);

        final FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper(getActivity());
        final FirebaseUser user = firebaseDatabaseHelper.getCurrentFirebaseUser();

        if (user != null) {
            editProfileName.setText(user.getDisplayName());
        }

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profileName = editProfileName.getText().toString();
                String profilePassword = editProfilePassword.getText().toString();

                if (TextUtils.isEmpty(profileName)) {
                    Toast.makeText(getActivity(), "Name required", Toast.LENGTH_LONG).show();
                    return;
                }

                if (user != null) {
                    String userId = user.getProviderId();
                    String id = user.getUid();
                    String profileEmail = user.getEmail();

                    firebaseDatabaseHelper.updateUserProfile(user, profileName);

                }
            }
        });
    }
}
