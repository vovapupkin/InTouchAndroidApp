package intouchteam.intouch.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import intouchteam.intouch.R;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.textView_handler)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/nautilus_pompilius_regular.ttf"));
        (findViewById(R.id.button_registration)).setOnClickListener(this);
        (findViewById(R.id.button_login)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_registration: {
                Intent registrationActivity = new Intent(getBaseContext(), RegistrationActivity.class);
                startActivity(registrationActivity);
                break;
            }
            case R.id.button_login: {
                Intent registrationActivity = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(registrationActivity);
                break;
            }
        }
    }
}
