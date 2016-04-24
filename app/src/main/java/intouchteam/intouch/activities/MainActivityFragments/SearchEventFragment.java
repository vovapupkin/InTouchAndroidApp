package intouchteam.intouch.activities.MainActivityFragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import intouchteam.intouch.R;
import intouchteam.intouch.adapter.MainActivityEventsAdapter;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerEvent;
import intouchteam.intouch.intouchapi.model.Event;
import intouchteam.intouch.intouchapi.model.EventType;

public class SearchEventFragment extends Fragment {

    View rootView;
    EventType selectedEventType = null;
    volatile ArrayList<EventType> eventTypes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_search, container, false);
        downloadEventTypes();
        setOnEventTypeClickListener();
        setOnSearchButtonClickListener();
        return rootView;
    }

    private View.OnClickListener showEventTypeDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventTypes.size() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    ArrayAdapter<EventType> arrayAdapter = new ArrayAdapter<>(rootView.getContext(), R.layout.event_type_dialog_item, eventTypes);
                    builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setType(eventTypes.get(which));
                        }
                    });
                    builder.setCancelable(true);
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            setType(null);
                        }
                    });
                    builder.show();
                }
                else {
                    Toast.makeText(rootView.getContext(), "Wait, loading.", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void setType(EventType type) {
        MaterialEditText typeEditText = ((MaterialEditText) rootView.findViewById(R.id.event_type));
        if(type != null)
            typeEditText.setText(type.getTypeName());
        else
            typeEditText.setText(null);
        selectedEventType = type;
    }

    private void setOnEventTypeClickListener() {
        MaterialEditText typeEditText = ((MaterialEditText) rootView.findViewById(R.id.event_type));
        typeEditText.setOnClickListener(showEventTypeDialog());
    }

    private void downloadEventTypes() {
        InTouchServerEvent.getTypes(new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                Gson gson = new Gson();
                JsonArray eventTypesJsonElements = gson.fromJson(result.get("EventTypes").getAsString(), JsonArray.class);
                for (int i = 0; i < eventTypesJsonElements.size(); i++)
                    eventTypes.add(gson.fromJson(eventTypesJsonElements.get(i).getAsJsonObject(), EventType.class));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(InTouchApi.getContext(), "Cant load event types:" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOnSearchButtonClickListener() {
        rootView.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long id = null;
                if (selectedEventType != null)
                    id = selectedEventType.getId();
                InTouchServerEvent.search(checkSearchField(R.id.event_city),
                        checkSearchField(R.id.event_name), id, onSearchInTouchCallback());
                ((LinearLayout)rootView.findViewById(R.id.events_layout)).removeAllViews();
                rootView.findViewById(R.id.marker_progress).setVisibility(View.VISIBLE);
            }
        });
    }

    private InTouchCallback onSearchInTouchCallback() {
        return new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                JsonArray jsonEvents = new Gson().fromJson(result.get("events").getAsString(), JsonArray.class);
                ArrayList<Event> events = Event.getList(jsonEvents);
                MainActivityEventsAdapter adapter = new MainActivityEventsAdapter(InTouchApi.getContext(), events);
                LinearLayout eventLayout = (LinearLayout)rootView.findViewById(R.id.events_layout);
                for(int i = 0; i < adapter.getCount(); i++)
                    eventLayout.addView(adapter.getView(i, eventLayout, null));
                rootView.findViewById(R.id.marker_progress).setVisibility(View.GONE);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(InTouchApi.getContext(), "Cant load events:" + error, Toast.LENGTH_SHORT).show();
                rootView.findViewById(R.id.marker_progress).setVisibility(View.GONE);
            }
        };
    }

    private String checkSearchField(Integer id) {
        String result = ((MaterialEditText)rootView.findViewById(id)).getText().toString();
        if(result.equals("")) return null;
        else return result;
    }
}
