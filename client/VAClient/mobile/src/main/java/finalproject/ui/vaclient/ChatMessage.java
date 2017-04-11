package finalproject.ui.vaclient;

import java.util.Date;

/**
 * Created by Cliff
 */
public class ChatMessage {

    private Date time;
    private long id;
    private boolean isMe;
    private String message;
    private Long userId;
    private String dateTime;
    private String location;

    public ChatMessage(){
    }

    public ChatMessage(boolean isMe, String message){
        this.isMe = isMe;
        this.message = message;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() { return time; }

    public void setTime(Date time) { this.time = time; }

    public boolean getIsme() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }
}
