package at.thelegend27.timemanagementtool;

import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.thelegend27.timemanagementtool.HelperClasses.TestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by dominik on 07.06.17.
 */
@RunWith(AndroidJUnit4.class)
public class TimeTrackingInstrumentedTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public void setUp() {
        onView(withId(R.id.login_email)).perform(typeText(TestHelper.working_user));
        onView(withId(R.id.login_password)).perform(typeText(TestHelper.working_password));
        onView(withId(R.id.login_button)).perform(click());
    }
    @Test
    public void workingDayRoutine() throws InterruptedException {
        onView(withId(R.id.dashboardButtonArrive)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_tasks));
        Thread.sleep(1000);
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_dashboard));
        Thread.sleep(1000);
        onView(withId(R.id.dashboardButtonLeave)).perform(click());
        Thread.sleep(1000);
    }
    @Test
    public void finishedWorkingTest() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_tasks));
        Thread.sleep(1000);
    }

    @AfterClass
    public static void teardown(){
        System.out.println("reseting");
        DatabaseReference md = FirebaseDatabase.getInstance().getReference("Users");
        md.orderByChild("fullName").equalTo("Worker").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("found user");
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("WorkingDays");
                ref.orderByChild("user").equalTo(dataSnapshot.getKey()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        System.out.println("found working day");
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
