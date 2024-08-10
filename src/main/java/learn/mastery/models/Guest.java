package learn.mastery.models;

public class Guest {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String state;

    //Constructors

    /**
     * Class constructor
     */
    public Guest() {
    }

    /**
     * Class constructor for a fully populated guest object.
     */
    public Guest(int id, String firstName, String lastName, String email, String phone, String state) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.state = state;
    }

    //Getter Setters

    /**
     * Gets the int id stored inside the
     * guest object used as a unique identifier
     *
     * @return the int used to identify the object
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the int id stored inside the
     * guest object used as a unique identifier
     *
     * @param id the int used to identify the object
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the string firstName stored inside the
     * guest object
     *
     * @return the firstName string
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the string firstName stored inside the
     * guest object
     *
     * @param firstName the string saved as firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the string lastName stored inside the
     * guest object
     *
     * @return the lastName string
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the string lastName stored inside the
     * guest object
     *
     * @param lastName the string saved as lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the string email stored inside the
     * guest object
     *
     * @return the string saved as email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the string email stored inside the
     * guest object
     *
     * @param email the string saved as email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the string phone stored inside the
     * guest object
     *
     * @return the string saved as phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the string phone stored inside the
     * guest object
     *
     * @param phone the string saved as phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the string state stored inside the
     * guest object
     *
     * @return the string saved as state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the string state stored inside the
     * guest object
     *
     * @param state the string saved as state
     */
    public void setState(String state) {
        this.state = state;
    }
}
