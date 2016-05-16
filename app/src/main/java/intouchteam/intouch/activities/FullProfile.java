package intouchteam.intouch.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.ImageDownloader;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerEvent;
import intouchteam.intouch.intouchapi.InTouchServerProfile;
import intouchteam.intouch.intouchapi.model.Event;
import intouchteam.intouch.intouchapi.model.Mark;
import intouchteam.intouch.intouchapi.model.Profile;

public class FullProfile extends AppCompatActivity {

    Long id;
    Profile profile;
    ArrayList<Event> eventList = new ArrayList<Event>();
    ArrayList<Profile> followersList = new ArrayList<Profile>();
    ArrayList<Profile> followingList = new ArrayList<Profile>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_profile);
        setFollowButton();
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
                profile = new Gson().fromJson(result.get("user").getAsString(), Profile.class);

                InTouchServerEvent.getFollowingUsers(profile.getId(),
                        new InTouchCallback() {
                            @Override
                            public void onSuccess(JsonObject result) {
                                JsonArray jsonArray = new Gson().fromJson(result.get("users").getAsString(), JsonArray.class);
                                for (int i = 0; i < jsonArray.size(); i++){
                                    Gson gson = new Gson();
                                    Profile p = gson.fromJson(jsonArray.get(i), Profile.class);
                                    followingList.add(p);

                                }
                                TextView following = (TextView) findViewById(R.id.following);
                                following.setText(String.valueOf(jsonArray.size()));
                                setFollowingButton();
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                InTouchServerEvent.getFollowersUsers(profile.getId(),
                        new InTouchCallback() {
                            @Override
                            public void onSuccess(JsonObject result) {
                                JsonArray jsonArray = new Gson().fromJson(result.get("users").getAsString(), JsonArray.class);
                                for (int i = 0; i < jsonArray.size(); i++){
                                    Gson gson = new Gson();
                                    Profile p = gson.fromJson(jsonArray.get(i), Profile.class);
                                    followersList.add(p);
                                }
                                TextView followers = (TextView) findViewById(R.id.followers);
                                followers.setText(String.valueOf(jsonArray.size()));
                                setFollowersButton();
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                InTouchServerEvent.getByCreator(profile.getId(), new InTouchCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        JsonArray jsonArray = new Gson().fromJson(result.get("events").getAsString(), JsonArray.class);
                        for (int i = 0; i < jsonArray.size(); i++){
                            Gson gson = new Gson();
                            Event e = gson.fromJson(jsonArray.get(i), Event.class);
                            eventList.add(e);
                        }
                        TextView events = (TextView)findViewById(R.id.events);
                        events.setText(String.valueOf(jsonArray.size()));
                        setEventsButton();
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
                else if (profile.getPhone().isEmpty())
                    phone.setText("Empty");
                else
                    phone.setText(profile.getPhone());

                if(profile.getEmail() == null)
                    email.setText("Empty");
                else if(profile.getEmail().isEmpty())
                    email.setText("Empty");
                else
                    email.setText(profile.getEmail());

                if (profile.getSkype() == null)
                    skype.setText("Empty");
                else if(profile.getSkype().isEmpty())
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
    private void setEventsButton() {
        View view = findViewById(R.id.events_button);
        if(view != null)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eventList.size() != 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FullProfile.this);
                        ArrayAdapter<Event> arrayAdapter = new ArrayAdapter<>(FullProfile.this, R.layout.event_type_dialog_item, eventList);
                        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(FullProfile.this, FullEventActivity.class);
                                intent.putExtra("event", new Gson().toJson(eventList.get(which), Event.class));
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    } else {
                        Toast.makeText(FullProfile.this, "No events", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    private void setFollowersButton() {
        View view = findViewById(R.id.followers_button);
        if(view != null)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (followersList.size() != 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FullProfile.this);
                        ArrayAdapter<Profile> arrayAdapter = new ArrayAdapter<>(FullProfile.this, R.layout.event_type_dialog_item, followersList);
                        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(FullProfile.this, FullProfile.class);
                                intent.putExtra("userId", followersList.get(which).getId());
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    } else {
                        Toast.makeText(FullProfile.this, "No followers", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    private void setFollowingButton() {
        View view = findViewById(R.id.following_button);
        if(view != null)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (followingList.size() != 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FullProfile.this);
                        ArrayAdapter<Profile> arrayAdapter = new ArrayAdapter<>(FullProfile.this, R.layout.event_type_dialog_item, followingList);
                        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(FullProfile.this, FullProfile.class);
                                intent.putExtra("userId", followingList.get(which).getId());
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    } else {
                        Toast.makeText(FullProfile.this, "No following users", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    private void setFollowButton() {
        //getFollowers();
        Toast.makeText(FullProfile.this, "before", Toast.LENGTH_SHORT).show();
        FloatingActionButton floatingActionButton = ((FloatingActionButton)findViewById(R.id.follow));
        if(floatingActionButton != null) {
            Toast.makeText(FullProfile.this, "miu", Toast.LENGTH_SHORT).show();
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_outline_30dp));
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InTouchServerProfile.follow(profile.getLogin(), new InTouchCallback() {
                        @Override
                        public void onSuccess(JsonObject result) {
                            Toast.makeText(FullProfile.this, "Follow success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(FullProfile.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

}
