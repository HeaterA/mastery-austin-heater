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
    public Host(){
    }
    //Full Con

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

    public Host(String id, String lastName, String email, String phone, String address, String city, String state, String postalCode, int standardRate, int weekendRate) {
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
    }

    public Host(String id, String lastName, String email, String phone, String address, String city, String state, String postalCode, String standardRate, String weekendRate) {
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.standardRate = BigDecimal.valueOf(Double.valueOf( standardRate ));
        this.weekendRate = BigDecimal.valueOf(Double.valueOf( weekendRate ));
    }

    //Getters Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public BigDecimal getStandardRate() {
        return standardRate;
    }

    public void setStandardRate(BigDecimal standardRate) {
        this.standardRate = standardRate;
    }

    public BigDecimal getWeekendRate() {
        return weekendRate;
    }

    public void setWeekendRate(BigDecimal weekendRate) {
        this.weekendRate = weekendRate;
    }


    //add equals

}
