package intouchteam.intouch.intouchapi.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Date;

public class Profile implements java.io.Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private Date registrationDate;
    private Date lastVisit;

    private String skype;
    private String email;
    private String phone;
    private String userImage;
    private String backGround;

    public Profile() {}

    public Profile(String firstName, String lastName, String login, Date registrationDate, Date lastVisit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.registrationDate = registrationDate;
        this.lastVisit = lastVisit;
    }

    public Profile(String firstName, String lastName, String userImage, String login, Date registrationDate, Date lastVisit, String skype) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userImage = userImage;
        this.login = login;
        this.registrationDate = registrationDate;
        this.lastVisit = lastVisit;
        this.skype = skype;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserImageURL() {
        return this.userImage;
    }

    public void setUserImageURL(String image_url) {
        this.userImage = image_url;
    }

    public String getBackgroundURL() {
        return this.backGround;
    }

    public void setBackgroundURL(String image_url) {
        this.backGround = image_url;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getLastVisit() {
        return this.lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }


    public static Profile fromJsonObject(JsonObject jsonObject) {
        Gson gson = new Gson();
        Profile profile = gson.fromJson(jsonObject, Profile.class);
        return profile;
    }

    public static Profile getFromJson(JsonObject result) {
        Profile profile = new Gson().fromJson(result.get("user").getAsString(), Profile.class);
        return profile;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
