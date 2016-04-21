package intouchteam.intouch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import intouchteam.intouch.R;
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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainItem = inflater.inflate(R.layout.main_item, parent, false);
        ((TextView)mainItem.findViewById(R.id.title_text)).setText(events.get(position).getName());
        ((TextView)mainItem.findViewById(R.id.title_time)).setText(events.get(position).getDateTime());
        ((TextView)mainItem.findViewById(R.id.content_text)).setText(events.get(position).getDescription());
        return mainItem;
    }
}
