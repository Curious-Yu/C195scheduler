package model;

/**
 * Represents a country with a unique ID and a name.
 */
public class Countries {
    private int countryId;
    private String country;

    /**
     * Constructs a new Countries object.
     *
     * @param countryId the unique identifier for the country
     * @param country   the name of the country
     */
    public Countries(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    /**
     * Returns the country's unique identifier.
     *
     * @return the country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the country's unique identifier.
     *
     * @param countryId the new country ID
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Returns the name of the country.
     *
     * @return the country name
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the name of the country.
     *
     * @param country the new country name
     */
    public void setCountry(String country) {
        this.country = country;
    }
}