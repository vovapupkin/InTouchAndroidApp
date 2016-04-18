package intouchteam.intouch.intouchapi;

import com.google.gson.JsonElement;

public interface InTouchCallback {
    void onSuccess(JsonElement result);
    void onError(String error);
}
