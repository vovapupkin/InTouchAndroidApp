package intouchteam.intouch.intouchapi;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import intouchteam.intouch.intouchapi.model.User;

public class InTouchApi {

    private static final String globalURL = "http://intouch.mycloud.by/RequestServlet";
    private static final String apiKey = "SHEMODED";
    private static Context context;
    private static InTouchApi instance = new InTouchApi();
    private static User profile = null;

    public static InTouchApi getInstance(Context context) {
        InTouchApi.context = context;
        return instance;
    }

    private InTouchApi() {
        if(InTouchAuthorization.isAuthorize()) {
            Gson gson = new Gson();
            profile = gson.fromJson(PreferenceManager.getDefaultSharedPreferences(InTouchApi.getContext()).getString("profile", null), User.class);
        }
    }

    public static User getProfile() { return profile; }

    public static String getGlobalURL() {
        return globalURL;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static Context getContext() {
        return context;
    }
}
