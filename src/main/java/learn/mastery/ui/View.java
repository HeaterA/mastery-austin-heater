package learn.mastery.ui;

import learn.mastery.domain.Result;
import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class View {

    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }


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
    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public LocalDate getDate() {
        return io.readLocalDate("Select a date [MM/dd/yyyy]: ");
    }

    public void printReservations(List<Reservation> reservations) {
        //Check Empty
        if (reservations == null || reservations.isEmpty()) {
            io.print("No reservations were found for this host\n");
            return;
        }

        for (Reservation reservation : reservations) {
            io.printf("ID: %s | %s - %s | Guest: %s, %s | Cost: $%.2f%n",
                    reservation.getId(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    reservation.getGuest().getLastName(), //TODO FIX get guest
                    reservation.getGuest().getFirstName(),
                    reservation.getcostOfStay()
            );
        }

        //Add spacing
        System.out.print("\n");
    }

    public String getHostEmail() {
        return io.readRequiredString("Please enter the host email: ");
    }

    public String getGuestEmail() {
        return io.readRequiredString("Please enter the guest email: ");
    }

    public void printHostResults(Host host) {
        if(host == null){
            displayHeader("Error");
            io.println("Host does not exist");
        }else {
            displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));
        }
    }

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

    public Result<Reservation> confirmReservation(Reservation reservation){
        boolean endLoop = false;
        Result<Reservation> result  = new Result<>();
        String choice = "";

        displayHeader("SUMMARY");
        //String Format
        String summary = String.format("Start: %s\nEnd: %s\nTotal Cost: $%.02f",
                reservation.getStartDate(), reservation.getEndDate(), reservation.getcostOfStay());
        io.println(summary);
       //Get Answer
        while(!endLoop){
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

    public int getReservationID() {
        return io.readInt("Reservation ID: ");
    }

    public Reservation updateReservationDates(Reservation oldReservation, Host host) {
        Reservation newReservation = new Reservation();
        //Confirm a reservation was submitted
        if(oldReservation == null){return newReservation;}

        //Copy info from old to new
        newReservation.setId(oldReservation.getId());
        newReservation.setGuest(oldReservation.getGuest());
        newReservation.setHost(host);
        newReservation.setStartDate(oldReservation.getStartDate());
        newReservation.setEndDate(oldReservation.getEndDate());
        newReservation.setId(oldReservation.getId());

        //Update
        //Get start date
        LocalDate newDate = io.readOptionalLocalDate(String.format("Start date (%s): ",oldReservation.getStartDate()));
        if(newDate != null){
            newReservation.setStartDate(newDate);
        }

        //Get end date
        newDate = io.readOptionalLocalDate(String.format("End date (%s): ",oldReservation.getEndDate()));
        if(newDate != null){
            newReservation.setEndDate(newDate);
        }

        //Update cost
        newReservation.setcostOfStay(newReservation.determineCostOfStay());

        return newReservation;
    }


}
