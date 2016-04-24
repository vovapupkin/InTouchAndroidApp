package intouchteam.intouch.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import intouchteam.intouch.R;
import intouchteam.intouch.activities.FullEventActivity;
import intouchteam.intouch.intouchapi.model.Event;


public class MainActivityEventsAdapter extends BaseAdapter {
    ArrayList<Event> events;
    Context context;

    public MainActivityEventsAdapter(Context context, ArrayList<Event> events) {
        this.events = events;
        this.context = context;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Event event = events.get(getCount() - position - 1);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainItem = inflater.inflate(R.layout.my_events_item, parent, false);
        ((TextView)mainItem.findViewById(R.id.title_text)).setText(event.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        String date = sdf.format(event.getDateTime());
        ((TextView)mainItem.findViewById(R.id.title_time)).setText(date);
        ((TextView)mainItem.findViewById(R.id.content_text)).setText(event.getDescription());
        mainItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FullEventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("event", new Gson().toJson(event, Event.class));
                v.getContext().startActivity(intent);
            }
        });
        return mainItem;
    }
}
