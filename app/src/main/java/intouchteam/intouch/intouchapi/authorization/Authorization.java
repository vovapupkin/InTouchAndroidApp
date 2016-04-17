package intouchteam.intouch.intouchapi.authorization;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.android.gms.iid.InstanceID;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import intouchteam.intouch.intouchapi.RegistrationIntentService;
import intouchteam.intouch.intouchapi.model.User;

public class Authorization {

    private static String globalURL;
    private static String apiKey;
    private static Context context;
    private static String token = null;
    private static Authorization instance = null;

    public static Authorization getInstance() {
        return instance;
    }

    public static Authorization getInstance(String apiKey, String globalURL, Context context) {
        if (instance == null)
            return instance = new Authorization(apiKey, globalURL, context);
        return instance;
    }

    private Authorization(String apiKey, String globalURL, Context context) {
        this.globalURL = globalURL;
        this.apiKey = apiKey;
        this.context = context;
        restoreToken();
    }

    private void inTouchServerRequest(String method, final Map<String, List<String>> requestParameters, final AuthorizationCallback callback) {

        InstanceID instanceID = InstanceID.getInstance(context);
        token = RegistrationIntentService.TOKEN;

        requestParameters.put("api_key", Collections.singletonList(apiKey));
        requestParameters.put("method", Collections.singletonList(method));
        requestParameters.put("applicationId", Collections.singletonList("228"));
        requestParameters.put("deviceId", Collections.singletonList(token));
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
        if (response.getHeaders().code() >= 500)
            callback.onError(response.getHeaders().message());
        else if (response.getHeaders().code() >= 400)
            callback.onError(response.getHeaders().message());
        else catchError(response, callback);
    }

    private void catchError(Response<JsonObject> response, AuthorizationCallback callback) {
        if (response.getResult() == null)
            callback.onError(response.getHeaders().message());
        else if (response.getResult().has("result")) {
            switch (response.getResult().get("result").getAsString()) {
                case "success":
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(response.getResult().get("user").getAsString(), JsonObject.class);
                    setToken(jsonObject.get("token").getAsString());
                    User user = gson.fromJson(response.getResult().get("user").getAsString(), User.class);
                    callback.onSuccess(user);
                    break;
                case "error":
                    callback.onError(response.getResult().get("error_type").getAsString());
            }
        } else callback.onError(response.getResult().getAsString());
    }

    public void signUp(String username, String password, String firstName, String lastName, final AuthorizationCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(username));
        requestParameters.put("password", Collections.singletonList(password));
        requestParameters.put("first_name", Collections.singletonList(firstName));
        requestParameters.put("last_name", Collections.singletonList(lastName));
        inTouchServerRequest("registration", requestParameters, callback);
    }

    public void signIn(String username, String password, final AuthorizationCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(username));
        requestParameters.put("password", Collections.singletonList(password));
        inTouchServerRequest("login", requestParameters, callback);
    }

    public void socialSignIn(String id, String social, final AuthorizationCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(id + "_" + social));
        requestParameters.put("password", Collections.singletonList(id));
        inTouchServerRequest("login", requestParameters, callback);
    }

    public void socialSignUp(String id, String firstName, String lastName, String social, final AuthorizationCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(id + "_" + social));
        requestParameters.put("password", Collections.singletonList(id));
        requestParameters.put("first_name", Collections.singletonList("social"));
        requestParameters.put("last_name", Collections.singletonList("social"));
        inTouchServerRequest("registration", requestParameters, callback);
    }

    public void logOut() {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("token");
        token = null;
    }

    public static boolean isAuthorize() {
        return token != null;
    }

    public static String getToken() {
        return token;
    }

    private static void setToken(String token) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("token", token).apply();
        Authorization.token = token;
    }

    private static void restoreToken() {
        token = PreferenceManager.getDefaultSharedPreferences(context).getString("token", null);
    }


}
