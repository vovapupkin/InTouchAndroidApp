package intouchteam.intouch.intouchapi;

import android.content.Context;

import intouchteam.intouch.intouchapi.authorization.Authorization;

public class InTouchApi {

    private static final String globalURL = "http://intouch.mycloud.by/RequestServlet";
    private static final String apiKey = "SHEMODED";
    private Context context;
    private static InTouchApi instance;

    public static InTouchApi getInstance(Context context) {
        if(instance == null)
            return instance = new InTouchApi(context);
        return instance;
    }

    private InTouchApi(Context context) {
        this.context = context;
        Authorization.getInstance(apiKey, globalURL, this.context);
    }
}
