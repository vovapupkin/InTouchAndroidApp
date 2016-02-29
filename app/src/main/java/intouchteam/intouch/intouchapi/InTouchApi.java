package intouchteam.intouch.intouchapi;

import android.content.Context;

import intouchteam.intouch.intouchapi.authorization.Authorization;

public class InTouchApi {

    private static final String globalURL = "http://intouch.unicloud.pl/RequestServlet";
    private static final String apiKey = "SHEMODED";
    private Context context;
    private static InTouchApi instance;
    private Authorization authorization;

    public static InTouchApi getInstance(Context context) {
        if(instance == null)
            return instance = new InTouchApi(context);
        return instance;
    }

    private InTouchApi(Context context) {
        this.context = context;
        authorization = new Authorization(apiKey, globalURL, this.context);
    }

    public Authorization getAuthorization() { return authorization; }
}
