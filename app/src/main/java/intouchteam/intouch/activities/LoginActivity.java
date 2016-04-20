package intouchteam.intouch.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import intouchteam.intouch.R;
import intouchteam.intouch.Validation;
import intouchteam.intouch.intouchapi.InTouchAuthorization;
import intouchteam.intouch.intouchapi.InTouchCallback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        (findViewById(R.id.button_ok)).setOnClickListener(this);
        ((TextView)findViewById(R.id.textView_handler)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/nautilus_pompilius_regular.ttf"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok: {
                final String username = ((EditText) findViewById(R.id.editText_login)).getText().toString();
                final String pass = ((EditText) findViewById(R.id.editText_password)).getText().toString();
                if (Validation.isValidUsername(username) && Validation.isValidPassword(pass)) {
                    InTouchAuthorization.getInstance().signIn(
                            ((EditText) findViewById(R.id.editText_login)).getText().toString(),
                            ((EditText) findViewById(R.id.editText_password)).getText().toString(),
                            signInCallback());
                } else {
                    if (!Validation.isValidUsername(username))
                        ((MaterialEditText) findViewById(R.id.editText_login)).setError(getResources().getString(R.string.username_error));
                    if (!Validation.isValidPassword(pass))
                        ((MaterialEditText) findViewById(R.id.editText_password)).setError(getResources().getString(R.string.password_error));
                }
            }
        }
    }

    private InTouchCallback signInCallback() {
        return new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject user) {
                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                Intent registrationActivity = new Intent(getBaseContext(), MainActivity.class);
                registrationActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(registrationActivity);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, "Login error:" + error, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
