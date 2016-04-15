package intouchteam.intouch.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import intouchteam.intouch.R;
import intouchteam.intouch.Validation;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.authorization.AuthorizationCallback;
import intouchteam.intouch.intouchapi.model.User;

public class RegistrationActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ((TextView)findViewById(R.id.textView_handler)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/nautilus_pompilius_regular.ttf"));
        (findViewById(R.id.button_ok)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok: {
                final String username = ((EditText) findViewById(R.id.editText_login)).getText().toString();
                final String pass = ((EditText) findViewById(R.id.editText_password)).getText().toString();
                if(Validation.isValidUsername(username) && Validation.isValidPassword(pass)) {
                    InTouchApi.getInstance(getApplicationContext()).getAuthorization().signUp(
                            ((EditText) findViewById(R.id.editText_login)).getText().toString(),
                            ((EditText) findViewById(R.id.editText_password)).getText().toString(),
                            ((EditText) findViewById(R.id.editText_first_name)).getText().toString(),
                            ((EditText) findViewById(R.id.editText_last_name)).getText().toString(),
                            signUpCallback());
                }
                else {
                    if (!Validation.isValidUsername(username))
                        ((MaterialEditText)findViewById(R.id.editText_login)).setError(getResources().getString(R.string.username_error));
                    if (!Validation.isValidPassword(pass))
                        ((MaterialEditText) findViewById(R.id.editText_password)).setError(getResources().getString(R.string.password_error));
                }
                break;
            }
        }
    }

    private AuthorizationCallback signUpCallback() {
        return new AuthorizationCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(RegistrationActivity.this, "Registration success", Toast.LENGTH_SHORT).show();
                Intent registrationActivity = new Intent(getBaseContext(), HelloActivity.class);
                startActivity(registrationActivity);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(RegistrationActivity.this, "Registration error:" + error, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
