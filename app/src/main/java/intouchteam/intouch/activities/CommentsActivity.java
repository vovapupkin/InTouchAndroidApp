package intouchteam.intouch.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import intouchteam.intouch.R;
import intouchteam.intouch.adapter.CommentsAdapter;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerComment;
import intouchteam.intouch.intouchapi.InTouchServerProfile;
import intouchteam.intouch.intouchapi.model.Comment;
import intouchteam.intouch.intouchapi.model.Event;
import intouchteam.intouch.intouchapi.model.Profile;

public class CommentsActivity extends AppCompatActivity {

    Event event;
    SwipeRefreshLayout refreshLayout;
    ArrayList<Comment> comments;
    ArrayList<Profile> profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String strEvent = getIntent().getStringExtra("event");
        event = new Gson().fromJson(strEvent, Event.class);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        refreshLayout.setRefreshing(true);
        refreshComments();
        setOnSendButtonClick();
    }

    private void setOnSendButtonClick() {
        View send = findViewById(R.id.send_button);
        if(send != null)
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InTouchServerComment.create(event.getId(),
                            ((MaterialEditText) findViewById(R.id.comment_text)).getText().toString(),
                            new InTouchCallback() {
                                @Override
                                public void onSuccess(JsonObject result) {
                                    refreshLayout.setRefreshing(true);
                                    refreshComments();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(getApplicationContext(), "Can't send message", Toast.LENGTH_SHORT);
                                }
                            });
                }
            });
    }

    private void refreshComments() {
        InTouchServerComment.get(event.getId(), new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                comments = Comment.getArrayFromJson(result);
                profiles = new ArrayList<>();
                getCommentCreator(0);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void getCommentCreator(final int i) {
        if(i == comments.size()) {
            CommentsAdapter commentsAdapter = new CommentsAdapter();
            commentsAdapter.setComments(comments);
            commentsAdapter.setProfiles(profiles);
            ((ListView) findViewById(R.id.comments_list)).setAdapter(commentsAdapter);
            return;
        }
        InTouchServerProfile.getUser(comments.get(i).getUserId(), new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                profiles.add(Profile.getFromJson(result));
                getCommentCreator(i + 1);
            }

            @Override
            public void onError(String error) {
            }
        });
    }
}
