package intouchteam.intouch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import intouchteam.intouch.R;
import intouchteam.intouch.activities.MainActivityFragments.MyEventsFragment;
import intouchteam.intouch.activities.MainActivityFragments.SearchEventFragment;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.InTouchAuthorization;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setProfileToNavigationBar();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new MyEventsFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filters) {
            //Filter action there
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_search:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, new SearchEventFragment())
                        .commit();
                break;
            case R.id.nav_events:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, new MyEventsFragment())
                        .commit();
                break;
            case R.id.nav_friends:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_logout:
                navigationLogout();
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigationLogout() {
        InTouchAuthorization.logOut();
        Intent intent = new Intent(this, FirstActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setProfileToNavigationBar() {
        if(InTouchApi.getProfile() != null) {
            ((TextView) navigationView.getHeaderView(0)
                    .findViewById(R.id.nav_name))
                    .setText(InTouchApi.getProfile().getFirstName() + " " + InTouchApi.getProfile().getLastName());
            ((TextView) navigationView.getHeaderView(0)
                    .findViewById(R.id.nav_login))
                    .setText(InTouchApi.getProfile().getLogin());
        }
    }
}
