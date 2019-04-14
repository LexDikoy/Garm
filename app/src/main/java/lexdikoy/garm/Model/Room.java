package lexdikoy.garm.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {
    public String roomID;
    public String roomTitle;
    public String roomMembers;
    public String roomMaster;

    public Room(String roomID, String roomTitle, String roomMembers, String roomMaster) {
        this.roomID = roomID;
        this.roomTitle = roomTitle;
        this.roomMembers = roomMembers;
        this.roomMaster = roomMaster;
    }

    public String getRoomMaster() {
        return roomMaster;
    }

    public void setRoomMaster(String roomMaster) {
        this.roomMaster = roomMaster;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getRoomMembers() {
        return roomMembers;
    }

    public void setRoomMembers(String roomMembers) {
        this.roomMembers = roomMembers;
    }
}
