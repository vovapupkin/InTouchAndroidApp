package intouchteam.intouch.intouchapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import intouchteam.intouch.intouchapi.model.Profile;

public class InTouchServerEvent {

    public static void get(final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("user_id", Collections.singletonList(InTouchApi.getProfile().getId().toString()));
        requestParameters.put("method", Collections.singletonList("getEvents"));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void get(Long userId, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("user_id", Collections.singletonList(userId.toString()));
        requestParameters.put("method", Collections.singletonList("getEvents"));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void create(String name, String description, String gps, String dateTime,
            String address, Long typeId, String city, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("CreateEvent"));
        requestParameters.put("name", Collections.singletonList(name));
        requestParameters.put("description", Collections.singletonList(description));
        requestParameters.put("gps", Collections.singletonList(gps));
        requestParameters.put("date_time", Collections.singletonList(dateTime));
        requestParameters.put("address", Collections.singletonList(address));
        requestParameters.put("type_id", Collections.singletonList(typeId.toString()));
        requestParameters.put("city", Collections.singletonList(city));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void getTypes(InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("getEventTypes"));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void search(String city, String eventName, Long typeId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("searchEvents"));
        if(city != null)
            requestParameters.put("city", Collections.singletonList(city));
        if(eventName != null)
            requestParameters.put("eventName", Collections.singletonList(eventName));
        if(typeId != null)
            requestParameters.put("typeId", Collections.singletonList(typeId.toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void getCreator(Long eventId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("getEventCreator"));
        requestParameters.put("event_id", Collections.singletonList(eventId.toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void getFollowers(Long eventId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("getEventFollowers"));
        requestParameters.put("event_id", Collections.singletonList(eventId.toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void subscribe(Long eventId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("subscribeEvent"));
        requestParameters.put("event_id", Collections.singletonList(eventId.toString()));
        InTouchRequest.get(requestParameters, callback);
    }
}
