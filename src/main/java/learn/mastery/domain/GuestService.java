package learn.mastery.domain;

import learn.mastery.data.GuestRepository;
import learn.mastery.models.Guest;
import org.springframework.stereotype.Service;

@Service
public class GuestService {

    private final GuestRepository repository;

    /**
     * Class constructor.
     */
    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }


    //Helper
    //Checks if a String is shorthand for a state
    /**
     * Returns a boolean based on whether a
     * provided string is found in the stored list of
     * state abbreviations
     *
     * @param state     string value of a state abbreviation
     * @return          if the state string matches the stored values
     */
    private boolean checkValidStateShorthand(String state) {
        final String[] validStateShorthand = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
                "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
                "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH",
                "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI",
                "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};
        for(String vss : validStateShorthand){
            if (state.equalsIgnoreCase(vss)) {
                return true;
            }
        }
        return false;
    }
    
    //pass find id
    /**
     * Returns a specific guest from the repository
     * based on a provided matching integer. Always returns null
     * if there is no match
     *
     * @param guestID the guestID string of the targeted guest object
     * @return null or the targeted guest with matching guestID string
     */
    public Guest findGuestByGuestID(int guestID){
        return repository.findAll().stream()
                .filter(i -> i.getId() == guestID )
                .findFirst()
                .orElse(null);
    }

    //pass find email
    /**
     * Returns a specific guest from the repository
     * based on a provided matching string. Always returns null
     * if there is no match
     *
     * @param guestEmail    the email string of the targeted guest object
     * @return              null or the targeted guest with matching email string
     */
    public Guest findGuestByEmail(String guestEmail){
        return repository.findAll().stream()
                .filter(i -> i.getEmail().toLowerCase().trim().equalsIgnoreCase(guestEmail) )
                .findFirst()
                .orElse(null);
    }

    //Validate
    /**
     * Validates if a guest object's fields all pass
     * the desired checks
     *
     * @param guest  the host object to check
     * @return      a list of potential error messages
     */
    public Result<Guest> validateGuest(Guest guest) {

        Result<Guest> result = new Result<>();
        //Catch Null
        if (guest == null) {
            result.addErrorMessage("Guest cannot be null");
            return result;
        }

        //First name
        if (guest.getFirstName() == null) {
            result.addErrorMessage("Guest last name is required");
        } else {
            //Check Name Length
            if (guest.getFirstName().length() < 2) {
                result.addErrorMessage("Names must be at least 2 letters long.");
            }
            if (guest.getFirstName().contains(",")) {
                result.addErrorMessage("Illegal character detected in name.");
            }
        }
        //Last Name
        if (guest.getLastName() == null) {
            result.addErrorMessage("Guest last name is required");
        } else {
            //Check Name Length
            if (guest.getLastName().length() < 2) {
                result.addErrorMessage("Names must be at least 2 letters long.");
            }
            if (guest.getLastName().contains(",")) {
                result.addErrorMessage("Illegal character detected in name.");
            }
        }//Name End


        //email
        if (guest.getEmail() == null) {
            result.addErrorMessage("Email is required");
        } else {
            //Check For Errors
            if (guest.getEmail().length() < 8) {
                result.addErrorMessage("Email length is too short to be a valid email.");
            }
            if (guest.getEmail().contains(",")) {
                result.addErrorMessage("Illegal character detected in name.");
            }

            if (!guest.getEmail().contains("@") || !guest.getEmail().contains(".")) {
                result.addErrorMessage("Invalid email address. (Check placement of '@' or '.'.");
            } else {
                String[] s = new String[2];
                s[0] = guest.getEmail().substring(0, guest.getEmail().indexOf("@"));
                s[1] = guest.getEmail().substring(guest.getEmail().indexOf("@") + 1);

                if (s[1].contains("@") || !s[1].contains(".")) {
                    result.addErrorMessage("Invalid email address. (Check placement of '@' or '.'.");
                }
            }
        }//End Email


        //phone
        if (guest.getPhone() == null) {
            result.addErrorMessage("Guest phone number is required");
        } else if (guest.getPhone().length() != 13) {
            result.addErrorMessage("Guest phone number has invalid length");
        } else if (guest.getPhone().charAt(0) != '(' ||
                guest.getPhone().charAt(4) != ')' ||
                guest.getPhone().charAt(5) != ' ') {
            result.addErrorMessage("Invalid phone number format");
        } else {
            String purePhoneNumber = guest.getPhone().substring(1, 4) + guest.getPhone().substring(6, 13);
            boolean purePhone = true;
            for (char c : purePhoneNumber.toCharArray()) {
                if (!Character.isDigit(c)) {
                    purePhone = false;
                }
            }//End For

            if (!purePhone) {
                result.addErrorMessage("Phone can only contain numeric values");
            }
        }//End Phone

        //state
        if (guest.getState() == null) {
            result.addErrorMessage("State abbreviation is required");
        } else if (!checkValidStateShorthand(guest.getState()) || guest.getState().length() != 2) {
            result.addErrorMessage("Invalid state abbreviation");
        }//End City


        if(!result.isSuccess()){
            result.addErrorMessage("Error: Invalid Guest. See issues above.");
        }

        return result;
    }
    
    
}
