package at.thelegend27.timemanagementtool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import at.thelegend27.timemanagementtool.Firebase.FirebaseApplication;
import at.thelegend27.timemanagementtool.HelperClasses.CurrentSession;
import layout.AdminTabHost;
import layout.DashboardFragment;
import layout.DepartmentDetails;
import layout.DepartmentOverview;
import layout.EditProfileFragment;
import layout.EmployeeOverview;
import layout.EmployeeTabHost;
import layout.StatisticsTabHost;
import layout.TaskTabHost;
import layout.TasksFragment;

/**
 * Created by markusfriedl on 08/05/2017.
 */

public class TimemanagementActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    private static TextView userNameTextView;
    private static TextView userEmailTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setUserInfos();
            }
        } ;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(CurrentSession.getInstance().getCurrent_user().isCEO) {
            navigationView.getMenu().findItem(R.id.admin).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_employee_overview).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_department_overview).setVisible(true);
        }else if(CurrentSession.getInstance().getCurrent_user().isSupervisor){
            navigationView.getMenu().findItem(R.id.nav_employee_overview).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_department_details).setVisible(true);
        }

        View v = navigationView.getHeaderView(0);
        userNameTextView = (TextView) v.findViewById(R.id.nav_name);
        userEmailTextView = (TextView) v.findViewById(R.id.nav_email);

        setUserInfos();

        displaySelectedScreen(R.id.nav_dashboard);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;
    }

    public void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_dashboard:
                fragment = new DashboardFragment();
                break;
            case R.id.nav_tasks:
                if(CurrentSession.getInstance().getCurrent_user().isCEO ||
                        CurrentSession.getInstance().getCurrent_user().isSupervisor){
                    fragment = new TaskTabHost();
                }else{
                    fragment = new TasksFragment();
                }
                break;
            case R.id.nav_statistics:
                fragment = new StatisticsTabHost();
                break;
            case R.id.nav_logout:
                logoutCurrentUser();
                break;
            case R.id.edit_profile_fragment:
                fragment = new EditProfileFragment();
                break;
            case R.id.admin:
                fragment = new AdminTabHost();//new AdminFragment();
                break;
            case R.layout.fragment_dashboard:
                fragment = new DashboardFragment();
                break;
            case R.id.nav_employee_overview:
                if(CurrentSession.getInstance().getCurrent_user().isSupervisor) {
                    fragment = new EmployeeOverview();
                }else{
                    fragment = new EmployeeTabHost();
                }
                break;
            case R.id.nav_department_overview:
                fragment = new DepartmentOverview();
                break;
            case R.layout.department_detail:
                fragment = new DepartmentDetails();
                break;
            case R.id.nav_department_details:
                Log.d("Switching", "Departmnet is: " + CurrentSession.getInstance().getDepartment().name);
                showDepDetail(CurrentSession.getInstance().getDepartment().name);
                break;
        }

        //replacing the fragment
        if (fragment != null ) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void editProfileButtonPressed(View view) {
        displaySelectedScreen(R.id.edit_profile_fragment);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setUserInfos() {
        FirebaseApplication firebaseApplication = new FirebaseApplication();
        FirebaseUser user = firebaseApplication.getCurrentFirebaseUser();

        if (user != null) {
            String userName = CurrentSession.getInstance().getCurrent_user().fullName;
            String userEmail = CurrentSession.getInstance().getCurrent_user().email;

            userNameTextView.setText(userName);
            userEmailTextView.setText(userEmail);
        }
    }

    private void logoutCurrentUser() {
        FirebaseApplication firebaseApplication = new FirebaseApplication();
        firebaseApplication.logoutCurrentFirebaseUser();

        Intent loginIntent = new Intent(TimemanagementActivity.this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    public void showDepDetail(String name){
        DepartmentDetails fragment = new DepartmentDetails();
        fragment.dep = name;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }
}
