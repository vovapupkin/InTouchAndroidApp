package intouchteam.intouch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.model.Comment;
import intouchteam.intouch.intouchapi.model.Profile;

/**
 * Created by vova on 15-May-16.
 */
public class CommentsAdapter extends BaseAdapter {
    ArrayList<Comment> comments;
    ArrayList<Profile> profiles;
    Context context;

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainItem = inflater.inflate(R.layout.comment_item, parent, false);
        Toast.makeText(context, comments.get(position).getComment(), Toast.LENGTH_SHORT).show();
        ((TextView) mainItem.findViewById(R.id.comment_text)).setText(comments.get(position).getComment());
        ((TextView)mainItem.findViewById(R.id.comment_writer)).setText(profiles.get(position).getFirstName() + " " + (profiles.get(position).getLastName()));
        return mainItem;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void setProfiles(ArrayList<Profile> profiles) {
        this.profiles = profiles;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
