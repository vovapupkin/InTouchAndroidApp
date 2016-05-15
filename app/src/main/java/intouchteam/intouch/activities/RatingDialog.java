package intouchteam.intouch.activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.model.Event;

public class RatingDialog extends DialogFragment {

    Event event;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rating_dialog, container, false);
        getDialog().setTitle("Set rating");
        return rootView;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


}
