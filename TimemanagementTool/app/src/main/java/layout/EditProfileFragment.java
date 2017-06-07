package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import at.thelegend27.timemanagementtool.Firebase.FirebaseApplication;
import at.thelegend27.timemanagementtool.HelperClasses.ConfirmFieldValidatorHelper;
import at.thelegend27.timemanagementtool.HelperClasses.PasswordFieldValidatorHelper;
import at.thelegend27.timemanagementtool.R;
import at.thelegend27.timemanagementtool.TimemanagementActivity;

@VisibleForTesting
public class EditProfileFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {
    private static final int PASS_MIN_LENGTH = 6;

    private TextInputEditText editProfileName;
    private TextInputEditText editProfileOldPassword;
    private TextInputEditText editProfilePassword;
    private TextInputEditText editProfileRepeatPassword;
    private static ProgressBar progressBar;

    private PasswordFieldValidatorHelper oldPasswordFieldValidator;
    private PasswordFieldValidatorHelper passwordFieldValidator;
    private ConfirmFieldValidatorHelper confirmPasswordFieldValidator;

    private final FirebaseApplication firebaseApplication = new FirebaseApplication();
    private final FirebaseUser user = firebaseApplication.getCurrentFirebaseUser();

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

        TextInputLayout oldPasswordWrapper = (TextInputLayout) getView().findViewById(R.id.profile_old_password_wrapper);
        TextInputLayout passwordWrapper = (TextInputLayout) getView().findViewById(R.id.profile_password_wrapper);
        TextInputLayout confirmPasswordWrapper = (TextInputLayout) getView().findViewById(R.id.profile_repeat_password_wrapper);

        editProfileName = (TextInputEditText)getView().findViewById(R.id.profile_name);
        editProfileOldPassword = (TextInputEditText)getView().findViewById(R.id.profile_old_password);
        editProfilePassword = (TextInputEditText)getView().findViewById(R.id.profile_password);
        editProfileRepeatPassword = (TextInputEditText)getView().findViewById(R.id.profile_repeat_password);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBarEditProfil);

        oldPasswordFieldValidator = new PasswordFieldValidatorHelper(oldPasswordWrapper, PASS_MIN_LENGTH);
        passwordFieldValidator = new PasswordFieldValidatorHelper(passwordWrapper, PASS_MIN_LENGTH);
        confirmPasswordFieldValidator = new ConfirmFieldValidatorHelper(confirmPasswordWrapper);

        Button saveEditButton = (Button)getView().findViewById(R.id.save_edit_button);
        final Button savePasswordButton = (Button)getView().findViewById(R.id.save_password_button);

        final FirebaseApplication firebaseApplication = new FirebaseApplication();
        final FirebaseUser user = firebaseApplication.getCurrentFirebaseUser();

        if (user != null) {
            editProfileName.setText(user.getDisplayName());
        }

        editProfileOldPassword.setOnFocusChangeListener(this);
        editProfilePassword.setOnFocusChangeListener(this);
        editProfileRepeatPassword.setOnFocusChangeListener(this);

        saveEditButton.setOnClickListener(this);
        savePasswordButton.setOnClickListener(this);
    }

    private void saveUserProfile(FirebaseUser user, FirebaseApplication firebaseApplication) {
        String profileName = editProfileName.getText().toString();

        TimemanagementActivity.hideKeyboard(getActivity());

        if (TextUtils.isEmpty(profileName)) {
            Toast.makeText(getActivity(), "Name required", Toast.LENGTH_LONG).show();
            return;
        }

        visibilityProgressbarEditProfile(View.VISIBLE);

        if (user != null) {
            String userId = user.getProviderId();
            String id = user.getUid();
            String profileEmail = user.getEmail();

            firebaseApplication.updateUserProfile(getActivity(), user, profileName, progressBar);
        }
    }

    private void saveUserPassword(FirebaseUser user, FirebaseApplication firebaseApplication) {
        String profileOldPassword = editProfileOldPassword.getText().toString();
        String profilePassword = editProfilePassword.getText().toString();

        TimemanagementActivity.hideKeyboard(getActivity());

        visibilityProgressbarEditProfile(View.VISIBLE);

        if (user != null) {
            firebaseApplication.changeUserPassword(getActivity(), user, profileOldPassword, profilePassword, progressBar);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            return; //we want to validate only fields loosing focus and not fields gaining focus
        }

        int id = view.getId();
        if (id == R.id.profile_old_password) {
            oldPasswordFieldValidator.validate(editProfileOldPassword.getText().toString());
        } else if (id == R.id.profile_password) {
            passwordFieldValidator.validate(editProfilePassword.getText().toString());
        } else if (id == R.id.profile_repeat_password) {
            confirmPasswordFieldValidator.confirm(editProfilePassword.getText().toString(), editProfileRepeatPassword.getText().toString());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.save_edit_button) {
            saveUserProfile(user, firebaseApplication);
        }
        else if (id == R.id.save_password_button) {
            String enteredOldPassword = editProfileOldPassword.getText().toString();
            String enteredPassword = editProfilePassword.getText().toString();
            String enteredConfirmPassword = editProfileRepeatPassword.getText().toString();

            boolean isOldPasswordValid= passwordFieldValidator.validate(enteredOldPassword);
            boolean isPasswordValid= passwordFieldValidator.validate(enteredPassword);
            boolean isConfirmPasswordValid= confirmPasswordFieldValidator.confirm(enteredPassword, enteredConfirmPassword);

            if (!isOldPasswordValid || !isPasswordValid || !isConfirmPasswordValid) {
                //go ahead ans submit the form for all things are fine now
                Toast.makeText(getActivity(), "Field Validations failed! Please check your inputs", Toast.LENGTH_SHORT).show();
                return;
            }

            saveUserPassword(user, firebaseApplication);
        }
    }

    public static void visibilityProgressbarEditProfile(int visibility) {
        progressBar.setVisibility(visibility);
    }
}
