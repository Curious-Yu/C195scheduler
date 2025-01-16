package model;

import java.time.LocalDateTime;

public class Appointments {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime localStart;
    private LocalDateTime localEnd;
    private int customerId;
    private int userId;
    private int contactId;

    // Constructor
    public Appointments(int appointmentId, String title, String description, String location, String type,
                       LocalDateTime localStart, LocalDateTime localEnd, int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.localStart = localStart;
        this.localEnd = localEnd;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    // Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getLocalStart() {
        return localStart;
    }

    public void setLocalStart(LocalDateTime localStart) {
        this.localStart = localStart;
    }

    public LocalDateTime getLocalEnd() {
        return localEnd;
    }

    public void setLocalEnd(LocalDateTime localEnd) {
        this.localEnd = localEnd;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

}
