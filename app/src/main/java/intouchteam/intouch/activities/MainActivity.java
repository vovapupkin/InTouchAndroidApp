package intouchteam.intouch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.JsonObject;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.InTouchAuthorization;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerEvent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(floatingActionButtonListener());
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        testEvents();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_events) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            navigationLogout();
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

    private View.OnClickListener floatingActionButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        };
    }

    private void testEvents() {
        InTouchServerEvent.create(
                "name",
                "description",
                "gps",
                "time",
                "address",
                (long)1,
                "city",
                new InTouchCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        System.out.println("create:" + result);
                    }

                    @Override
                    public void onError(String error) {
                        System.out.println("create error:" + error);
                    }
                }
        );
        InTouchServerEvent.get(new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                System.out.println("get:" + result);
            }

            @Override
            public void onError(String error) {
                System.out.println("get error:" + error);
            }
        });
        InTouchServerEvent.get((long) 1, new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                System.out.println("get id1:" + result);
            }

            @Override
            public void onError(String error) {
                System.out.println("get id1 error:" + error);
            }
        });
    }
}
