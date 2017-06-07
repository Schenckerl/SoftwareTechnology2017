package at.thelegend27.timemanagementtool;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.thelegend27.timemanagementtool.HelperClasses.TestHelper;
import layout.AdminTabHost;
import layout.TasksFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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

    @Before
    public void setUp() {
        onView(withId(R.id.login_email)).perform(typeText(TestHelper.boss_email));
        onView(withId(R.id.login_password)).perform(typeText(TestHelper.boss_password));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
    }
    @Test
    public void switchToTasks() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_tasks));
        Thread.sleep(1000);
        onView(withText("All Open")).perform(click());
        Thread.sleep(1000);
        onView(withText("All Closed")).perform(click());
    }
    @Test
    public void TestAdministrationCreateUser() throws InterruptedException {
        mFragementTestAdminTabRule.launchActivity(null);
        onView(withId(R.id.username)).perform(typeText("Max Mustermann"));
        onView(withId(R.id.email)).perform(typeText("max@test.com"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.target_hour)).perform(typeText("44"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.password)).perform(typeText("Gulasch1994"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_user)).perform(scrollTo());
        onView(withId(R.id.submit_user)).perform(click());
    }

/*    @Test
    public void TestAdministrationCreateDepartment() throws InterruptedException {
        mFragementTestAdminTabRule.launchActivity(null);
        onView(withId(R.id.submit_dep)).perform(scrollTo());
        onView(withId(R.id.department_name)).perform(typeText("Automatic Department"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_dep)).perform(click());
    }*/

    @Test
    public void switchToEmployeeOverview() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_employee_overview));
        Thread.sleep(1000);
        onView(withText("Employees")).perform(click());
        Thread.sleep(1000);
        onView(withText("Supervisors")).perform(click());
    }

    @Test
    public void switchToDepartment() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_department_overview));
        Thread.sleep(1000);
        onView(withText("Automatic Department")).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.edit_department)).perform(click());
        onView(withText("Cancel")).perform(click());
    }
}

