package intouchteam.intouch.activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.authorization.AuthorizationCallback;
import intouchteam.intouch.intouchapi.model.User;

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
                InTouchApi.getInstance(getApplicationContext()).getAuthorization().signIn(
                        ((EditText) findViewById(R.id.editText_login)).getText().toString(),
                        ((EditText) findViewById(R.id.editText_password)).getText().toString(),
                        signInCallback());
            }
        }
    }

    private AuthorizationCallback signInCallback() {
        return new AuthorizationCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void inError(String error) {
                Toast.makeText(LoginActivity.this, "Login error:" + error, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
