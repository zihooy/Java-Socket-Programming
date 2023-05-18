package domain;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private Room room;
    private User user;
    private String message;
    private LocalDateTime sendDate;

    public Message() {
    }

    public Message(Room room, User user, String message, LocalDateTime sendDate) {
        this.room = room;
        this.user = user;
        this.message = message;
        this.sendDate = sendDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }
}
