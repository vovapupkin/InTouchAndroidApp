package intouchteam.intouch.activities;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.AndroidCharacter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.ImageDownloader;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerComment;
import intouchteam.intouch.intouchapi.InTouchServerEvent;
import intouchteam.intouch.intouchapi.InTouchServerProfile;
import intouchteam.intouch.intouchapi.model.Comment;
import intouchteam.intouch.intouchapi.model.Event;
import intouchteam.intouch.intouchapi.model.EventType;
import intouchteam.intouch.intouchapi.model.Mark;
import intouchteam.intouch.intouchapi.model.Profile;

public class FullEventActivity extends AppCompatActivity implements View.OnClickListener{

    Event event;
    ArrayList<Profile> followers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_event);
        Intent intent = getIntent();

        (findViewById(R.id.rating_button)).setOnClickListener(this);
        (findViewById(R.id.creator_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullEventActivity.this, FullProfile.class);
                intent.putExtra("userId", event.getCreatorId());
                startActivity(intent);

            }
        });


        event = new Gson().fromJson(intent.getStringExtra("event"), Event.class);

        setEditTextValue();
        setRatingField();
        setEventTypeName();
        setCreatorName();
        editButtonForCreator();
        setMembersButton();
        setFollowButton();
        setBackButtonListener();
        setCommentButtonListener();
        if(event.getImage_url() != null) {
            new ImageDownloader((ImageView)findViewById(R.id.event_logo)).execute(event.getImage_url());
        }
        setCommentsText();
    }

    private double getAverageRating(ArrayList<Mark> ratingList){
        int summa = 0;
        for (Mark item: ratingList){
            summa += item.getMark();
        };
        return (double)summa/ratingList.size();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rating_button){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            RatingDialog ratingDialog = new RatingDialog();
            ratingDialog.setParentActivity(this);
            ratingDialog.setEvent(event);
            ratingDialog.show(ft, "dialog");
        }
    }


    private void setEditTextValue() {
        ((TextView) findViewById(R.id.event_name)).setText(event.getName());
        ((TextView) findViewById(R.id.event_description)).setText(event.getDescription());
        ((TextView) findViewById(R.id.event_city)).setText(event.getCity());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
        String dateStr = sdf.format(event.getDateTime());
        ((TextView) findViewById(R.id.event_date)).setText(dateStr);
        ((TextView) findViewById(R.id.event_address)).setText(event.getCity() + " " + event.getAddress());
        ((TextView) findViewById(R.id.event_type)).setText("id:" + event.getTypeId().toString());
        ((TextView) findViewById(R.id.event_contacts)).setText("id:" + event.getCreatorId().toString());
        ((TextView) findViewById(R.id.event_contacts)).setText("id:" + event.getCreatorId().toString());
        //((TextView) findViewById(R.id.event_rating)).setText("id:" + event.getCreatorId().toString());

    }
    public void setRatingField(){
        InTouchServerEvent.getMarks(event.getId(),
                new InTouchCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        Gson gson = new Gson();
                        ArrayList<Mark> ratingList = new ArrayList<Mark>();
                        JsonArray JSONratingList = gson.fromJson(result.get("marks").getAsString(), JsonArray.class);
                        for (int i = 0; i < JSONratingList.size(); i++) {
                            Mark m = gson.fromJson(JSONratingList.get(i), Mark.class);
                            ratingList.add(m);
                        }
                        if (JSONratingList.size() == 0) {
                            ((TextView) findViewById(R.id.event_rating)).setText("No rating");
                            return;
                        }
                        ((TextView) findViewById(R.id.event_rating)).setText(Double.toString(getAverageRating(ratingList)));
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(InTouchApi.getContext(), "Cant load marks:" + error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setEventTypeName() {
        InTouchServerEvent.getTypes(new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                Gson gson = new Gson();
                JsonArray eventTypesJsonElements = gson.fromJson(result.get("EventTypes").getAsString(), JsonArray.class);
                TextView eventType = ((TextView) findViewById(R.id.event_type));
                if (eventType != null)
                    eventType.setText(gson.fromJson(eventTypesJsonElements
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
                TextView contacts = ((TextView) findViewById(R.id.event_contacts));
                if (contacts != null)
                    contacts.setText(profile.getFirstName() + " " + profile.getLastName());
            }

            @Override
            public void onError(String error) {
                Toast.makeText(FullEventActivity.this, "Cant load creator name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editButtonForCreator() {
        if(InTouchApi.getProfile().getId() == event.getCreatorId()) {
            ImageButton button = (ImageButton) findViewById(R.id.edit_button);
            if (button != null) {
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
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
    }


    private void setMembersButton() {
        View view = findViewById(R.id.members_button);
        if(view != null)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (followers.size() != 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FullEventActivity.this);
                        ArrayAdapter<Profile> arrayAdapter = new ArrayAdapter<>(FullEventActivity.this, R.layout.event_type_dialog_item, followers);
                        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(FullEventActivity.this, FullProfile.class);
                                intent.putExtra("userId", followers.get(which).getId());
                                startActivity(intent);
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
        FloatingActionButton floatingActionButton = ((FloatingActionButton)findViewById(R.id.follow_button));
        if(floatingActionButton != null) {
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_outline_30dp));
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InTouchServerEvent.follow(event.getId(), new InTouchCallback() {
                        @Override
                        public void onSuccess(JsonObject result) {
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
    }

    private void changeFollowButton() {
        getFollowers();
        FloatingActionButton floatingActionButton = ((FloatingActionButton)findViewById(R.id.follow_button));
        if(floatingActionButton != null) {
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_30dp));
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InTouchServerEvent.unfollow(event.getId(), new InTouchCallback() {
                        @Override
                        public void onSuccess(JsonObject result) {
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
    }

    private void getFollowers() {
        InTouchServerEvent.getFollowers(event.getId(), new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                Gson gson = new Gson();
                JsonArray users = gson.fromJson(result.get("users").getAsString(), JsonArray.class);
                followers = new ArrayList<>();
                for (int i = 0; i < users.size(); i++) {
                    Profile follower = gson.fromJson(users.get(i), Profile.class);
                    if (InTouchApi.getProfile().getId() == follower.getId())
                        changeFollowButton();
                    followers.add(follower);
                }
                TextView eventFollowers = ((TextView) findViewById(R.id.event_followers));
                if (eventFollowers != null)
                    eventFollowers.setText(String.valueOf(users.size()));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(FullEventActivity.this, "Cant load followers:" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBackButtonListener() {
        View view = findViewById(R.id.back_button);
        if(view != null)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }

    private void setCommentButtonListener() {
        View view = findViewById(R.id.comment_button);
        if(view != null)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FullEventActivity.this, CommentsActivity.class);
                    intent.putExtra("event", new Gson().toJson(event));
                    startActivity(intent);
                }
            });
    }

    private void setCommentsText() {
        InTouchServerComment.get(event.getId(), new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                ArrayList<Comment> comments = Comment.getArrayFromJson(result);
                ((TextView)findViewById(R.id.event_comments)).setText(String.valueOf(comments.size()));
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}