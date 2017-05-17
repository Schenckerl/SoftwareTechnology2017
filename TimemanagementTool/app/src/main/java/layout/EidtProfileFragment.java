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
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import at.thelegend27.timemanagementtool.Firebase.FirebaseApplication;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.TimemanagementActivity;

public class EidtProfileFragment extends Fragment {
    private EditText editProfileName;
    private EditText editProfileOldPassword;
    private EditText editProfilePassword;
    private EditText editProfileRepeatPassword;
    private FrameLayout loadingFrameLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Edit Profile");

        editProfileName = (EditText)getView().findViewById(R.id.profile_name);
        editProfileOldPassword = (EditText)getView().findViewById(R.id.profile_old_password);
        editProfilePassword = (EditText)getView().findViewById(R.id.profile_password);
        editProfileRepeatPassword = (EditText)getView().findViewById(R.id.profile_repeat_password);
        loadingFrameLayout = (FrameLayout)getView().findViewById(R.id.edit_profile_laoding_framelayout);

        Button saveEditButton = (Button)getView().findViewById(R.id.save_edit_button);
        final Button savePasswordButton = (Button)getView().findViewById(R.id.save_password_button);

        final FirebaseApplication firebaseApplication = new FirebaseApplication();
        final FirebaseUser user = firebaseApplication.getCurrentFirebaseUser();

        if (user != null) {
            editProfileName.setText(user.getDisplayName());
        }

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserProfile(user, firebaseApplication);
            }
        });

        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserPassword(user, firebaseApplication);
            }
        });
    }

    private void saveUserProfile(FirebaseUser user, FirebaseApplication firebaseApplication) {
        String profileName = editProfileName.getText().toString();

        TimemanagementActivity.hideKeyboard(getActivity());

        if (TextUtils.isEmpty(profileName)) {
            Toast.makeText(getActivity(), "Name required", Toast.LENGTH_LONG).show();
            return;
        }

        loadingFrameLayout.setVisibility(View.VISIBLE);

        if (user != null) {
            String userId = user.getProviderId();
            String id = user.getUid();
            String profileEmail = user.getEmail();

            firebaseApplication.updateUserProfile(getActivity(), user, profileName, loadingFrameLayout);
        }
    }

    private void saveUserPassword(FirebaseUser user, FirebaseApplication firebaseApplication) {
        String profileOldPassword = editProfileOldPassword.getText().toString();
        String profilePassword = editProfilePassword.getText().toString();
        String profileRepeatPassword = editProfileRepeatPassword.getText().toString();

        TimemanagementActivity.hideKeyboard(getActivity());

        if (TextUtils.isEmpty(profileOldPassword) || TextUtils.isEmpty(profilePassword) || TextUtils.isEmpty(profileRepeatPassword)) {
            Toast.makeText(getActivity(), "Password required", Toast.LENGTH_LONG).show();
            return;
        }
        if (!TextUtils.equals(profilePassword, profileRepeatPassword)) {
            Toast.makeText(getActivity(), "Passwords aren't equal. Try again.", Toast.LENGTH_LONG).show();
            return;
        }

        loadingFrameLayout.setVisibility(View.VISIBLE);

        if (user != null) {
            String userId = user.getProviderId();
            String id = user.getUid();
            String profileEmail = user.getEmail();

            firebaseApplication.changeUserPassword(getActivity(), user, profileOldPassword, profilePassword, loadingFrameLayout);
        }

    }
}
