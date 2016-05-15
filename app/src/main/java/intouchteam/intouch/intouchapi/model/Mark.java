package intouchteam.intouch.intouchapi.model;

import java.util.ArrayList;

/**
 * Created by User on 15.05.2016.
 */
public class Mark {

    private Long id;
    private long userId;
    private long eventId;
    private int mark;

    /*
    public void setRating(ArrayList<Integer> ratingList) {
        this.rating = getAverageRating(ratingList).longValue();
    }*/
    public int getMark() {
        return mark;
    }
    public void setMark(int mark) {
        this.mark = mark;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserID(int userId) {
        this.userId = userId;
    }
}
