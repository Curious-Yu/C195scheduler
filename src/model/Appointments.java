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

/**
 * Represents an appointment with details such as title, description, location, type,
 * start and end times (stored in UTC), and associated customer, user, and contact IDs.
 */
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

    /**
     * Constructs an Appointment with the specified details.
     *
     * @param appointmentId the appointment ID
     * @param title         the title of the appointment
     * @param description   the description of the appointment
     * @param location      the location of the appointment
     * @param type          the type of appointment
     * @param startDateTime the start time in UTC
     * @param endDateTime   the end time in UTC
     * @param customerId    the customer ID associated with the appointment
     * @param userId        the user ID associated with the appointment
     * @param contactId     the contact ID associated with the appointment
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

    //------ Property Getters ------

    /**
     * Returns the appointment ID property.
     *
     * @return the appointment ID property
     */
    public IntegerProperty appointmentIdProperty() {
        return appointmentId;
    }

    /**
     * Returns the title property.
     *
     * @return the title property
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     * Returns the description property.
     *
     * @return the description property
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    /**
     * Returns the location property.
     *
     * @return the location property
     */
    public StringProperty locationProperty() {
        return location;
    }

    /**
     * Returns the type property.
     *
     * @return the type property
     */
    public StringProperty typeProperty() {
        return type;
    }

    /**
     * Returns the start time property.
     *
     * @return the start time property
     */
    public ObjectProperty<LocalDateTime> startDateTimeProperty() {
        return startDateTime;
    }

    /**
     * Returns the end time property.
     *
     * @return the end time property
     */
    public ObjectProperty<LocalDateTime> endDateTimeProperty() {
        return endDateTime;
    }

    /**
     * Returns the customer ID property.
     *
     * @return the customer ID property
     */
    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    /**
     * Returns the user ID property.
     *
     * @return the user ID property
     */
    public IntegerProperty userIdProperty() {
        return userId;
    }

    /**
     * Returns the contact ID property.
     *
     * @return the contact ID property
     */
    public IntegerProperty contactIdProperty() {
        return contactId;
    }

    //------ Traditional Getters and Setters ------

    /**
     * Returns the appointment ID.
     *
     * @return the appointment ID
     */
    public int getAppointmentId() {
        return appointmentId.get();
    }

    /**
     * Sets the appointment ID.
     *
     * @param appointmentId the new appointment ID
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }

    /**
     * Returns the title.
     *
     * @return the appointment title
     */
    public String getTitle() {
        return title.get();
    }

    /**
     * Sets the title.
     *
     * @param title the new appointment title
     */
    public void setTitle(String title) {
        this.title.set(title);
    }

    /**
     * Returns the description.
     *
     * @return the appointment description
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Sets the description.
     *
     * @param description the new appointment description
     */
    public void setDescription(String description) {
        this.description.set(description);
    }

    /**
     * Returns the location.
     *
     * @return the appointment location
     */
    public String getLocation() {
        return location.get();
    }

    /**
     * Sets the location.
     *
     * @param location the new appointment location
     */
    public void setLocation(String location) {
        this.location.set(location);
    }

    /**
     * Returns the appointment type.
     *
     * @return the appointment type
     */
    public String getType() {
        return type.get();
    }

    /**
     * Sets the appointment type.
     *
     * @param type the new appointment type
     */
    public void setType(String type) {
        this.type.set(type);
    }

    /**
     * Returns the start time in UTC.
     *
     * @return the start time in UTC
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime.get();
    }

    /**
     * Sets the start time in UTC.
     *
     * @param startDateTime the new start time in UTC
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime.set(startDateTime);
    }

    /**
     * Returns the end time in UTC.
     *
     * @return the end time in UTC
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime.get();
    }

    /**
     * Sets the end time in UTC.
     *
     * @param endDateTime the new end time in UTC
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime.set(endDateTime);
    }

    /**
     * Returns the customer ID.
     *
     * @return the customer ID
     */
    public int getCustomerId() {
        return customerId.get();
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId the new customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public int getUserId() {
        return userId.get();
    }

    /**
     * Sets the user ID.
     *
     * @param userId the new user ID
     */
    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    /**
     * Returns the contact ID.
     *
     * @return the contact ID
     */
    public int getContactId() {
        return contactId.get();
    }

    /**
     * Sets the contact ID.
     *
     * @param contactId the new contact ID
     */
    public void setContactId(int contactId) {
        this.contactId.set(contactId);
    }

    //------ Helper Methods ------

    /**
     * Converts the stored UTC start time to the system's local time.
     *
     * @return the local start time
     */
    public LocalDateTime getLocalStartDateTime() {
        LocalDateTime utcDateTime = getStartDateTime();
        if (utcDateTime == null)
            return null;
        ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault());
        return localZoned.toLocalDateTime();
    }

    /**
     * Converts the stored UTC end time to the system's local time.
     *
     * @return the local end time
     */
    public LocalDateTime getLocalEndDateTime() {
        LocalDateTime utcDateTime = getEndDateTime();
        if (utcDateTime == null)
            return null;
        ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault());
        return localZoned.toLocalDateTime();
    }
}