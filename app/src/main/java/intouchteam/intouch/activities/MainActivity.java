package intouchteam.intouch.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import intouchteam.intouch.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 892;
    GoogleApiClient googleApi;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.textView_handler)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/nautilus_pompilius_regular.ttf"));
        googleApi = initializeGoogleApiClient();
        setOnClickListeners();
    }

    void setOnClickListeners() {
        (findViewById(R.id.button_registration)).setOnClickListener(this);
        (findViewById(R.id.button_login)).setOnClickListener(this);
        (findViewById(R.id.button_google)).setOnClickListener(this);
        (findViewById(R.id.button_vk)).setOnClickListener(this);
    }
    
    private GoogleApiClient initializeGoogleApiClient() {
        //achtung!!! google hardcode
        return new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build())
                .build();
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
            case R.id.button_google: {
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(googleApi), RC_SIGN_IN);
                break;
            }
            case R.id.button_vk: {
                VKSdk.login(this);
                break;
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Google fail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(getApplicationContext(), res.userId, Toast.LENGTH_SHORT).show();
                //TODO: authorize vk user inTouch
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), "VK fail", Toast.LENGTH_SHORT).show();
            }
        }))
            super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            GoogleSignInAccount user = Auth.GoogleSignInApi.getSignInResultFromIntent(data).getSignInAccount();
            Toast.makeText(getApplicationContext(), user.getId(), Toast.LENGTH_SHORT).show();
            //TODO: authorize google user inTouch
        }
    }
}
