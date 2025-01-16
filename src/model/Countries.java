package model;

public class Countries {

    private int countryId;
    private String country;

    // Constructor
    public Countries(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    // Getters and Setters
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // Override toString for better output representation
    @Override
    public String toString() {
        return "Country [Country_ID=" + countryId + ", Country=" + country + "]";
    }
}
