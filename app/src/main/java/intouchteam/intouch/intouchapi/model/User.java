package intouchteam.intouch.intouchapi.model;

import java.util.Date;

public class User  implements java.io.Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private byte[] userImage;
    private String login;
    private String password;
    private Date registrationDate;
    private Date lastVisit;

    public User() {}

    public User(String firstName, String lastName, String login, String password, Date registrationDate, Date lastVisit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.registrationDate = registrationDate;
        this.lastVisit = lastVisit;
    }

    public User(String firstName, String lastName, byte[] userImage, String login, String password, Date registrationDate, Date lastVisit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userImage = userImage;
        this.login = login;
        this.password = password;
        this.registrationDate = registrationDate;
        this.lastVisit = lastVisit;
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

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getUserImage() {
        return this.userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
