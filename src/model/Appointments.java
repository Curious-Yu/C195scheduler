package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Appointments {
    private final IntegerProperty appointmentId;
    private final StringProperty title;
    private final StringProperty description;
    private final StringProperty location;
    private final StringProperty type;
    // The start and end times are stored in UTC.
    private final ObjectProperty<LocalDateTime> startDateTime;
    private final ObjectProperty<LocalDateTime> endDateTime;
    private final IntegerProperty customerId;
    private final IntegerProperty userId;
    private final IntegerProperty contactId;

    //------ Constructor ------
    public Appointments(int appointmentId, String title, String description, String location, String type,
                        LocalDateTime startDateTime, LocalDateTime endDateTime,
                        int customerId, int userId, int contactId) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
        this.type = new SimpleStringProperty(type);
        this.startDateTime = new SimpleObjectProperty<>(startDateTime);
        this.endDateTime = new SimpleObjectProperty<>(endDateTime);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.userId = new SimpleIntegerProperty(userId);
        this.contactId = new SimpleIntegerProperty(contactId);
    }

    //------ Property Getters ------
    public IntegerProperty appointmentIdProperty() { return appointmentId; }
    public StringProperty titleProperty() { return title; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty locationProperty() { return location; }
    public StringProperty typeProperty() { return type; }
    public ObjectProperty<LocalDateTime> startDateTimeProperty() { return startDateTime; }
    public ObjectProperty<LocalDateTime> endDateTimeProperty() { return endDateTime; }
    public IntegerProperty customerIdProperty() { return customerId; }
    public IntegerProperty userIdProperty() { return userId; }
    public IntegerProperty contactIdProperty() { return contactId; }

    //------ Traditional Getters and Setters ------
    public int getAppointmentId() { return appointmentId.get(); }
    public void setAppointmentId(int appointmentId) { this.appointmentId.set(appointmentId); }

    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }

    public String getType() { return type.get(); }
    public void setType(String type) { this.type.set(type); }

    public LocalDateTime getStartDateTime() { return startDateTime.get(); }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime.set(startDateTime); }

    public LocalDateTime getEndDateTime() { return endDateTime.get(); }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime.set(endDateTime); }

    public int getCustomerId() { return customerId.get(); }
    public void setCustomerId(int customerId) { this.customerId.set(customerId); }

    public int getUserId() { return userId.get(); }
    public void setUserId(int userId) { this.userId.set(userId); }

    public int getContactId() { return contactId.get(); }
    public void setContactId(int contactId) { this.contactId.set(contactId); }

    //------ Helper Methods ------
    // Converts stored UTC start time to local time.
    public LocalDateTime getLocalStartDateTime() {
        LocalDateTime utcDateTime = getStartDateTime();
        if (utcDateTime == null) return null;
        ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault());
        return localZoned.toLocalDateTime();
    }

    // Converts stored UTC end time to local time.
    public LocalDateTime getLocalEndDateTime() {
        LocalDateTime utcDateTime = getEndDateTime();
        if (utcDateTime == null) return null;
        ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault());
        return localZoned.toLocalDateTime();
    }
}