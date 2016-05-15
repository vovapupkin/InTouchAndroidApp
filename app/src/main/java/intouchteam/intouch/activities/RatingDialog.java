package intouchteam.intouch.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerEvent;
import intouchteam.intouch.intouchapi.model.Event;
import intouchteam.intouch.intouchapi.model.Mark;

public class RatingDialog extends DialogFragment implements View.OnClickListener{

    Event event;
    View rootView;
    ImageView star[];
    Long mark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        star = new ImageView[5];
        rootView = inflater.inflate(R.layout.rating_dialog, container, false);

        initImageView();
        setOnClickListener();
        setLastMark();

        rootView.findViewById(R.id.star1).setOnClickListener(this);
        getDialog().setTitle("Set rating");

        return rootView;
    }

    private void setLastMark() {
        InTouchServerEvent.getMarks(event.getId(),
                new InTouchCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        Gson gson = new Gson();
                        JsonArray JSONratingList = gson.fromJson(result.get("marks").getAsString(), JsonArray.class);
                        for (int i = 0; i < JSONratingList.size(); i++){
                            Mark m = gson.fromJson(JSONratingList.get(i), Mark.class);
                            if(m.getUserId() == InTouchApi.getProfile().getId()){
                                changeStars(m.getMark());
                                break;
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                }

        );
    }

    private void initImageView() {
        star[0] = (ImageView)rootView.findViewById(R.id.star1);
        star[1] = (ImageView)rootView.findViewById(R.id.star2);
        star[2] = (ImageView)rootView.findViewById(R.id.star3);
        star[3] = (ImageView)rootView.findViewById(R.id.star4);
        star[4] = (ImageView)rootView.findViewById(R.id.star5);
    }

    private void setOnClickListener() {
        for (ImageView item: star){
            item.setOnClickListener(this);
        }
        rootView.findViewById(R.id.apply_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.star1){
            changeStars(1);
        }
        if(v.getId() == R.id.star2){
            changeStars(2);
        }
        if(v.getId() == R.id.star3){
            changeStars(3);
        }
        if(v.getId() == R.id.star4){
            changeStars(4);
        }
        if(v.getId() == R.id.star5){
            changeStars(5);
        }
        if(v.getId() == R.id.apply_button){
            System.out.println();
            System.out.println(mark);
            System.out.println();
            setMark();
        }

    }

    public void changeStars(int rate){
        mark = Long.parseLong(Integer.toString(rate));
        for (int i = 0; i < 5; i++){
            if(i < rate)
                star[i].setImageResource(R.drawable.ic_star_blue_24dp);
            else
                star[i].setImageResource(R.drawable.ic_star_outline_blue_24dp);
        }
    }

    private void setMark() {
        if(mark == null) {
            Toast.makeText(InTouchApi.getContext(), "Choose rate before", Toast.LENGTH_SHORT).show();
            return;
        }
        InTouchServerEvent.setMarks(event.getId(),
                mark,
                new InTouchCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        Toast.makeText(InTouchApi.getContext(), "Success", Toast.LENGTH_SHORT).show();
                        getDialog().cancel();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(InTouchApi.getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setEvent(Event event) {
        this.event = event;
    }


}
