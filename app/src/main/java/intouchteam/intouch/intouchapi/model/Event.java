package intouchteam.intouch.intouchapi.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;

/**
 * Created by vova on 19-Apr-16.
 */
public class Event {
    private Long id;
    private Long creatorId;
    private String name;
    private String description;
    private String gps;
    private String dateTime;
    private String address;
    private String createDate;
    private Long typeId;

    Event() {}

    public Long getId() {
        return id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public String getAddress() {
        return address;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getGps() {
        return gps;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public static ArrayList<Event> getList(JsonArray jsonArray) {
        ArrayList<Event> events = new ArrayList<>();
        Gson gson = new Gson();
        for(int i = 0; i < jsonArray.size(); i++)
            events.add(gson.fromJson(jsonArray.get(i).getAsJsonObject(), Event.class));
        return events;
    }
}
