package at.thelegend27.timemanagementtool;

import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.thelegend27.timemanagementtool.HelperClasses.TestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by dominik on 07.06.17.
 */
@RunWith(AndroidJUnit4.class)

public class SuperVisorTests {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public void setUp() {
        onView(withId(R.id.login_email)).perform(typeText(TestHelper.supervisor_user));
        onView(withId(R.id.login_password)).perform(typeText(TestHelper.supervisor_password));
        onView(withId(R.id.login_button)).perform(click());
    }

    @Test
    public void testSUperVisorTasks() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_tasks));
        Thread.sleep(1000);
        onView(withText("All Open")).perform(click());
        Thread.sleep(1000);
        onView(withText("All Closed")).perform(click());
    }

    @Test
    public void testSUperVisorEmployee() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_employee_overview));
        Thread.sleep(1000);
    }

    @Test
    public void testSupervisorDepartment() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_department_details));
        Thread.sleep(1000);
    }
}

