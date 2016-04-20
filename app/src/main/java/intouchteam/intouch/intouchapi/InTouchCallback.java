package intouchteam.intouch.intouchapi;

import com.google.gson.JsonObject;

public interface InTouchCallback {
    void onSuccess(JsonObject result);
    void onError(String error);
}
