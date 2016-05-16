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

    public static void getByCreator(Long userId, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("user_id", Collections.singletonList(userId.toString()));
        requestParameters.put("method", Collections.singletonList("getEvents"));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void getByFollowed(Long userId, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("user_id", Collections.singletonList(userId.toString()));
        requestParameters.put("method", Collections.singletonList("getFollowedEvents"));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void create(String name, String description, String gps, String dateTime,
            String address, Long typeId, String city, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("createEvent"));
        requestParameters.put("name", Collections.singletonList(name));
        requestParameters.put("description", Collections.singletonList(description));
        requestParameters.put("gps", Collections.singletonList(gps));
        requestParameters.put("date_time", Collections.singletonList(dateTime));
        requestParameters.put("address", Collections.singletonList(address));
        requestParameters.put("type_id", Collections.singletonList(typeId.toString()));
        requestParameters.put("city", Collections.singletonList(city));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void update(Long eventId, String name, String description, String gps, String dateTime,
                              String address, Long typeId, String city, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("updateEvent"));
        requestParameters.put("name", Collections.singletonList(name));
        requestParameters.put("event_id", Collections.singletonList(eventId.toString()));
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

    public static void getFollowers(Long eventId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("getEventFollowers"));
        requestParameters.put("event_id", Collections.singletonList(eventId.toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void follow(Long eventId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("subscribeEvent"));
        requestParameters.put("event_id", Collections.singletonList(eventId.toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void unfollow(Long eventId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("unfollowEvent"));
        requestParameters.put("event_id", Collections.singletonList(eventId.toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void getMarks(Long eventId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("getEventMarks"));
        requestParameters.put("event_id", Collections.singletonList(eventId.toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void setMarks(Long eventId, Long mark, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("markEvent"));
        requestParameters.put("event_id", Collections.singletonList(eventId.toString()));
        requestParameters.put("mark", Collections.singletonList(mark.toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void GetFollowers(Long userId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("getUsersThatFollow"));
        requestParameters.put("user_id", Collections.singletonList(userId.toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void GetFollowing(Long userId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("getUserFollowers"));
        requestParameters.put("user_id", Collections.singletonList(userId.toString()));
        InTouchRequest.get(requestParameters, callback);
    }

}
