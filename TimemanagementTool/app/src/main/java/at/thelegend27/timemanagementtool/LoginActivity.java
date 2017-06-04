package at.thelegend27.timemanagementtool;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import at.thelegend27.timemanagementtool.Firebase.FirebaseApplication;
import at.thelegend27.timemanagementtool.HelperClasses.EmailFieldValidatorHelper;
import at.thelegend27.timemanagementtool.HelperClasses.PasswordFieldValidatorHelper;

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {

    private static final int PASS_MIN_LENGTH = 6;
    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextView switchSignUpTextView;
    private TextView loginError;
    private FirebaseAuth mAuth;
    private EmailFieldValidatorHelper emailFieldValidator;
    private PasswordFieldValidatorHelper passwordFieldValidator;

    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mAuth = ((FirebaseApplication) getApplication()).getFirebaseAuth();
        ((FirebaseApplication) getApplication()).checkUserLogin(LoginActivity.this);

        loginError = (TextView) findViewById(R.id.login_error);
        emailInput = (TextInputEditText) findViewById(R.id.login_email);
        passwordInput = (TextInputEditText) findViewById(R.id.login_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        switchSignUpTextView = (TextView) findViewById(R.id.switch_sign_up_text_view);
        switchSignUpTextView.setOnClickListener(this);
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.login_email_wrapper);
        TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.login_password_wrapper);

        emailInput.setOnFocusChangeListener(this);
        passwordInput.setOnFocusChangeListener(this);

        emailFieldValidator = new EmailFieldValidatorHelper(emailWrapper);
        passwordFieldValidator = new PasswordFieldValidatorHelper(passwordWrapper, PASS_MIN_LENGTH);

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            return; //we want to validate only fields loosing focus and not fields gaining focus
        }

        int id = view.getId();
        if (id == R.id.login_email) {
            emailFieldValidator.validate(emailInput.getText().toString());
        } else if (id == R.id.login_password) {
            passwordFieldValidator.validate(passwordInput.getText().toString());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.login_button) {
            visibilityProgressbarLogin(View.VISIBLE);

            String enteredEmail = emailInput.getText().toString();
            String enteredPassword = passwordInput.getText().toString();

            boolean isEmailValid = emailFieldValidator.validate(enteredEmail);
            boolean isPasswordValid= passwordFieldValidator.validate(enteredPassword);

            if (!isEmailValid || !isPasswordValid) {
                //go ahead ans submit the form for all things are fine now
                Toast.makeText(this, "Field Validations failed! Please check your inputs", Toast.LENGTH_SHORT).show();
                visibilityProgressbarLogin(View.INVISIBLE);
                return;
            }

            ((FirebaseApplication) getApplication()).loginAUser(LoginActivity.this, enteredEmail, enteredPassword, loginError);
        } else if (id == R.id.switch_sign_up_text_view) {
            Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(signUpIntent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(((FirebaseApplication)getApplication()).mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (((FirebaseApplication) getApplication()).mAuthListener != null) {
            //mAuth.removeAuthStateListener(((FirebaseApplication)getApplication()).mAuthListener);
        }
    }

    public static void visibilityProgressbarLogin(int visibility) {
        progressBar.setVisibility(visibility);
    }

    public static void completeLogin(final Context context){
        Intent timemanagementIntent = new Intent(context, TimemanagementActivity.class);
        context.startActivity(timemanagementIntent);
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "User has been logged in ", Toast.LENGTH_LONG).show();
            }
        });
    }
}