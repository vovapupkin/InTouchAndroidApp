package intouchteam.intouch.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.ImageDownloader;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerEvent;
import intouchteam.intouch.intouchapi.InTouchServerProfile;
import intouchteam.intouch.intouchapi.model.Profile;

public class FullProfile extends AppCompatActivity {

    Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_profile);
        id = getIntent().getLongExtra("userId", 0);
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        InTouchServerProfile.getUser(id,
            new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                Profile profile = new Gson().fromJson(result.get("user").getAsString(), Profile.class);

                InTouchServerEvent.GetFollowing(profile.getId(),
                        new InTouchCallback() {
                            @Override
                            public void onSuccess(JsonObject result) {
                                JsonArray jsonArray = new Gson().fromJson(result.get("users").getAsString(), JsonArray.class);
                                TextView following = (TextView) findViewById(R.id.following);
                                following.setText(String.valueOf(jsonArray.size()));
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                InTouchServerEvent.GetFollowers(profile.getId(),
                        new InTouchCallback() {
                            @Override
                            public void onSuccess(JsonObject result) {
                                JsonArray jsonArray = new Gson().fromJson(result.get("users").getAsString(), JsonArray.class);
                                TextView followers = (TextView) findViewById(R.id.followers);
                                followers.setText(String.valueOf(jsonArray.size()));
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                InTouchServerEvent.getByCreator(profile.getId(), new InTouchCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        JsonArray jsonEvents = new Gson().fromJson(result.get("events").getAsString(), JsonArray.class);
                        TextView events = (TextView)findViewById(R.id.events);
                        events.setText(String.valueOf(jsonEvents.size()));
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

                TextView phone = (TextView) findViewById(R.id.profile_phone);
                TextView email = (TextView) findViewById(R.id.profile_email);
                TextView skype = (TextView) findViewById(R.id.profile_skype);
                TextView last_seen = (TextView) findViewById(R.id.profile_last_seen);
                TextView name = (TextView) findViewById(R.id.profile_name);

                if(profile.getPhone() == null)
                    phone.setText("Empty");
                else
                    phone.setText(profile.getPhone());
                if(profile.getEmail() == null)
                    email.setText("Empty");
                else
                    email.setText(profile.getEmail());
                if(profile.getSkype() == null)
                    skype.setText("Empty");
                else
                    skype.setText(profile.getSkype());
                last_seen.setText(profile.getLastVisit().toString());
                name.setText(profile.getFirstName() + " " + profile.getLastName());

                if(profile.getUserImageURL() != null) {
                    new ImageDownloader((ImageView)findViewById(R.id.profile_image_photo)).execute(profile.getUserImageURL());
                }
                if(profile.getBackgroundURL() != null) {
                    new ImageDownloader((ImageView)findViewById(R.id.profile_image_background)).execute(profile.getBackgroundURL());
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

}
