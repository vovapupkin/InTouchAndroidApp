package intouchteam.intouch.intouchapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import intouchteam.intouch.intouchapi.model.User;

/**
 * Created by vova on 18-Apr-16.
 */
public class InTouchServerEvent {

    private static InTouchServerEvent instance = new InTouchServerEvent();

    public static InTouchServerEvent getInstance() {
        return instance;
    }

    private InTouchServerEvent() {

    }

    private void get(String method, final Map<String, List<String>> requestParameters, final InTouchCallback callback) {
        requestParameters.put("user_id", Collections.singletonList(InTouchApi.getProfile().getId().toString()));
        requestParameters.put("api_key", Collections.singletonList(InTouchApi.getApiKey()));
        requestParameters.put("method", Collections.singletonList("getEvents"));
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
                    User user = gson.fromJson(response.getResult().get("user").getAsString(), User.class);
                    callback.onSuccess(response.getResult());
                    break;
                case "error":
                    callback.onError(response.getResult().get("error_type").getAsString());
            }
        } else callback.onError(response.getResult().getAsString());
    }


}
