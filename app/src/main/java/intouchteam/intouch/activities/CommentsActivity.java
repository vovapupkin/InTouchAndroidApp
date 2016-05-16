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
import android.widget.Button;
import android.widget.EditText;
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
    Boolean isRefreshing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        String strEvent = getIntent().getStringExtra("event");
        event = new Gson().fromJson(strEvent, Event.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(event.getName());
        setSupportActionBar(toolbar);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRefreshing)
                    refreshComments();
            }
        });
        refreshComments();
        setOnSendButtonClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            if(!isRefreshing)
                refreshComments();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setOnSendButtonClick() {
        View send = findViewById(R.id.send_button);
        if(send != null)
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = ((EditText) findViewById(R.id.send_comment_text)).getText().toString();
                    ((EditText) findViewById(R.id.send_comment_text)).setText(null);
                    findViewById(R.id.sending).setVisibility(View.VISIBLE);
                    InTouchServerComment.create(event.getId(), s,
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
        isRefreshing = true;
        refreshLayout.setRefreshing(true);
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
            commentsAdapter.setContext(this);
            commentsAdapter.setComments(comments);
            commentsAdapter.setProfiles(profiles);
            ((ListView) findViewById(R.id.comments_list)).setAdapter(commentsAdapter);
            ((ListView) findViewById(R.id.comments_list)).setDivider(null);
            findViewById(R.id.sending).setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);
            isRefreshing = false;
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
