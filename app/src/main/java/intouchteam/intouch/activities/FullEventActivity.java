package intouchteam.intouch.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerEvent;
import intouchteam.intouch.intouchapi.InTouchServerProfile;
import intouchteam.intouch.intouchapi.model.Event;
import intouchteam.intouch.intouchapi.model.EventType;
import intouchteam.intouch.intouchapi.model.Profile;

public class FullEventActivity extends AppCompatActivity {

    Event event;
    ArrayList<Profile> followers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_event);
        Intent intent = getIntent();
        event = new Gson().fromJson(intent.getStringExtra("event"), Event.class);
        setToolbar();
        setEditTextValue();
        setEventTypeName();
        setCreatorName();
        editButtonForCreator();
        setMembersButton();
        setFollowButton();
    }

    private void setEditTextValue() {
        ((TextView) findViewById(R.id.event_name)).setText(event.getName());
        ((TextView) findViewById(R.id.event_description)).setText(event.getDescription());
        ((TextView) findViewById(R.id.event_date)).setText(event.getDateTime().toString());
        ((TextView) findViewById(R.id.event_address)).setText(event.getCity() + " " + event.getAddress());
        ((TextView) findViewById(R.id.event_type)).setText("id:" + event.getTypeId().toString());
        ((TextView) findViewById(R.id.event_contacts)).setText("id:" + event.getCreatorId().toString());
    }

    private void setEventTypeName() {
        InTouchServerEvent.getTypes(new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                Gson gson = new Gson();
                JsonArray eventTypesJsonElements = gson.fromJson(result.get("EventTypes").getAsString(), JsonArray.class);
                ((TextView) findViewById(R.id.event_type))
                        .setText(gson.fromJson(eventTypesJsonElements
                                .get(event.getTypeId().intValue() - 1)
                                .getAsJsonObject(), EventType.class)
                                .getTypeName());
            }

            @Override
            public void onError(String error) {
                Toast.makeText(InTouchApi.getContext(), "Cant load event types:" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCreatorName() {
        InTouchServerProfile.getEventCreator(event.getId(), new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                Profile profile = new Gson().fromJson(result.get("User").getAsString(), Profile.class);
                ((TextView) findViewById(R.id.event_contacts)).setText(profile.getFirstName() + " " + profile.getLastName());
            }

            @Override
            public void onError(String error) {
                Toast.makeText(FullEventActivity.this, "Cant load creator name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editButtonForCreator() {
        if(InTouchApi.getProfile().getId() == event.getCreatorId()) {
            findViewById(R.id.edit_button).setVisibility(View.VISIBLE);
            findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FullEventActivity.this, EventCreateActivity.class);
                    intent.putExtra("event", new Gson().toJson(event, Event.class));
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle(event.getName());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(event.getName());
    }

    private void setMembersButton() {
         findViewById(R.id.members_button).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (followers.size() != 0) {
                     AlertDialog.Builder builder = new AlertDialog.Builder(FullEventActivity.this);
                     ArrayAdapter<Profile> arrayAdapter = new ArrayAdapter<>(FullEventActivity.this, R.layout.event_type_dialog_item, followers);
                     builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             //Open profile;
                         }
                     });
                     builder.show();
                 } else {
                     Toast.makeText(FullEventActivity.this, "No members", Toast.LENGTH_SHORT).show();
                 }
             }
         });
    }

    private void setFollowButton() {
        getFollowers();
        ((Button)findViewById(R.id.follow_button)).setText("Follow");
        findViewById(R.id.follow_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InTouchServerEvent.follow(event.getId(), new InTouchCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        Toast.makeText(FullEventActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        changeFollowButton();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(FullEventActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void changeFollowButton() {
        getFollowers();
        ((Button)findViewById(R.id.follow_button)).setText("Unfollow");
        findViewById(R.id.follow_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InTouchServerEvent.unfollow(event.getId(), new InTouchCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        Toast.makeText(FullEventActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        setFollowButton();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(FullEventActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getFollowers() {
        followers = new ArrayList<>();
        InTouchServerEvent.getFollowers(event.getId(), new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                Gson gson = new Gson();
                JsonArray users = gson.fromJson(result.get("users").getAsString(), JsonArray.class);
                for(int i = 0; i < users.size(); i++) {
                    Profile follower = gson.fromJson(users.get(i), Profile.class);
                    if(InTouchApi.getProfile().getId() == follower.getId())
                        changeFollowButton();
                    followers.add(follower);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(FullEventActivity.this, "Cant load followers:" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}