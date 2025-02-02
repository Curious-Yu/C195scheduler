package model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Appointments {
    private IntegerProperty appointmentId;
    private StringProperty title;
    private StringProperty description;
    private StringProperty location;
    private StringProperty type;
    private ObjectProperty<LocalDateTime> localStart;
    private ObjectProperty<LocalDateTime> localEnd;
    private IntegerProperty customerId;
    private IntegerProperty userId;
    private IntegerProperty contactId;

    // Constructor
    public Appointments(int appointmentId, String title, String description, String location, String type,
                        LocalDateTime localStart, LocalDateTime localEnd, int customerId, int userId, int contactId) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
        this.type = new SimpleStringProperty(type);
        this.localStart = new SimpleObjectProperty<>(localStart);
        this.localEnd = new SimpleObjectProperty<>(localEnd);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.userId = new SimpleIntegerProperty(userId);
        this.contactId = new SimpleIntegerProperty(contactId);
    }

    // Getters and Setters using JavaFX properties
    public IntegerProperty appointmentIdProperty() {
        return appointmentId;
    }

    public int getAppointmentId() {
        return appointmentId.get();
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty locationProperty() {
        return location;
    }

    public String getLocation() {
        return location.get();
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public StringProperty typeProperty() {
        return type;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public ObjectProperty<LocalDateTime> localStartProperty() {
        return localStart;
    }

    public LocalDateTime getLocalStart() {
        return localStart.get();
    }

    public void setLocalStart(LocalDateTime localStart) {
        this.localStart.set(localStart);
    }

    public ObjectProperty<LocalDateTime> localEndProperty() {
        return localEnd;
    }

    public LocalDateTime getLocalEnd() {
        return localEnd.get();
    }

    public void setLocalEnd(LocalDateTime localEnd) {
        this.localEnd.set(localEnd);
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public int getUserId() {
        return userId.get();
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public IntegerProperty contactIdProperty() {
        return contactId;
    }

    public int getContactId() {
        return contactId.get();
    }

    public void setContactId(int contactId) {
        this.contactId.set(contactId);
    }
}
