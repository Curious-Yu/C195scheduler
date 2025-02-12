package model;

/**
 * Represents a first-level division.
 * Contains a division ID, division name, and the associated country ID.
 */
public class FirstLevelDivisions {
    private int divisionId;
    private String division;
    private int countryId;

    /**
     * Constructs a new FirstLevelDivisions object.
     *
     * @param divisionId the unique identifier for the division
     * @param division   the name of the division
     * @param countryId  the ID of the country to which the division belongs
     */
    public FirstLevelDivisions(int divisionId, String division, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
    }

    /**
     * Returns the division ID.
     *
     * @return the division ID
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the division ID.
     *
     * @param divisionId the new division ID
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Returns the division name.
     *
     * @return the division name
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets the division name.
     *
     * @param division the new division name
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Returns the country ID.
     *
     * @return the country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the country ID.
     *
     * @param countryId the new country ID
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}