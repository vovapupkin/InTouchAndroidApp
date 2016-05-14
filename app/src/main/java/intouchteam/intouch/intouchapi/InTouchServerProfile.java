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

    public static void getUser(Long eventId, InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("getUserById"));
        requestParameters.put("user_id", Collections.singletonList(InTouchApi.getProfile().getId().toString()));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void update(String phone, String email, String skype, String image_url, final InTouchCallback callback){
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("method", Collections.singletonList("updateUser"));
        requestParameters.put("phone", Collections.singletonList(phone));
        requestParameters.put("email", Collections.singletonList(email));
        requestParameters.put("skype", Collections.singletonList(skype));
        requestParameters.put("image_url", Collections.singletonList("image_url"));
        InTouchRequest.get(requestParameters, callback);
    }
}
