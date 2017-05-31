package at.thelegend27.timemanagementtool;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.thelegend27.timemanagementtool.Firebase.FirebaseApplication;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by markusfriedl on 30/05/2017.
 */

@RunWith(AndroidJUnit4.class)
public class LoginInstrumentedTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public void setUp() {
        FirebaseApplication firebaseApplication = new FirebaseApplication();
        if (firebaseApplication.getCurrentFirebaseUser() != null) {
            firebaseApplication.logoutCurrentFirebaseUser();

            Intent loginIntent = new Intent();
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mActivityRule.launchActivity(loginIntent);
        }
    }

    @Test
    public void inputCorrectLogin() {
        onView(withId(R.id.login_email)).perform(typeText("test@test.com"));
        onView(withId(R.id.login_password)).perform(typeText("test123"));

        onView(withId(R.id.login_button)).perform(click());
    }

    @Test
    public void checkInvalidEmailLogin() {
        onView(withId(R.id.login_email)).perform(typeText("test@test"));
        onView(withId(R.id.login_password)).perform(typeText("test123"));
        onView(withId(R.id.login_email_wrapper)).check(matches(hasTextInputLayoutHintText(mActivityRule.getActivity().getString(R.string.invalid_email_address))));
    }

    @Test
    public void checkShortPasswordLogin() {
        onView(withId(R.id.login_email)).perform(typeText("test@test.com"));
        onView(withId(R.id.login_password)).perform(typeText("tes"));
        onView(withId(R.id.login_button)).perform(click());

        onView(withId(R.id.login_password_wrapper)).check(matches(hasTextInputLayoutHintText(mActivityRule.getActivity().getString(R.string.short_password))));
    }

    @Test
    public void checkMissingPasswordLogin() {
        onView(withId(R.id.login_email)).perform(typeText(""));
        onView(withId(R.id.login_password)).perform(typeText(""));
        onView(withId(R.id.login_button)).perform(click());

        onView(withId(R.id.login_email_wrapper)).check(matches(hasTextInputLayoutHintText(mActivityRule.getActivity().getString(R.string.missing_email_address))));
        onView(withId(R.id.login_password_wrapper)).check(matches(hasTextInputLayoutHintText(mActivityRule.getActivity().getString(R.string.error_field_required))));
    }


    @Test
    public void switchToSignUp() {
        onView(withId(R.id.switch_sign_up_text_view)).perform(click());
    }

    public static Matcher<View> hasTextInputLayoutHintText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}

