package intouchteam.intouch.intouchapi.model;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Comment implements java.io.Serializable {

    private Long id;
    private long userId;
    private long eventId;
    private String comment;

    public Comment() {
    }

    public Comment(long userId, long eventId, String comment) {
        this.userId = userId;
        this.eventId = eventId;
        this.comment = comment;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    public long getEventId() {
        return this.eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static ArrayList<Comment> getArrayFromJson(JsonObject result) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(result.get("Comments").getAsString(), JsonArray.class);
        ArrayList<Comment> comments = new ArrayList<>();
        for(int i = 0; i < jsonArray.size(); i++)
            comments.add(gson.fromJson(jsonArray.get(i).getAsJsonObject(), Comment.class));
        return comments;
    }
}
