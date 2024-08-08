package learn.mastery.domain;

import learn.mastery.data.GuestRepository;
import learn.mastery.models.Guest;
import learn.mastery.models.Guest;

public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }


    //Helper
    //Checks if a String is shorthand for a state
    private boolean checkValidStateShorthand(String state) {
        final String[] validStateShorthand = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
                "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
                "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH",
                "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI",
                "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};

        for (int i = 0; i < validStateShorthand.length; i++) {
            if (state.equalsIgnoreCase(validStateShorthand[i])) {
                return true;
            }
        }
        return false;
    }
    
    //pass find id
    public Guest findGuestByGuestID(int guestID){
        return repository.findAll().stream()
                .filter(i -> i.getId() == guestID )
                .findFirst()
                .orElse(null);
    }

    //pass find email
    public Guest findGuestByEmail(String guestEmail){
        return repository.findAll().stream()
                .filter(i -> i.getEmail().toLowerCase().trim().equalsIgnoreCase(guestEmail) )
                .findFirst()
                .orElse(null);
    }


    public Result<Guest> validateGuest(Guest Guest) {

        Result<Guest> result = new Result<>();
        //Catch Null
        if (Guest == null) {
            result.addErrorMessage("Guest cannot be null");
            return result;
        }


        //First name
        if (Guest.getFirstName().isEmpty() || Guest.getFirstName() == null) {
            result.addErrorMessage("Guest last name is required");
        } else {
            //Check Name Length
            if (Guest.getFirstName().length() < 2) {
                result.addErrorMessage("Names must be at least 2 letters long.");
            }
            if (Guest.getFirstName().contains(",")) {
                result.addErrorMessage("Illegal character detected in name.");
            }
        }
        //Last Name
        if (Guest.getLastName().isEmpty() || Guest.getLastName() == null) {
            result.addErrorMessage("Guest last name is required");
        } else {
            //Check Name Length
            if (Guest.getLastName().length() < 2) {
                result.addErrorMessage("Names must be at least 2 letters long.");
            }
            if (Guest.getLastName().contains(",")) {
                result.addErrorMessage("Illegal character detected in name.");
            }
        }//Name End


        //email
        if (Guest.getEmail().isEmpty() || Guest.getEmail() == null) {
            result.addErrorMessage("Email is required");
        } else {
            //Check For Errors
            if (Guest.getEmail().length() < 8) {
                result.addErrorMessage("Email length is too short to be a valid email.");
            }
            if (Guest.getEmail().contains(",")) {
                result.addErrorMessage("Illegal character detected in name.");
            }

            if (!Guest.getEmail().contains("@") || !Guest.getEmail().contains(".")) {
                result.addErrorMessage("Invalid email address. (Check placement of \'@\' or \'.\'.");
            } else {
                String[] s = new String[2];
                s[0] = Guest.getEmail().substring(0, Guest.getEmail().indexOf("@"));
                s[1] = Guest.getEmail().substring(Guest.getEmail().indexOf("@") + 1, Guest.getEmail().length());

                if (s[1].contains("@") || !s[1].contains(".")) {
                    result.addErrorMessage("Invalid email address. (Check placement of \'@\' or \'.\'.");
                }
            }
        }//End Email


        //phone
        if (Guest.getPhone().isEmpty() || Guest.getPhone() == null) {
            result.addErrorMessage("Guest phone number is required");
        } else if (Guest.getPhone().length() != 13) {
            result.addErrorMessage("Guest phone number has invalid length");
        } else if (Guest.getPhone().charAt(0) != '(' ||
                Guest.getPhone().charAt(4) != ')' ||
                Guest.getPhone().charAt(5) != ' ') {
            result.addErrorMessage("Invalid phone number format");
        } else {
            String purePhoneNumber = Guest.getPhone().substring(1, 4) + Guest.getPhone().substring(6, 13);
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
        if (Guest.getState().isEmpty() || Guest.getState() == null) {
            result.addErrorMessage("State abbreviation is required");
        } else if (!checkValidStateShorthand(Guest.getState()) || Guest.getState().length() != 2) {
            result.addErrorMessage("Invalid state abbreviation");
        }//End City


        if(!result.isSuccess()){
            result.addErrorMessage("Error: Invalid Guest. See issues above.");
        }

        return result;
    }
    
    
}
