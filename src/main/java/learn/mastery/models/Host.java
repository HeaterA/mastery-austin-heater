package learn.mastery.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Host {

    private String id;
    private String lastName;
    private String email;
    private String phone;
    //One location per host
    private String address;
    private String city;
    private String state; //2 letter Abbr
    private String postalCode;
    //Pricing
    private BigDecimal standardRate;
    private BigDecimal weekendRate;

    //Empty Con

    /**
     * Class constructor.
     */
    public Host() {
    }

    //Full Con

    /**
     * Class constructor for a fully populated host object.
     */
    public Host(String id, String lastName, String email, String phone, String address, String city, String state, String postalCode, BigDecimal standardRate, BigDecimal weekendRate) {
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.standardRate = standardRate;
        this.weekendRate = weekendRate;
    }
    //TODO remove if not needed
    /*public Host(String id, String lastName, String email, String phone, String address, String city, String state, String postalCode, int standardRate, int weekendRate) {
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.standardRate = BigDecimal.valueOf( standardRate );
        this.weekendRate = BigDecimal.valueOf( weekendRate );
    }*/

    /**
     * Class constructor for a fully populated host object
     * Converts provided double values into BigDecimals.
     */
    public Host(String id, String lastName, String email, String phone, String address, String city, String state, String postalCode, String standardRate, String weekendRate) {
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.standardRate = BigDecimal.valueOf(Double.valueOf(standardRate));
        this.weekendRate = BigDecimal.valueOf(Double.valueOf(weekendRate));
    }

    //Getters Setters

    /**
     * Gets the string id stored inside the
     * host object used as a unique identifier
     *
     * @return the string used to identify the object
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the string id stored inside the
     * host object used as a unique identifier
     *
     * @param id the string used to identify the object
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the string lastName stored inside the
     * host object
     *
     * @return the lastName string
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the string lastName stored inside the
     * host object
     *
     * @param lastName the string saved as lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the string email stored inside the
     * host object
     *
     * @return the string saved as email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the string email stored inside the
     * host object
     *
     * @param email the string saved as email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the string phone stored inside the
     * host object
     *
     * @return the string saved as phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the string phone stored inside the
     * host object
     *
     * @param phone the string saved as phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the string address stored inside the
     * host object
     *
     * @return the string saved as address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the string address stored inside the
     * host object
     *
     * @param address the string saved as address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the string city stored inside the
     * host object
     *
     * @return string saved as city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the string city stored inside the
     * host object
     *
     * @param city the string saved as city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the string state stored inside the
     * host object
     *
     * @return the string saved as state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the string state stored inside the
     * host object
     *
     * @param state the string saved as state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets the string postalCode stored inside the
     * host object
     *
     * @return the string saved as postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the string postalCode stored inside the
     * host object
     *
     * @param postalCode the string saved as postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets the BigDecimal standardRate stored inside the
     * host object
     *
     * @return the BigDecimal saved as standardRate
     */
    public BigDecimal getStandardRate() {
        return standardRate;
    }

    /**
     * Sets the BigDecimal standardRate stored inside the
     * host object
     *
     * @param standardRate the BigDecimal saved as standardRate
     */
    public void setStandardRate(BigDecimal standardRate) {
        this.standardRate = standardRate;
    }

    /**
     * Gets the BigDecimal weekendRate stored inside the
     * host object
     *
     * @return the BigDecimal saved as weekendRate
     */
    public BigDecimal getWeekendRate() {
        return weekendRate;
    }

    /**
     * Sets the BigDecimal weekendRate stored inside the
     * host object
     *
     * @param weekendRate the BigDecimal saved as weekendRate
     */
    public void setWeekendRate(BigDecimal weekendRate) {
        this.weekendRate = weekendRate;
    }

}
