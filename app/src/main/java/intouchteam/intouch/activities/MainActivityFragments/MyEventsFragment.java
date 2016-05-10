package intouchteam.intouch.activities.MainActivityFragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
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
        refreshEvent();
        ((SwipeRefreshLayout)rootView.findViewById(R.id.events_refresh_layout)).setOnRefreshListener(onRefreshEvent());
        return rootView;
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
                ((SwipeRefreshLayout)rootView.findViewById(R.id.events_refresh_layout)).setRefreshing(false);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(InTouchApi.getContext(), "Cant load events:" + error, Toast.LENGTH_SHORT).show();
                ((SwipeRefreshLayout)rootView.findViewById(R.id.events_refresh_layout)).setRefreshing(false);
            }
        };
    }

    @Override
    public void onResume() {
        ((SwipeRefreshLayout)rootView.findViewById(R.id.events_refresh_layout)).setRefreshing(true);
        refreshEvent();
        super.onResume();
    }

    public void setCreatorFilter(Boolean isCreator) {
        this.isCreator = isCreator;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshEvent() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshEvent();
            }
        };
    }

    public void refreshEvent() {
        if(isCreator)
            InTouchServerEvent.getByCreator(InTouchApi.getProfile().getId(), profileEventsCallback());
        else
            InTouchServerEvent.getByFollowed(InTouchApi.getProfile().getId(), profileEventsCallback());
    }
}
