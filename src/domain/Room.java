package domain;

public class Room {
    Long id;
    String roomName;
    int nowCount;
    int maxCount;

    public Room(){}

    public Room(String roomName, int nowCount, int maxCount) {
        this.roomName = roomName;
        this.nowCount = nowCount;
        this.maxCount = maxCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getNowCount() {
        return nowCount;
    }

    public void setNowCount(int nowCount) {
        this.nowCount = nowCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
