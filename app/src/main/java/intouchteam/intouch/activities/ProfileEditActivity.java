package intouchteam.intouch.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intouchteam.intouch.R;
import intouchteam.intouch.adapter.MainActivityEventsAdapter;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerProfile;
import intouchteam.intouch.intouchapi.model.Event;
import intouchteam.intouch.intouchapi.model.Profile;

public class ProfileEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(InTouchApi.getProfile().getFirstName() + " " + InTouchApi.getProfile().getLastName());
        setSupportActionBar(toolbar);

        SetProfile();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            UpdateProfile();
            return true;
        } else if(id == R.id.action_clear) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UpdateProfile() {
        TextView phone = ((TextView) findViewById(R.id.profile_phone));
        TextView email = ((TextView) findViewById(R.id.profile_email));
        TextView skype = ((TextView) findViewById(R.id.profile_skype));
        String image_url = "some url";

        //TODO add image_url.

        InTouchServerProfile.update(phone.getText().toString(),
                email.getText().toString(),
                skype.getText().toString(),
                image_url,
                new InTouchCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        Toast.makeText(ProfileEditActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(InTouchApi.getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

    private void SetProfile(){
        InTouchServerProfile.getUser(InTouchApi.getProfile().getId(),
                new InTouchCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        TextView phone = ((TextView) findViewById(R.id.profile_phone));
                        TextView email = ((TextView) findViewById(R.id.profile_email));
                        TextView skype = ((TextView) findViewById(R.id.profile_skype));

                        Profile profile = new Gson().fromJson(result.get("user").getAsString(), Profile.class);

                        skype.setText(profile.getSkype().toString());
                        email.setText(profile.getEmail().toString());
                        phone.setText(profile.getPhone().toString());
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(InTouchApi.getContext(), "Cant load info:" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_create, menu);
        return true;
    }
}

