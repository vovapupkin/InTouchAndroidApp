package intouchteam.intouch.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    static LatLng eventPlace;
    static String address;
    static GoogleMap map;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        address = getIntent().getStringExtra(getString(R.string.address));
        ((Button) findViewById(R.id.button_pick_an_address)).setOnClickListener(new View.OnClickListener() {
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
}