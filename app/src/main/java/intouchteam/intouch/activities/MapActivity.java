package intouchteam.intouch.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.GoogleAPIsRequest;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    static LatLng eventPlace;
    static String address;
    static GoogleMap map;
    static Context context;
    Toolbar toolbar;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_map);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        address = getIntent().getStringExtra(getString(R.string.address));
        findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String address = GoogleAPIsRequest.getAddress(eventPlace, context);
                intent.putExtra(getString(R.string.address), address);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        MapActivity.map = map;
        eventPlace = GoogleAPIsRequest.getCoords(address, getApplicationContext(), new LatLng(53.91163, 27.59585));
        map.addMarker(new MarkerOptions().position(eventPlace).title(getString(R.string.map_marker_title)));
        map.moveCamera(CameraUpdateFactory.newLatLng(eventPlace));
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setCoords(latLng.latitude, latLng.longitude);
            }
        });
    }

    static public void setCoords(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        map.clear();
        map.addMarker(new MarkerOptions().position(latLng).title(context.getString(R.string.map_marker_title)));
        eventPlace = latLng;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        searchView = (SearchView) toolbar.getMenu().findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                eventPlace = GoogleAPIsRequest.getCoords(query, getApplicationContext(), new LatLng(53.91163, 27.59585));
                map.clear();
                map.addMarker(new MarkerOptions().position(eventPlace).title(context.getString(R.string.map_marker_title)));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

}