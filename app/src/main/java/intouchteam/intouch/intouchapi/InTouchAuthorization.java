package intouchteam.intouch.intouchapi;

import android.content.Context;
import android.content.SharedPreferences;
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

import intouchteam.intouch.intouchapi.model.User;

public class InTouchAuthorization {

    private static String token = null;
    private static InTouchAuthorization instance = new InTouchAuthorization();

    public static InTouchAuthorization getInstance() {
        return instance;
    }

    private InTouchAuthorization() {
        restoreToken();
    }

    private void inTouchServerRequest(String method, final Map<String, List<String>> requestParameters, final InTouchCallback callback) {

        requestParameters.put("api_key", Collections.singletonList(InTouchApi.getApiKey()));
        requestParameters.put("method", Collections.singletonList(method));
        requestParameters.put("applicationId", Collections.singletonList("228"));
        requestParameters.put("deviceId", Collections.singletonList(RegistrationIntentService.TOKEN));
        Ion.with(InTouchApi.getContext())
                .load(InTouchApi.getGlobalURL())
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

    private void handleResponse(Response<JsonObject> response, InTouchCallback callback) {
        if (response.getHeaders().code() >= 500)
            callback.onError(response.getHeaders().message());
        else if (response.getHeaders().code() >= 400)
            callback.onError(response.getHeaders().message());
        else catchError(response, callback);
    }

    private void catchError(Response<JsonObject> response, InTouchCallback callback) {
        if (response.getResult() == null)
            callback.onError(response.getHeaders().message());
        else if (response.getResult().has("result")) {
            switch (response.getResult().get("result").getAsString()) {
                case "success":
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(response.getResult().get("user").getAsString(), JsonObject.class);
                    setToken(jsonObject.get("token").getAsString());
                    PreferenceManager.getDefaultSharedPreferences(InTouchApi.getContext()).edit().putString("profile", response.getResult().get("user").getAsString()).apply();
                    callback.onSuccess(response.getResult());
                    break;
                case "error":
                    callback.onError(response.getResult().get("error_type").getAsString());
            }
        } else callback.onError(response.getResult().getAsString());
    }

    public void signUp(String username, String password, String firstName, String lastName, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(username));
        requestParameters.put("password", Collections.singletonList(password));
        requestParameters.put("first_name", Collections.singletonList(firstName));
        requestParameters.put("last_name", Collections.singletonList(lastName));
        inTouchServerRequest("registration", requestParameters, callback);
    }

    public void signIn(String username, String password, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(username));
        requestParameters.put("password", Collections.singletonList(password));
        inTouchServerRequest("login", requestParameters, callback);
    }

    public void socialSignIn(String id, String social, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(id + "_" + social));
        requestParameters.put("password", Collections.singletonList(id));
        inTouchServerRequest("login", requestParameters, callback);
    }

    public void socialSignUp(String id, String firstName, String lastName, String social, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(id + "_" + social));
        requestParameters.put("password", Collections.singletonList(id));
        requestParameters.put("first_name", Collections.singletonList("social"));
        requestParameters.put("last_name", Collections.singletonList("social"));
        inTouchServerRequest("registration", requestParameters, callback);
    }

    public static void logOut() {
        PreferenceManager.getDefaultSharedPreferences(InTouchApi.getContext()).edit().remove("token").apply();
        token = null;
    }

    public static boolean isAuthorize() {
        return token != null;
    }

    public static String getToken() {
        return token;
    }

    private static void setToken(String token) {
        PreferenceManager.getDefaultSharedPreferences(InTouchApi.getContext()).edit().putString("token", token).apply();
        InTouchAuthorization.token = token;
    }

    private static void restoreToken() {
        token = PreferenceManager.getDefaultSharedPreferences(InTouchApi.getContext()).getString("token", null);
    }


}
