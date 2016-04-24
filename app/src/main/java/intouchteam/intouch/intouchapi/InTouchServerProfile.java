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
}
