package intouchteam.intouch.intouchapi.authorization;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONObject;

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
        requestParameters.put("applicationId", Collections.singletonList("228"));
        requestParameters.put("deviceId", Collections.singletonList("322"));
        Ion.with(context)
                .load(globalURL)
                .setBodyParameters(requestParameters)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception exception, final Response<JsonObject> response) {
                        if (exception == null)
                            handleResponse(response, callback);
                        else
                            callback.onError(exception.getLocalizedMessage());
                    }
                });
    }

    private void handleResponse(Response<JsonObject> response, AuthorizationCallback callback) {
        if(response.getHeaders().code() >= 500)
            callback.onError(response.getHeaders().message());
        else if(response.getHeaders().code() >= 400) callback.onError(response.getHeaders().message());
        else catchError(response, callback);
    }

    private void catchError (Response<JsonObject> response, AuthorizationCallback callback) {
        if (response.getResult() == null)
            callback.onError(response.getHeaders().message());
        else if(response.getResult().has("result")) {
            switch (response.getResult().get("result").getAsString()) {
                case "success":
                    Gson gson = new Gson();
                    User user = gson.fromJson(response.getResult().get("user").getAsString(), User.class);
                    callback.onSuccess(user);
                    break;
                case "error":
                    callback.onError(response.getResult().get("error_type").getAsString());
            }
        }
        else callback.onError(response.getResult().getAsString());
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

    public void socialSignIn(String id, String social, final AuthorizationCallback callback) {
        Map<String, List<String>> requestParameters  = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(id + "_" + social));
        requestParameters.put("password", Collections.singletonList(id));
        inTouchServerRequest("login", requestParameters, callback);
    }

    public void socialSignUp(String id, String firstName, String lastName, String social, final AuthorizationCallback callback) {
        Map<String, List<String>> requestParameters  = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(id + "_" + social));
        requestParameters.put("password", Collections.singletonList(id));
        requestParameters.put("first_name", Collections.singletonList("social"));
        requestParameters.put("last_name", Collections.singletonList("social"));
        inTouchServerRequest("registration", requestParameters, callback);
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
