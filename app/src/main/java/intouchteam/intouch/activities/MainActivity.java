package intouchteam.intouch.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My events");
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setProfileToNavigationBar();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(getString(R.string.REGISTRATION_COMPLETE))) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra("token");

                    Toast.makeText(getApplicationContext(), "GCM registration token: " + token, Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(getString(R.string.PUSH_NOTIFICATION))) {
                    // new push notification is received

                    Toast.makeText(getApplicationContext(), intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
                }
            }
        };
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new MyEventsFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver,
                new IntentFilter(getString(R.string.REGISTRATION_COMPLETE)));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver,
                new IntentFilter(getString(R.string.PUSH_NOTIFICATION)));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
        super.onPause();
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
        MyEventsFragment myEventsFragment = new MyEventsFragment();
        switch (id) {
            case R.id.action_followed:
                myEventsFragment.setCreatorFilter(false);
                toolbar.setTitle("I follow");
                break;
            case R.id.action_manage:
                myEventsFragment.setCreatorFilter(true);
                toolbar.setTitle("I create");
                break;
        }
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, myEventsFragment)
                .commit();
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_search:
                toolbar.getMenu().clear();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, new SearchEventFragment())
                        .commit();
                toolbar.setTitle("Search");
                break;
            case R.id.nav_events:
                toolbar.getMenu().clear();
                getMenuInflater().inflate(R.menu.nav_bar_menu, toolbar.getMenu());
                MyEventsFragment myEventsFragment = new MyEventsFragment();
                myEventsFragment.setCreatorFilter(false);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, myEventsFragment)
                        .commit();
                toolbar.setTitle("I follow");
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
