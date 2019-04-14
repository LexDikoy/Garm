package lexdikoy.garm.Model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String textMessage;
    private String author;
    private long timeMessage;

    public Message(String author, String textMessage) {
        this.textMessage = textMessage;
        this.author = author;
        this.timeMessage = new Date().getTime();
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getTimeMessage() {
        return timeMessage;
    }

    public void setTimeMessage(long timeMessage) {
        this.timeMessage = timeMessage;
    }
}
