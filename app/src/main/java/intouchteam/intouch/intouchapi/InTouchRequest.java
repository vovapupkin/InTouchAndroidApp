package intouchteam.intouch.intouchapi;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;


public class InTouchRequest {
    private static InTouchRequest ourInstance = new InTouchRequest();

    public static InTouchRequest getInstance() {
        return ourInstance;
    }

    private InTouchRequest() {

    }

    public static void get(final Map<String, List<String>> requestParameters, final InTouchCallback callback) {
        Builders.Any.B ion = Ion.with(InTouchApi.getContext())
                .load("GET", InTouchApi.getGlobalURL());
        if(InTouchAuthorization.getToken() != null)
//TODO: add user token ion.addHeader("Authorization", "Token " + InTouchAuthorization.getToken());
        if(requestParameters != null)
            ion.addQueries(requestParameters);

        ion.asString(Charset.forName("UTF-8"))
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception exception, final Response<String> response) {
                        if (exception == null)
                            handleResponse(response, callback);
                        else {
                            Toast.makeText(InTouchApi.getContext(), "HTTP:get request exception:" + exception.toString(), Toast.LENGTH_SHORT).show();
                            callback.onError(exception.getLocalizedMessage());
                        }
                    }
                });
    }

    private static void handleResponse(Response<String> response, InTouchCallback callback) {
        if (response.getHeaders().code() >= 400)
            callback.onError(response.getHeaders().code() + response.getHeaders().message());
        else catchError(response, callback);
    }

    private static void catchError(Response<String> response, InTouchCallback callback) {
        if (response.getResult() == null)
            callback.onError(response.getHeaders().message() + " no result");
        else {
            Gson gson = new Gson();
            JsonObject result = new Gson().fromJson(response.getResult(), JsonObject.class);
            if (result.has("result")) {
                switch (result.get("result").getAsString()) {
                    case "success":
                        //TODO: say to Vlad do this norm
//                        JsonObject jsonObject = gson.fromJson(response.getResult().get("user").getAsString(), JsonObject.class);
//                        setToken(jsonObject.get("token").getAsString());
//                        User user = gson.fromJson(response.getResult().get("user").getAsString(), User.class);
                        callback.onSuccess(result);
                        break;
                    case "error":
                        callback.onError(result.get("error_type").getAsString());
                }
            } else callback.onError("No result:" + response.getResult());
        }
    }
}
