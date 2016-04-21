package intouchteam.intouch.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerEvent;

public class EventCreate extends AppCompatActivity {

    Toolbar toolbar;
    ImageView toolbarBackground;
    Calendar calendar = Calendar.getInstance();
    Date date = new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarBackground = (ImageView)findViewById(R.id.img_toolbar);
        setOnDateClickListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            if(isAllFieldCorrect())
                createEvent();
            return true;
        } else if(id == R.id.action_clear) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_create, menu);
        return true;
    }

    private boolean isAllFieldCorrect() {
        boolean isAllCorrect = true;
        MaterialEditText name = ((MaterialEditText) findViewById(R.id.event_name));
        if(name.getText().toString().equals("")) {
            isAllCorrect = false;
            name.setError("Name require");
        }
        MaterialEditText city = ((MaterialEditText) findViewById(R.id.event_city));
        if(city.getText().toString().equals("")) {
            isAllCorrect = false;
            city.setError("City require");
        }
        MaterialEditText address = ((MaterialEditText) findViewById(R.id.event_address));
        if(address.getText().toString().equals("")) {
            isAllCorrect = false;
            address.setError("Address require");
        }
        MaterialEditText evenType = ((MaterialEditText) findViewById(R.id.event_type));
        if(evenType.getText().toString().equals("")) {
            isAllCorrect = false;
            evenType.setError("Event type require");
        }
        MaterialEditText date = ((MaterialEditText) findViewById(R.id.event_date));
        if(date.getText().toString().equals("")) {
            isAllCorrect = false;
            date.setError("Date require");
        }
        return isAllCorrect;
    }

    private void createEvent() {
        MaterialEditText name = ((MaterialEditText) findViewById(R.id.event_name));
        MaterialEditText description = ((MaterialEditText) findViewById(R.id.event_description));
        MaterialEditText city = ((MaterialEditText) findViewById(R.id.event_city));
        MaterialEditText address = ((MaterialEditText) findViewById(R.id.event_address));
        MaterialEditText evenType = ((MaterialEditText) findViewById(R.id.event_type));
        MaterialEditText date = ((MaterialEditText) findViewById(R.id.event_date));
        InTouchServerEvent.create(name.getText().toString(),
                description.getText().toString(),
                "",
                date.getText().toString(),
                address.getText().toString(),
                (long)1,
                city.getText().toString(),
                new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                Toast.makeText(InTouchApi.getContext(), result.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(String error) {
                Toast.makeText(InTouchApi.getContext(), error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showDatePickerDialog() {
        date = new Date();
        final MaterialEditText dateEditText = ((MaterialEditText) findViewById(R.id.event_date));
        DatePickerDialog datePickerDialog = new DatePickerDialog(EventCreate.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.setYear(year);
                date.setMonth(monthOfYear);
                date.setDate(dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                String dateStr = sdf.format(date);
                dateEditText.setText(dateStr);
                TimePickerDialog timePickerDialog  = new TimePickerDialog(EventCreate.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.setHours(hourOfDay);
                        date.setMinutes(minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        String dateStr = sdf.format(date);
                        dateEditText.setText(dateStr);
                    }
                }, calendar.getTime().getHours(), calendar.getTime().getMinutes(), true);
                timePickerDialog.show();
            }
        }, calendar.getTime().getYear(), calendar.getTime().getMonth(), calendar.getTime().getDay());
        datePickerDialog.show();
    }

    private void setOnDateClickListener() {
        MaterialEditText dateEditText = ((MaterialEditText) findViewById(R.id.event_date));
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }
}
