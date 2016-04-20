package intouchteam.intouch.intouchapi;

import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import intouchteam.intouch.intouchapi.model.Profile;

public class InTouchAuthorization {

    private static String token = null;
    private static InTouchAuthorization instance = new InTouchAuthorization();

    public static InTouchAuthorization getInstance() {
        return instance;
    }

    private InTouchAuthorization() {
        restoreToken();
    }

    private void inTouchAuthorizationRequest(String method, final Map<String, List<String>> requestParameters, final InTouchCallback callback) {
        requestParameters.put("method", Collections.singletonList(method));
        requestParameters.put("applicationId", Collections.singletonList("228"));
        requestParameters.put("deviceId", Collections.singletonList(RegistrationIntentService.TOKEN));
        InTouchRequest.get(requestParameters, new InTouchCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                JsonObject jsonObject = result.get("user").getAsJsonObject();
                setToken(jsonObject.get("token").getAsString());
                PreferenceManager.getDefaultSharedPreferences(InTouchApi.getContext()).edit().putString("profile", jsonObject.getAsString()).apply();
                InTouchApi.setProfile(new Gson().fromJson(jsonObject, Profile.class));
                callback.onSuccess(result);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void signUp(String username, String password, String firstName, String lastName, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(username));
        requestParameters.put("password", Collections.singletonList(password));
        requestParameters.put("first_name", Collections.singletonList(firstName));
        requestParameters.put("last_name", Collections.singletonList(lastName));
        inTouchAuthorizationRequest("registration", requestParameters, callback);
    }

    public void signIn(String username, String password, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(username));
        requestParameters.put("password", Collections.singletonList(password));
        inTouchAuthorizationRequest("login", requestParameters, callback);
    }

    public void socialSignIn(String id, String social, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(id + "_" + social));
        requestParameters.put("password", Collections.singletonList(id));
        inTouchAuthorizationRequest("login", requestParameters, callback);
    }

    public void socialSignUp(String id, String firstName, String lastName, String social, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("login", Collections.singletonList(id + "_" + social));
        requestParameters.put("password", Collections.singletonList(id));
        requestParameters.put("first_name", Collections.singletonList("social"));
        requestParameters.put("last_name", Collections.singletonList("social"));
        inTouchAuthorizationRequest("registration", requestParameters, callback);
    }

    public static void logOut() {
        PreferenceManager.getDefaultSharedPreferences(InTouchApi.getContext()).edit().remove("token").apply();
        token = null;
    }

    public static boolean isAuthorize() {
        if(token == null)
            restoreToken();
        return token != null;
    }

    public static String getToken() {
        if(token == null)
            restoreToken();
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
