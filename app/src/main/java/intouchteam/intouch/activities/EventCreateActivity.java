package intouchteam.intouch.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerEvent;
import intouchteam.intouch.intouchapi.model.Event;
import intouchteam.intouch.intouchapi.model.EventType;

public class EventCreateActivity extends AppCompatActivity {

    Event event = null;
    Toolbar toolbar;
    ImageView toolbarBackground;
    Calendar calendar = Calendar.getInstance();
    Date date = new Date();
    EventType selectedEventType = null;
    volatile ArrayList<EventType> eventTypes = new ArrayList<>();
    final static int GET_LAT_LNG = 0;
    public static MaterialEditText materialEditTextAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarBackground = (ImageView) findViewById(R.id.img_toolbar);
        downloadEventTypes();
        setOnEventTypeClickListener();
        if (getIntent().getStringExtra("event") != null)
            setEventFields();
        setOnDateClickListener();
        materialEditTextAddress = ((MaterialEditText) findViewById(R.id.event_address));
        ((Button) findViewById(R.id.button_show_map)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra(getString(R.string.address), materialEditTextAddress.getText().toString());
                startActivityForResult(intent, GET_LAT_LNG);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_LAT_LNG && resultCode == RESULT_OK) {
            materialEditTextAddress.setText(data.getStringExtra(getString(R.string.address)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            if (isAllFieldCorrect())
                createEvent();
            return true;
        } else if (id == R.id.action_clear) {
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
        if (name.getText().toString().equals("")) {
            isAllCorrect = false;
            name.setError("Name require");
        }
        MaterialEditText city = ((MaterialEditText) findViewById(R.id.event_city));
        if (city.getText().toString().equals("")) {
            isAllCorrect = false;
            city.setError("City require");
        }

        if (materialEditTextAddress.getText().toString().equals("")) {
            isAllCorrect = false;
            materialEditTextAddress.setError("Address require");
        }
        MaterialEditText evenType = ((MaterialEditText) findViewById(R.id.event_type));
        if (evenType.getText().toString().equals("")) {
            isAllCorrect = false;
            evenType.setError("Event type require");
        }
        MaterialEditText date = ((MaterialEditText) findViewById(R.id.event_date));
        if (date.getText().toString().equals("")) {
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
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        String dateString = sdf.format(date);
        if (event == null)
            InTouchServerEvent.create(name.getText().toString(),
                    description.getText().toString(),
                    "nogps",
                    dateString,
                    address.getText().toString(),
                    selectedEventType.getId(),
                    city.getText().toString(),
                    new InTouchCallback() {
                        @Override
                        public void onSuccess(JsonObject result) {
                            Toast.makeText(EventCreateActivity.this, "Create success", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(InTouchApi.getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    });
        else
            InTouchServerEvent.update(event.getId(),
                    name.getText().toString(),
                    description.getText().toString(),
                    "nogps",
                    dateString,
                    address.getText().toString(),
                    selectedEventType.getId(),
                    city.getText().toString(),
                    new InTouchCallback() {
                        @Override
                        public void onSuccess(JsonObject result) {
                            Toast.makeText(EventCreateActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                            finish();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(EventCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.setYear(year - 1900);
                date.setMonth(monthOfYear);
                date.setDate(dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US);
                String dateStr = sdf.format(date);
                dateEditText.setText(dateStr);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EventCreateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.setHours(hourOfDay);
                        date.setMinutes(minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
                        String dateStr = sdf.format(date);
                        dateEditText.setText(dateStr);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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

    private View.OnClickListener showEventTypeDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventTypes.size() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventCreateActivity.this);
                    ArrayAdapter<EventType> arrayAdapter = new ArrayAdapter<>(EventCreateActivity.this, R.layout.event_type_dialog_item, eventTypes);
                    builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MaterialEditText typeEditText = ((MaterialEditText) findViewById(R.id.event_type));
                            typeEditText.setText(eventTypes.get(which).getTypeName());
                            selectedEventType = eventTypes.get(which);
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(EventCreateActivity.this, "Wait, loading.", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void setOnEventTypeClickListener() {
        MaterialEditText typeEditText = ((MaterialEditText) findViewById(R.id.event_type));
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
                if (event != null) {
                    selectedEventType = eventTypes.get(event.getTypeId().intValue() - 1);
                    MaterialEditText typeEditText = ((MaterialEditText) findViewById(R.id.event_type));
                    typeEditText.setText(eventTypes.get(event.getTypeId().intValue() - 1).getTypeName());
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(InTouchApi.getContext(), "Cant load event types:" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEventFields() {
        event = new Gson().fromJson(getIntent().getStringExtra("event"), Event.class);
        ((MaterialEditText) findViewById(R.id.event_name)).setText(event.getName());
        ((MaterialEditText) findViewById(R.id.event_description)).setText(event.getDescription());
        ((MaterialEditText) findViewById(R.id.event_city)).setText(event.getCity());
        ((MaterialEditText) findViewById(R.id.event_address)).setText(event.getAddress());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
        date = event.getDateTime();
        String dateStr = sdf.format(date);
        ((MaterialEditText) findViewById(R.id.event_date)).setText(dateStr);
    }
}
