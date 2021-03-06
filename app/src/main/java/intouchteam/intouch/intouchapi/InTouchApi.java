package intouchteam.intouch.intouchapi;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import intouchteam.intouch.intouchapi.model.Profile;

public class InTouchApi {

    private static final String globalURL = "http://intouch.jelastic.regruhosting.ru/RequestServlet";
    private static final String apiKey = "SHEMODED";
    private static Context context;
    private static InTouchApi instance;
    private static Profile profile = null;

    public static InTouchApi getInstance(Context context) {
        if(instance == null) {
            instance = new InTouchApi(context);
        }
        return instance;
    }

    private InTouchApi(Context context) {
        InTouchApi.context = context;
        getProfile();
    }

    public static Profile getProfile() {
        if(profile == null && InTouchAuthorization.isAuthorize()) {
            Gson gson = new Gson();
            profile = gson.fromJson(PreferenceManager.getDefaultSharedPreferences(InTouchApi.getContext()).getString("profile", null), Profile.class);
        }
        return profile;
    }

    public static String getGlobalURL() {
        return globalURL;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static Context getContext() {
        return context;
    }

    public static void setProfile(Profile profile) {
        InTouchApi.profile = profile;
        PreferenceManager.getDefaultSharedPreferences(InTouchApi.getContext()).edit().putString("profile", new Gson().toJson(profile, Profile.class)).apply();
    }
}
