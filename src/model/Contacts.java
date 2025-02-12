package model;

/**
 * Represents a contact with a unique ID, name, and email.
 */
public class Contacts {
    private int contactId;
    private String contactName;
    private String email;

    /**
     * Constructs a new Contacts object.
     *
     * @param contactId   the unique identifier for the contact
     * @param contactName the name of the contact
     * @param email       the email address of the contact
     */
    public Contacts(int contactId, String contactName, String email) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     * Returns the contact's ID.
     *
     * @return the contact ID
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets the contact's ID.
     *
     * @param contactId the new contact ID
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Returns the contact's name.
     *
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets the contact's name.
     *
     * @param contactName the new contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Returns the contact's email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the contact's email address.
     *
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
}