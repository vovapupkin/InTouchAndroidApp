package intouchteam.intouch.intouchapi.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Date;

public class Event {
    private Long id;
    private Long creatorId;
    private String name;
    private String description;
    private String gps;
    private Date dateTime;
    private String address;
    private Date createDate;
    private Long typeId;
    private String city;
    private String eventImage;

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

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getDateTime() {
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

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setDateTime(Date dateTime) {
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

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage_url() {
        return eventImage;
    }

    public void setImage_url(String image_url) {
        this.eventImage = image_url;
    }

    public static ArrayList<Event> getList(JsonArray jsonArray) {
        ArrayList<Event> events = new ArrayList<>();
        Gson gson = new Gson();
        for(int i = 0; i < jsonArray.size(); i++)
            events.add(gson.fromJson(jsonArray.get(i).getAsJsonObject(), Event.class));
        return events;
    }

    @Override
    public String toString() {
        return name;
    }
}
