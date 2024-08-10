package learn.mastery.ui;

import learn.mastery.domain.Result;
import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class View {

    private final ConsoleIO io;

    /**
     * Class constructor specifying console input
     */
    public View(ConsoleIO io) {
        this.io = io;
    }


    /**
     * Returns the enum value of MainMenuOption based on a
     * integer provided by console input. the integer is restricted
     * to a min of 0 and a max of the enum size
     *
     * @return MainMenuOption the selected enum value
     * @see MainMenuOption
     */
    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getMessage());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }


    // display only

    /**
     * Prints out a provided string into the console above a
     * secondary line of the same length composed of the
     * character '='
     *
     * @param title the title to print in console
     */
    public void displayHeader(String title) {
        io.println("");
        io.println(title);
        io.println("=".repeat(title.length()));
    }

    /**
     * Prints out an eror header into the console above a
     * secondary line of the same length composed of the
     * character '='. An error message is printed out
     * below the head
     *
     * @param ex the error message print in console
     */
    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    /**
     * Prints out the pass/fail state of a result
     * and the list of string messages provided.
     *
     * @param success  the pass/fail state
     * @param messages the list of strings to be display
     */
    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    /**
     * Prints out the pass/fail state of a result
     * and the message provided.
     *
     * @param success the pass/fail state
     * @param message the string to be display
     */
    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    /**
     * Returns a LocalDate based on the string
     * provided by console input.
     *
     * @return LocalDate returns a valid LocalDate
     */
    public LocalDate getDate() {
        return io.readLocalDate("Select a date [MM/dd/yyyy]: ");
    }

    /**
     * Prints out a list of reservation objects in a
     * predetermined string format
     *
     * @param reservations a list of reservation type objects
     */
    public void printReservations(List<Reservation> reservations) {
        //Check Empty
        if (reservations == null || reservations.isEmpty()) {
            io.print("No reservations were found for this host\n");
            return;
        }

        for (Reservation reservation : reservations) {
            io.printf("ID: %s \t| %s - %s\t| Cost: $%.2f \t| Guest: %s, %s%n",
                    reservation.getId(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    reservation.getcostOfStay(),
                    reservation.getGuest().getLastName(),
                    reservation.getGuest().getFirstName()
            );
        }

        //Add spacing
        System.out.print("\n");
    }

    /**
     * Returns a string provided by console input.
     * The string is assumed to be a email address
     * found within a host object
     *
     * @return the provided string
     */
    public String getHostEmail() {
        return io.readRequiredString("Please enter the host email: ");
    }

    /**
     * Returns a string provided by console input.
     * The string is assumed to be a email address
     * found within a guest object
     *
     * @return the provided string
     */
    public String getGuestEmail() {
        return io.readRequiredString("Please enter the guest email: ");
    }

    /**
     * Prints out the last name, city, and state of the provided host as a
     * console header. If the host object is null, an error message will
     * be printed as the header instead
     *
     * @param host the host object we wish to view
     */
    public void printHostResults(Host host) {
        if (host == null) {
            displayHeader("Error");
            io.println("Host does not exist");
        } else {
            displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));
        }
    }

    /**
     * Returns a new reservation object containing values obtained by
     * console input and from the provided host and guest objects.
     *
     * @param host  the host object that determines where the reservation will be saved
     * @param guest the guest object that the reservation is associated with
     * @return a new reservation object
     */
    public Reservation createNewReservation(Host host, Guest guest) {

        //Start new reservation
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);

        //Get Dates
        reservation.setStartDate(io.readLocalDate("Start date [MM/dd/yyyy]: "));
        reservation.setEndDate(io.readLocalDate("End date [MM/dd/yyyy]: "));

        //Determine Cost
        reservation.setHost(host);
        reservation.setcostOfStay(reservation.determineCostOfStay());

        //Assign temporary id and return
        reservation.setId(0);
        return reservation;
    }

    /**
     * Prints out the start date, the end date, and the cost stored in
     * the provided reservation object. User is then prompted to provide
     * a string input from console via a loop until a valid input is provided.
     * method will then return a empty or populated result object
     *
     * @param reservation a reservation object to be displayed
     * @return the embedded error messages
     * @see Result
     */
    public Result<Reservation> confirmReservation(Reservation reservation) {
        boolean endLoop = false;
        Result<Reservation> result = new Result<>();
        String choice = "";

        displayHeader("SUMMARY");
        //String Format
        String summary = String.format("Start: %s\nEnd: %s\nTotal Cost: $%.02f",
                reservation.getStartDate(), reservation.getEndDate(), reservation.getcostOfStay());
        io.println(summary);
        //Get Answer
        while (!endLoop) {
            choice = io.readRequiredString("Confirm reservation? [Y/N]: ");
            if (choice.trim().equalsIgnoreCase("y")) {
                endLoop = true;
            }
            if (choice.trim().equalsIgnoreCase("n")) {
                endLoop = true;
                result.addErrorMessage("Reservation was not confirmed");
            }
        }
        return result;
    }

    /**
     * Returns an int provided by user input via console
     *
     * @return the integer provided by console input
     * @see ConsoleIO
     */
    public int getReservationID() {
        return io.readInt("Reservation ID: ");
    }

    /**
     * Updates a reservation's information based on new localDates
     * provided by the user's input. The reservation's dates are updated
     * based on the provided input. The reservation's costOfStay is updated
     * based on the new dates and prices stored in the provided host.
     *
     * @param oldReservation the reservation object being updated
     * @param host           the host object that the reservation is being recorded under
     * @return the updated reservation objected
     */
    public Reservation updateReservationDates(Reservation oldReservation, Host host) {
        Reservation newReservation = new Reservation();
        //Confirm a reservation was submitted
        if (oldReservation == null) {
            return newReservation;
        }

        //Copy info from old to new
        newReservation.setId(oldReservation.getId());
        newReservation.setGuest(oldReservation.getGuest());
        newReservation.setHost(host);
        newReservation.setStartDate(oldReservation.getStartDate());
        newReservation.setEndDate(oldReservation.getEndDate());
        newReservation.setId(oldReservation.getId());

        //Update
        //Get start date
        LocalDate newDate = io.readOptionalLocalDate(String.format("Start date (%s): ", oldReservation.getStartDate()));
        if (newDate != null) {
            newReservation.setStartDate(newDate);
        }

        //Get end date
        newDate = io.readOptionalLocalDate(String.format("End date (%s): ", oldReservation.getEndDate()));
        if (newDate != null) {
            newReservation.setEndDate(newDate);
        }

        //Update cost
        newReservation.setcostOfStay(newReservation.determineCostOfStay());

        return newReservation;
    }


}
