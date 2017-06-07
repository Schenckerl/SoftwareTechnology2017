package at.thelegend27.timemanagementtool;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.format.Time;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.thelegend27.timemanagementtool.HelperClasses.TestHelper;
import layout.AdminFragment;
import layout.AdminTabHost;
import layout.EditProfileFragment;
import layout.TasksFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class UiInstrumentedTest{

    @Rule
    public FragmentTestRule<TasksFragment> mFragementTestTaskRule = new
            FragmentTestRule<>(TasksFragment.class);
    @Rule
    public FragmentTestRule<AdminTabHost> mFragementTestAdminTabRule = new
            FragmentTestRule<>(AdminTabHost.class);
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    public static ViewAction clickXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

    @Before
    public void setUp() {
        onView(withId(R.id.login_email)).perform(typeText(TestHelper.boss_email));
        onView(withId(R.id.login_password)).perform(typeText(TestHelper.boss_password));
        onView(withId(R.id.login_button)).perform(click());
    }

    @Test
    public void TestClick() throws InterruptedException {
        Thread.sleep(2000);
    }
    @Test
    public void switchToTasks() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_tasks));
        Matcher<View> matcher = allOf(withText("All Open"),
                isDescendantOfA(withId(R.id.tab_host)));
        onView(matcher).perform(click());
    }
    @Test
    public void TestAdministrationCreateUser() throws InterruptedException {
        mFragementTestAdminTabRule.launchActivity(null);
        onView(withId(R.id.username)).perform(typeText("Max Musermann"));
        onView(withId(R.id.email)).perform(typeText("max@test.com"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.target_hour)).perform(typeText("44"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.password)).perform(typeText("Gulasch1994"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_user)).perform(scrollTo());
        onView(withId(R.id.submit_user)).perform(click());
    }

    @Test
    public void TestAdministrationCreateDepartment() throws InterruptedException {
        mFragementTestAdminTabRule.launchActivity(null);
        onView(withId(R.id.submit_dep)).perform(scrollTo());
        onView(withId(R.id.department_name)).perform(typeText("Automatic Department"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_dep)).perform(click());
    }
}

