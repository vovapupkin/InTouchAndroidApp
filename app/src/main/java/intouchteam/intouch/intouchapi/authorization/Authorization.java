package intouchteam.intouch.intouchapi.authorization;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import intouchteam.intouch.intouchapi.model.User;

public class Authorization {

    private String globalURL;
    private String apiKey;
    private Context context;
    private int token = 0;

    public Authorization(String apiKey, String globalURL, Context context) {
        this.globalURL = globalURL;
        this.apiKey = apiKey;
        this.context = context;
        restoreToken();
    }

    private void inTouchServerRequest(String method, final Map<String, List<String>> requestParameters, final AuthorizationCallback callback) {
        requestParameters.put("api_key", Collections.singletonList(apiKey));
        requestParameters.put("method", Collections.singletonList(method));
        Ion.with(context).
                load(globalURL).
                setBodyParameters(requestParameters).
                asJsonObject().
                setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null) {
                            String res = result.get("result").getAsString();
                            switch (res) {
                                case "success":
                                    Gson gson = new Gson();
                                    User user = new User();
                                    user.setFirstName("sdfsd");
                                    user.setId((long) 423423);
                                    String str = gson.toJson(user, User.class).toString();
                                    //setToken(result.get("session_id").getAsInt());

                                    //callback.onSuccess((new User()));
                                    break;
                                case "error":
                                    callback.inError(result.get("error type").toString());
                                    break;
                                default:
                                    callback.inError("can't parse JSON");
                                    break;
                            }
                        }
                        else
                            callback.inError("Ion FutureCallback Exception ");
                    }
                });
    }

    public void signUp(String username, String password, String firstName, String lastName, final AuthorizationCallback callback) {
        Map<String, List<String>> requestParameters  = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(username));
        requestParameters.put("password", Collections.singletonList(password));
        requestParameters.put("first_name", Collections.singletonList(firstName));
        requestParameters.put("last_name", Collections.singletonList(lastName));
        inTouchServerRequest("registration", requestParameters, callback);
    }

    public void signIn(String username, String password, final AuthorizationCallback callback) {
        Map<String, List<String>> requestParameters  = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(username));
        requestParameters.put("password", Collections.singletonList(password));
        inTouchServerRequest("login", requestParameters, callback);
    }

    public void logOut() {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("token");
        token = 0;
    }

    public boolean isAuthorize() {
        return token != 0;
    }

    public int getToken() {
        return token;
    }

    private void setToken(int token) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("token", token);
        this.token = token;
    }

    private void restoreToken() {
        token = PreferenceManager.getDefaultSharedPreferences(context).getInt("token", 0);
    }
}
