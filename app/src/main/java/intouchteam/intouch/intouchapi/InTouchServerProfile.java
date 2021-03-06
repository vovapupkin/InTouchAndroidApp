package intouchteam.intouch.intouchapi;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InTouchServerProfile {

    public static void getEventCreator(Long eventId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("getEventCreator"));
        requestParameters.put("event_id", Collections.singletonList(eventId.toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void follow(String followed_login, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("follow"));
        requestParameters.put("followed_login", Collections.singletonList(followed_login));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void getUser(InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("getUserById"));
        requestParameters.put("user_id", Collections.singletonList(InTouchApi.getProfile().getId().toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void getUser(Long userId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("getUserById"));
        requestParameters.put("user_id", Collections.singletonList(userId.toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void update(String phone, String email, String skype, String image_url,String background, final InTouchCallback callback){
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("updateUser"));
        requestParameters.put("phone", Collections.singletonList(phone));
        requestParameters.put("email", Collections.singletonList(email));
        requestParameters.put("skype", Collections.singletonList(skype));
        requestParameters.put("image_url", Collections.singletonList(image_url));
        requestParameters.put("background", Collections.singletonList(background));
        InTouchRequest.get(requestParameters, callback);
    }
}
