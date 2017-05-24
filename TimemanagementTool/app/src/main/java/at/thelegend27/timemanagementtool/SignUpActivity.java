package at.thelegend27.timemanagementtool;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import at.thelegend27.timemanagementtool.Firebase.FirebaseApplication;
import at.thelegend27.timemanagementtool.HelperClasses.EmailFieldValidatorHelper;
import at.thelegend27.timemanagementtool.HelperClasses.PasswordFieldValidatorHelper;
import at.thelegend27.timemanagementtool.HelperClasses.RequiredFieldValidatorHelper;

public class SignUpActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {
    private static final int PASS_MIN_LENGTH = 6;
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private TextInputEditText forenameInput;
    private TextInputEditText surenameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText companyInput;
    private RequiredFieldValidatorHelper foreameFieldValidator;
    private RequiredFieldValidatorHelper sureameFieldValidator;
    private EmailFieldValidatorHelper emailFieldValidator;
    private PasswordFieldValidatorHelper passwordFieldValidator;
    private RequiredFieldValidatorHelper companyFieldValidator;
    private TextView switchLoginTextView;
    private TextView loginError;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mAuth = ((FirebaseApplication) getApplication()).getFirebaseAuth();
        ((FirebaseApplication) getApplication()).checkUserLogin(SignUpActivity.this);
        loginError = (TextView) findViewById(R.id.login_error);
        forenameInput = (TextInputEditText) findViewById(R.id.forename);
        surenameInput = (TextInputEditText) findViewById(R.id.surename);
        emailInput = (TextInputEditText) findViewById(R.id.email);
        passwordInput = (TextInputEditText) findViewById(R.id.password);
        companyInput = (TextInputEditText) findViewById(R.id.company);
        switchLoginTextView = (TextView) findViewById(R.id.switch_login_text_view);

        TextInputLayout forenameWrapper = (TextInputLayout) findViewById(R.id.forename_wrapper);
        TextInputLayout surenameWrapper = (TextInputLayout) findViewById(R.id.surename_wrapper);
        TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.email_wrapper);
        TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.password_wrapper);
        TextInputLayout companyWrapper = (TextInputLayout) findViewById(R.id.company_wrapper);

        //set onFocus change Listener to all the EditText Views
        forenameInput.setOnFocusChangeListener(this);
        surenameInput.setOnFocusChangeListener(this);
        emailInput.setOnFocusChangeListener(this);
        passwordInput.setOnFocusChangeListener(this);
        companyInput.setOnFocusChangeListener(this);

        //init all the validators
        foreameFieldValidator = new RequiredFieldValidatorHelper(forenameWrapper);
        sureameFieldValidator = new RequiredFieldValidatorHelper(surenameWrapper);
        emailFieldValidator = new EmailFieldValidatorHelper(emailWrapper);
        passwordFieldValidator = new PasswordFieldValidatorHelper(passwordWrapper, PASS_MIN_LENGTH);
        companyFieldValidator = new RequiredFieldValidatorHelper(companyWrapper);

        Button loginButton = (Button) findViewById(R.id.sign_up_button);
        loginButton.setOnClickListener(this);

        switchLoginTextView.setOnClickListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            return; //we want to validate only fields loosing focus and not fields gaining focus
        }

        int id = view.getId();
        if (id == R.id.forename) {
            foreameFieldValidator.validate(forenameInput.getText().toString());
        } else if (id == R.id.surename) {
            sureameFieldValidator.validate(surenameInput.getText().toString());
        } else if (id == R.id.email) {
            emailFieldValidator.validate(emailInput.getText().toString());
        } else if (id == R.id.password) {
            passwordFieldValidator.validate(passwordInput.getText().toString());
        } else if (id == R.id.company) {
            companyFieldValidator.validate(companyInput.getText().toString());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.sign_up_button) {
            String enteredForename = forenameInput.getText().toString();
            String enteredSurename = surenameInput.getText().toString();
            String enteredEmail = emailInput.getText().toString();
            String enteredPassword = passwordInput.getText().toString();
            String enteredCompany = companyInput.getText().toString();

            boolean isForeameValid = foreameFieldValidator.validate(enteredForename);
            boolean isSurenameValid = sureameFieldValidator.validate(enteredSurename);
            boolean isEmailValid = emailFieldValidator.validate(enteredEmail);
            boolean isPasswordValid= passwordFieldValidator.validate(enteredPassword);
            boolean isCompanyValid= companyFieldValidator.validate(enteredCompany);

            if (!isForeameValid || !isSurenameValid || !isEmailValid || !isPasswordValid || !isCompanyValid) {
                //go ahead ans submit the form for all things are fine now
                Toast.makeText(this, "Field Validations failed! Please check your inputs", Toast.LENGTH_SHORT).show();
                return;
            }

            ((FirebaseApplication) getApplication()).createNewUser(SignUpActivity.this, enteredEmail, enteredPassword, loginError);
        }
        else if (id == R.id.switch_login_text_view) {
            Intent signUpIntent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(signUpIntent);
        }
    }
}
