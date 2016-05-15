package intouchteam.intouch.intouchapi;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vova on 15-May-16.
 */
public class InTouchServerComment {

    public static void create(Long eventId, String comment, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("event_id", Collections.singletonList(eventId.toString()));
        requestParameters.put("comment", Collections.singletonList(comment));
        requestParameters.put("method", Collections.singletonList("createComment"));
        InTouchRequest.get(requestParameters, callback);
    }

    public static void get(Long eventId, final InTouchCallback callback) {
        Map<String, List<String>> requestParameters = new HashMap<>();
        requestParameters.put("event_id", Collections.singletonList(eventId.toString()));
        requestParameters.put("method", Collections.singletonList("getEventComments"));
        InTouchRequest.get(requestParameters, callback);
    }
}
