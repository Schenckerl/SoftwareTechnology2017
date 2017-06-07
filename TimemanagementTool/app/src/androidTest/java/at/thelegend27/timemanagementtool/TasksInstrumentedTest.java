package at.thelegend27.timemanagementtool;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.thelegend27.timemanagementtool.HelperClasses.TestHelper;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by dominik on 07.06.17.
 */
@RunWith(AndroidJUnit4.class)
public class TasksInstrumentedTest {

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
    public void testTasks() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_tasks));
        Thread.sleep(1000);
        onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        Thread.sleep(1000);
        Espresso.pressBack();
        Thread.sleep(1000);
        onView(withText("All Open")).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        Thread.sleep(1000);
        Espresso.pressBack();
        onView(withText("All Closed")).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        Thread.sleep(1000);
    }

    @Test
    public void testTaskCreation() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_tasks));
        Thread.sleep(2000);
        onView(withId(R.id.add_new_task_admin)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.addTaskName)).perform(typeText("Test Task"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.addTaskDescription)).perform(typeText("Test Description"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.addTaskDeadlineButton)).perform(click());
        Thread.sleep(1000);
        onView(withText("OK")).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.addTaskButton)).perform(click());
    }

    @Test
    public void testTaskEmployeeCreation() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_tasks));
        Thread.sleep(2000);
        onView(withId(R.id.add_new_task_for_employee)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.addTaskNameAdmin)).perform(typeText("Test Admin Task"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.addTaskDescriptionAdmin)).perform(typeText("Test Admin Description"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.addTaskDeadlineButtonAdmin)).perform(click());
        Thread.sleep(1000);
        onView(withText("OK")).perform(click());
        onView(withId(R.id.addTaskButtonAdmin)).perform(click());
    }

    @Test
    public void testTaskCreationButtons() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_tasks));
        Thread.sleep(1000);
        onView(withId(R.id.add_new_task_admin)).perform(click());
        Thread.sleep(1000);
        Espresso.closeSoftKeyboard();
        Espresso.pressBack();
        Thread.sleep(1000);
        onView(withId(R.id.add_new_task_for_employee)).perform(click());
        Espresso.closeSoftKeyboard();
        Espresso.pressBack();
        Thread.sleep(1000);
        onView(withText("All Open")).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.add_new_task_admin)).perform(click());
        Thread.sleep(1000);
        Espresso.closeSoftKeyboard();
        Espresso.pressBack();
        Thread.sleep(1000);
        onView(withId(R.id.add_new_task_for_employee)).perform(click());
        Espresso.closeSoftKeyboard();
        Espresso.pressBack();
        Thread.sleep(1000);
        onView(withText("All Closed")).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.add_new_task_admin)).perform(click());
        Thread.sleep(1000);
        Espresso.closeSoftKeyboard();
        Espresso.pressBack();
        Thread.sleep(1000);
        onView(withId(R.id.add_new_task_for_employee)).perform(click());
        Espresso.closeSoftKeyboard();
        Espresso.pressBack();
        Thread.sleep(1000);
    }


    @AfterClass
    public static void teardown(){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tasks");

        ref.orderByChild("task_name").equalTo("Test Admin Task").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ref.child(dataSnapshot.getKey()).removeValue();
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
        ref.orderByChild("task_name").equalTo("Test Task").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ref.child(dataSnapshot.getKey()).removeValue();
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
}
