package at.thelegend27.timemanagementtool;

import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import layout.EditProfileFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by markusfriedl on 31/05/2017.
 */

@RunWith(AndroidJUnit4.class)
public class EditProfileInstrumentedTest {

    @Rule
    public FragmentTestRule<EditProfileFragment> mFragementTestRule = new
            FragmentTestRule<>(EditProfileFragment.class);

    @Test
    public void changeUserName() {
        mFragementTestRule.launchActivity(null);
        onView(withId(R.id.profile_name)).perform(clearText());
        onView(withId(R.id.profile_name)).perform(typeText("Max Mustermann"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_edit_button)).perform(click());

        onView(withId(R.id.profile_name)).perform(clearText());
        onView(withId(R.id.profile_name)).perform(typeText("Test User"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_edit_button)).perform(click());
    }

    @Test
    public void changeUserPasswort() {
        mFragementTestRule.launchActivity(null);
        onView(withId(R.id.profile_old_password)).perform(clearText());
        onView(withId(R.id.profile_old_password)).perform(typeText("test123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_password)).perform(clearText());
        onView(withId(R.id.profile_password)).perform(typeText("321tset"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_repeat_password)).perform(clearText());
        onView(withId(R.id.profile_repeat_password)).perform(typeText("321tset"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_password_button)).perform(click());

        onView(withId(R.id.profile_old_password)).perform(clearText());
        onView(withId(R.id.profile_old_password)).perform(typeText("321tset"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_password)).perform(clearText());
        onView(withId(R.id.profile_password)).perform(typeText("test123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_repeat_password)).perform(clearText());
        onView(withId(R.id.profile_repeat_password)).perform(typeText("test123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_password_button)).perform(click());
    }

    @Test
    public void changeUserPasswortFailed() {
        mFragementTestRule.launchActivity(null);
        onView(withId(R.id.profile_old_password)).perform(clearText());
        onView(withId(R.id.profile_old_password)).perform(typeText("test123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_password)).perform(clearText());
        onView(withId(R.id.profile_password)).perform(typeText("321tset"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_repeat_password)).perform(clearText());
        onView(withId(R.id.profile_repeat_password)).perform(typeText("321tse"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_password_button)).perform(click());

        onView(withId(R.id.profile_old_password)).perform(clearText());
        onView(withId(R.id.profile_old_password)).perform(typeText("test"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_password)).perform(clearText());
        onView(withId(R.id.profile_password)).perform(typeText("test123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_repeat_password)).perform(clearText());
        onView(withId(R.id.profile_repeat_password)).perform(typeText("test123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_password_button)).perform(click());

        onView(withId(R.id.profile_old_password)).perform(clearText());
        onView(withId(R.id.profile_old_password)).perform(typeText("test123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_password)).perform(clearText());
        onView(withId(R.id.profile_password)).perform(typeText("test"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_repeat_password)).perform(clearText());
        onView(withId(R.id.profile_repeat_password)).perform(typeText("test"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_password_button)).perform(click());

        onView(withId(R.id.profile_old_password)).perform(clearText());
        onView(withId(R.id.profile_old_password)).perform(typeText("test123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_password)).perform(clearText());
        onView(withId(R.id.profile_password)).perform(typeText("test123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_repeat_password)).perform(clearText());
        onView(withId(R.id.profile_repeat_password)).perform(typeText("test12"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_password_button)).perform(click());
    }
}
