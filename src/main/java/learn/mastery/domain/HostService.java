package learn.mastery.domain;


import learn.mastery.data.HostRepository;
import learn.mastery.models.Host;
import org.springframework.stereotype.Service;

@Service
public class HostService {

    private final HostRepository repository;

    /**
     * Class constructor.
     */
    public HostService(HostRepository repository) {
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

        for (int i = 0; i < validStateShorthand.length; i++) {
            if (state.equalsIgnoreCase(validStateShorthand[i])) {
                return true;
            }
        }
        return false;
    }

    //Find by id
    /**
     * Returns a specific host from the repository
     * based on a provided matching string. Always returns null
     * if there is no match
     *
     * @param hostID the hostID string of the targeted host object
     * @return null or the targeted host with matching hostID string
     */
    public Host findHostByHostId(String hostID){
        return repository.findAll().stream()
                .filter(i -> i.getId().equals(hostID) )
                .findFirst()
                .orElse(null);
    }

    //Find by email
    /**
     * Returns a specific host from the repository
     * based on a provided matching string. Always returns null
     * if there is no match
     *
     * @param email     the email string of the targeted host object
     * @return          null or the targeted host with matching email string
     */
    public Host findHostByEmail(String email){
        return repository.findAll().stream()
                .filter(i -> i.getEmail().toLowerCase().trim().equalsIgnoreCase(email) )
                .findFirst()
                .orElse(null);
    }

    //Dont Create

    //validate
    /**
     * Validates if a host object's fields all pass
     * the desired checks
     *
     * @param host  the host object to check
     * @return      a list of potential error messages
     */
    public Result<Host> validateHost(Host host) {

        Result<Host> result = new Result<>();

        //Catch Null
        if (host == null) {
            result.addErrorMessage("Host cannot be null");
            return result;
        }

        //Last Name
        if (host.getLastName().isEmpty() || host.getLastName() == null) {
            result.addErrorMessage("Host last name is required");
        } else {
            //Check Name Length
            if (host.getLastName().length() < 2) {
                result.addErrorMessage("Names must be at least 2 letters long.");
            }
            if (host.getLastName().contains(",")) {
                result.addErrorMessage("Illegal character detected in name.");
            }
        }//Name End


        //email
        if (host.getEmail().isEmpty() || host.getEmail() == null) {
            result.addErrorMessage("Email is required");
        } else {
            //Check For Errors
            if (host.getEmail().length() < 8) {
                result.addErrorMessage("Email length is too short to be a valid email.");
            }
            if (host.getEmail().contains(",")) {
                result.addErrorMessage("Illegal character detected in name.");
            }

            if (!host.getEmail().contains("@") || !host.getEmail().contains(".")) {
                result.addErrorMessage("Invalid email address. (Check placement of \'@\' or \'.\'.");
            } else {
                String[] s = new String[2];
                s[0] = host.getEmail().substring(0, host.getEmail().indexOf("@"));
                s[1] = host.getEmail().substring(host.getEmail().indexOf("@") + 1, host.getEmail().length());

                if (s[1].contains("@") || !s[1].contains(".")) {
                    result.addErrorMessage("Invalid email address. (Check placement of \'@\' or \'.\'.");
                }
            }
        }//End Email


        //phone
        //(208) 6604815
        if (host.getPhone().isEmpty() || host.getPhone() == null) {
            result.addErrorMessage("Host phone number is required");
        } else if (host.getPhone().length() != 13) {
            result.addErrorMessage("Host phone number has invalid length");
        } else if (host.getPhone().charAt(0) != '(' ||
                host.getPhone().charAt(4) != ')' ||
                host.getPhone().charAt(5) != ' ') {
            result.addErrorMessage("Invalid phone number format");
        } else {
            String purePhoneNumber = host.getPhone().substring(1, 4) + host.getPhone().substring(6, 13);
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

        //address
        if (host.getAddress().isEmpty() || host.getAddress() == null) {
            result.addErrorMessage("Address is required");
        } else {
            if (host.getAddress().length() < 8) {
                result.addErrorMessage("Address is too short");
            }
        }

        //city
        //address
        if (host.getAddress().isEmpty() || host.getAddress() == null) {
            result.addErrorMessage("City is required");
        } else {
            if (host.getAddress().length() < 2) {
                result.addErrorMessage("City name is too short");
            }
        }//End City


        //state
        if (host.getState().isEmpty() || host.getState() == null) {
            result.addErrorMessage("State abbreviation is required");
        } else if (!checkValidStateShorthand(host.getState()) || host.getState().length() != 2) {
            result.addErrorMessage("Invalid state abbreviation");
        }//End City


        //postalCode
        if (host.getPostalCode() == null || host.getPostalCode().isEmpty()) {
            result.addErrorMessage("Postal code is required.");
        } else {
            if (host.getPostalCode().length() != 5) {
                result.addErrorMessage("Invalid postal code format. Use 5 digit code.");
            } else {
                boolean pureNumeric = true;
                for (char c : host.getPostalCode().toCharArray()) {
                    if (!Character.isDigit(c)) {
                        pureNumeric = false;
                    }
                }//End For
                if (!pureNumeric) {
                    result.addErrorMessage("Invalid postal code format. Use 5 digit code.");
                }
            }//Postal End
        }

        //standardRate
        if (host.getStandardRate() == null) {
            result.addErrorMessage("Standard rate is required");
        } else if (host.getStandardRate().doubleValue() <= 0) {
            result.addErrorMessage("Standard rate must be higher than 0");
        }//Standard


        //weekendRate
        if (host.getWeekendRate() == null) {
            result.addErrorMessage("Weekend rate is required");
        } else if (host.getWeekendRate().doubleValue() <= 0) {
            result.addErrorMessage("Weekend rate must be higher than 0");
        }//Weekend


        //Check for error messages and return
        if(!result.isSuccess()){
            result.addErrorMessage("Error: Invalid Host. See issues above.");
        }
        return result;
    }

}
