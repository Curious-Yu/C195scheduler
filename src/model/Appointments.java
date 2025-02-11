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
    // The start and end date/times are stored in UTC.
    private final ObjectProperty<LocalDateTime> startDateTime;
    private final ObjectProperty<LocalDateTime> endDateTime;
    private final IntegerProperty customerId;
    private final IntegerProperty userId;
    private final IntegerProperty contactId;

    /**
     * Constructor to initialize all properties.
     *
     * @param appointmentId the appointment ID
     * @param title the title
     * @param description the description
     * @param location the location
     * @param type the type
     * @param startDateTime the start date/time (in UTC)
     * @param endDateTime the end date/time (in UTC)
     * @param customerId the customer ID
     * @param userId the user ID
     * @param contactId the contact ID
     */
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

    // Property getters for JavaFX bindings
    public IntegerProperty appointmentIdProperty() {
        return appointmentId;
    }
    public StringProperty titleProperty() {
        return title;
    }
    public StringProperty descriptionProperty() {
        return description;
    }
    public StringProperty locationProperty() {
        return location;
    }
    public StringProperty typeProperty() {
        return type;
    }
    public ObjectProperty<LocalDateTime> startDateTimeProperty() {
        return startDateTime;
    }
    public ObjectProperty<LocalDateTime> endDateTimeProperty() {
        return endDateTime;
    }
    public IntegerProperty customerIdProperty() {
        return customerId;
    }
    public IntegerProperty userIdProperty() {
        return userId;
    }
    public IntegerProperty contactIdProperty() {
        return contactId;
    }

    // Traditional getters and setters
    public int getAppointmentId() {
        return appointmentId.get();
    }
    public void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }
    public String getTitle() {
        return title.get();
    }
    public void setTitle(String title) {
        this.title.set(title);
    }
    public String getDescription() {
        return description.get();
    }
    public void setDescription(String description) {
        this.description.set(description);
    }
    public String getLocation() {
        return location.get();
    }
    public void setLocation(String location) {
        this.location.set(location);
    }
    public String getType() {
        return type.get();
    }
    public void setType(String type) {
        this.type.set(type);
    }
    /**
     * Returns the start date/time as stored (UTC).
     * @return the UTC start date/time.
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime.get();
    }
    /**
     * Sets the start date/time (should be in UTC).
     * @param startDateTime the UTC start date/time.
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime.set(startDateTime);
    }
    /**
     * Returns the end date/time as stored (UTC).
     * @return the UTC end date/time.
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime.get();
    }
    /**
     * Sets the end date/time (should be in UTC).
     * @param endDateTime the UTC end date/time.
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime.set(endDateTime);
    }
    public int getCustomerId() {
        return customerId.get();
    }
    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }
    public int getUserId() {
        return userId.get();
    }
    public void setUserId(int userId) {
        this.userId.set(userId);
    }
    public int getContactId() {
        return contactId.get();
    }
    public void setContactId(int contactId) {
        this.contactId.set(contactId);
    }

    // Helper methods for converting UTC times to the local time zone
    /**
     * Converts the stored UTC start date/time to the system's local date/time.
     * @return the local start date/time.
     */
    public LocalDateTime getLocalStartDateTime() {
        LocalDateTime utcDateTime = getStartDateTime();
        if (utcDateTime == null) return null;
        ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault());
        return localZoned.toLocalDateTime();
    }
    /**
     * Converts the stored UTC end date/time to the system's local date/time.
     * @return the local end date/time.
     */
    public LocalDateTime getLocalEndDateTime() {
        LocalDateTime utcDateTime = getEndDateTime();
        if (utcDateTime == null) return null;
        ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault());
        return localZoned.toLocalDateTime();
    }
}
