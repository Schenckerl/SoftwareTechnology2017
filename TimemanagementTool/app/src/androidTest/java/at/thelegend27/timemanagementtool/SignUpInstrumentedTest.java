package at.thelegend27.timemanagementtool;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.thelegend27.timemanagementtool.Firebase.FirebaseApplication;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by markusfriedl on 31/05/2017.
 */

@RunWith(AndroidJUnit4.class)
public class SignUpInstrumentedTest {

    @Rule
    public ActivityTestRule<SignUpActivity> mActivityRule = new ActivityTestRule<>(
            SignUpActivity.class);

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
    public void inputCorrectSignUpInput() {
        onView(withId(R.id.forename)).perform(typeText("Max"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.surename)).perform(typeText("Mustermann"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.email)).perform(typeText("muster@test.com"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.password)).perform(typeText("test123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirm_password)).perform(typeText("test123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.company)).perform(typeText("Muster company"));
        Espresso.closeSoftKeyboard();
        //onView(withId(R.id.sign_up_button)).perform(click());
    }

    @Test
    public void inputIncorrectSignUpInput() {
        onView(withId(R.id.forename)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.surename)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.email)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.password)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirm_password)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.company)).perform(typeText(""));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.sign_up_button)).perform(scrollTo());
        onView(withId(R.id.sign_up_button)).perform(click());

        onView(withId(R.id.forename_wrapper)).check(matches(hasTextInputLayoutHintText(mActivityRule.getActivity().getString(R.string.error_field_required))));
        onView(withId(R.id.surename_wrapper)).check(matches(hasTextInputLayoutHintText(mActivityRule.getActivity().getString(R.string.error_field_required))));
        onView(withId(R.id.email_wrapper)).check(matches(hasTextInputLayoutHintText(mActivityRule.getActivity().getString(R.string.missing_email_address))));
        onView(withId(R.id.password_wrapper)).check(matches(hasTextInputLayoutHintText(mActivityRule.getActivity().getString(R.string.error_field_required))));
        onView(withId(R.id.confirm_password_wrapper)).check(matches(hasTextInputLayoutHintText(mActivityRule.getActivity().getString(R.string.original_field_empty))));
        onView(withId(R.id.company_wrapper)).check(matches(hasTextInputLayoutHintText(mActivityRule.getActivity().getString(R.string.error_field_required))));
    }

//    @Test
//    public void inputWrongConfirmPasswordSignUpInput() throws InterruptedException {
//        onView(withId(R.id.password)).perform(typeText("test123"));
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.confirm_password)).perform(typeText("test321"));
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.sign_up_button)).perform(click());
//
//        onView(withId(R.id.confirm_password_wrapper)).check(matches(hasTextInputLayoutHintText(mActivityRule.getActivity().getString(R.string.password_must_be_same))));
//    }

    @Test
    public void switchToLogin() {
        onView(withId(R.id.switch_login_text_view)).perform(ViewActions.scrollTo());
        onView(withId(R.id.switch_login_text_view)).perform(click());
    }
    @AfterClass
    public static void teardown(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Companies");
        ref.child("Muster Company").removeValue();
        final DatabaseReference md = FirebaseDatabase.getInstance().getReference("Users");
        md.orderByChild("email").equalTo("muster@test.com").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                md.child(dataSnapshot.getKey()).removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
