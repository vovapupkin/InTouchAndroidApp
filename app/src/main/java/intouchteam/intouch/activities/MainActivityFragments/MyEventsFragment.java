package intouchteam.intouch.activities.MainActivityFragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import intouchteam.intouch.R;
import intouchteam.intouch.activities.EventCreateActivity;
import intouchteam.intouch.adapter.MainActivityEventsAdapter;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerEvent;
import intouchteam.intouch.intouchapi.model.Event;

public class MyEventsFragment extends Fragment {
    View rootView;
    Boolean isCreator = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_my_events, container, false);
        FloatingActionButton createEventActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        createEventActionButton.setOnClickListener(createEventActionButtonListener(inflater.getContext()));
        if(isCreator)
            InTouchServerEvent.getByCreator(InTouchApi.getProfile().getId(), profileEventsCallback());
        else
            InTouchServerEvent.getByFollowed(InTouchApi.getProfile().getId(), profileEventsCallback());
        return rootView;
    }

    private View.OnClickListener createEventActionButtonListener(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventCreateActivity.class);
                startActivity(intent);
            }
        };
    }

    private InTouchCallback profileEventsCallback() {
        return new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                JsonArray jsonEvents = new Gson().fromJson(result.get("events").getAsString(), JsonArray.class);
                ArrayList<Event> events = Event.getList(jsonEvents);
                MainActivityEventsAdapter adapter = new MainActivityEventsAdapter(InTouchApi.getContext(), events);
                ((ListView)rootView.findViewById(R.id.events_layout)).setAdapter(adapter);
                ((ListView)rootView.findViewById(R.id.events_layout)).setDivider(null);
                rootView.findViewById(R.id.marker_progress).setVisibility(View.GONE);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(InTouchApi.getContext(), "Cant load events:" + error, Toast.LENGTH_SHORT).show();
                rootView.findViewById(R.id.marker_progress).setVisibility(View.GONE);
            }
        };
    }

    @Override
    public void onResume() {
        rootView.findViewById(R.id.marker_progress).setVisibility(View.VISIBLE);
        if(isCreator)
            InTouchServerEvent.getByCreator(InTouchApi.getProfile().getId(), profileEventsCallback());
        else
            InTouchServerEvent.getByFollowed(InTouchApi.getProfile().getId(), profileEventsCallback());
        super.onResume();
    }

    public void setCreatorFilter(Boolean isCreator) {
        this.isCreator = isCreator;
    }
}
