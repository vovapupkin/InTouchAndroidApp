package intouchteam.intouch.intouchapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");
        Log.d("MyTag", "message: " + message);
        Toast.makeText(InTouchApi.getContext(), "message", Toast.LENGTH_SHORT).show();
    }
}