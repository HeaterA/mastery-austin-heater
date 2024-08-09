package learn.mastery.ui;

import learn.mastery.data.DataException;
import learn.mastery.domain.GuestService;
import learn.mastery.domain.HostService;
import learn.mastery.domain.ReservationService;
import learn.mastery.domain.Result;
import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


//@Component
public class Controller {

    private final View view;
    private HostService hostService;
    private GuestService guestService;
    private ReservationService reservationService;



    //Temp Constructor for General Outline
    public Controller(HostService hostService, GuestService guestService, ReservationService reservationService, View view) {
        this.hostService = hostService;
        this.guestService = guestService;
        this.reservationService = reservationService;
        this.view = view;
    }


    public void run() {
        view.displayHeader("Welcome to [title]");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS:
                    System.out.println("ADD VIEW RESERVATIONS");
                    viewReservations();
                    break;
                case MAKE_RESERVATION:
                    System.out.println("ADD MAKE RESERVATION");
                    addReservation();
                    break;
                case EDIT_RESERVATION:
                    System.out.println("ADD EDIT RESERVATION");
                    editReservations();
                    break;
                case CANCEL_RESERVATION:
                    System.out.println("ADD CANCEL RESERVATION");
                    deleteReservations();
                    break;

            }
        } while (option != MainMenuOption.EXIT);
    }

    //View TODO clean
    private void viewReservations() {
        view.displayHeader("VIEW RESERVATIONS FOR HOST");
        //Get host
        Host host = hostService.findHostByEmail(view.getHostEmail());
        host = hostService.findHostByEmail("jsuch3x@unblog.fr"); //TODO remove test line
        host = hostService.findHostByEmail("ryoselevitch78@free.fr"); //TODO remove test line

        //Print Reservation List
        //If host is found, print header and results
        view.printHostResults(host);
        //Confirm guests & return list
        List<Reservation> reservations = identifyGuestsInReservations(reservationService.findAllReservationsForHost(host));
        //printlist
        view.printReservations(reservations);
    }

    //Add TODO clean
    private void addReservation() throws DataException {
        //Get host
        Host host = hostService.findHostByEmail(view.getHostEmail());
        host = hostService.findHostByEmail("jsuch3x@unblog.fr"); //TODO remove test line
        host = hostService.findHostByEmail("ryoselevitch78@free.fr"); //TODO remove test line

        //get guest
        Guest guest = guestService.findGuestByEmail("view.getGuestEmail()");
        guest = guestService.findGuestByEmail("iganter9@privacy.gov.au"); //TODO remove test line
        guest = guestService.findGuestByEmail("coluwatoyinf@hubpages.com");//TODO remove test line

        //Print Reservation List
        //If host is found, print header and results
        view.printHostResults(host);
        //Confirm guests & return list
        List<Reservation> reservations = identifyGuestsInReservations(reservationService.findAllReservationsForHost(host));
        reservations = reservationService.filterUpcomingReservations(reservations);
        //Print the filtered list
        view.printReservations(reservations);

        //Return to menu if null host
        if (host == null) {return;}

        //Create new reservation in view, then pass to service
        Reservation reservation = view.createNewReservation(host, guest);
        //Check that  new reservation is valid
        Result<Reservation> result = reservationService.checkReservation(reservation);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
            return;
        }

        //Ask to confirm addition of new reservation
        result = view.confirmReservation(reservation);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
            return;
        }
        //Add new reservation to repository
        result = reservationService.addReservationToFile(reservation);

        //Confirm reservation was added
        String successMessage = String.format("Reservation %s created.", result.getPayload().getId());
        view.displayStatus(true, successMessage);
    }

    //Edit TODO clean
    private void editReservations() throws DataException {
        view.displayHeader("EDIT A RESERVATION");

        //Get host
        Host host = hostService.findHostByEmail(view.getHostEmail());
        host = hostService.findHostByEmail("jsuch3x@unblog.fr"); //TODO remove test line
        host = hostService.findHostByEmail("ryoselevitch78@free.fr"); //TODO remove test line

        //get guest
        Guest guest = guestService.findGuestByEmail(view.getGuestEmail());
        guest = guestService.findGuestByEmail("iganter9@privacy.gov.au"); //TODO remove test line

        //Return to menu if null host
        if (host == null) {
            view.displayStatus(false, "Host not found");
            return;}

        //Print Reservation List
        //If host is found, print header and results
        view.printHostResults(host);
        //Confirm guests & return list
        List<Reservation> reservations = identifyGuestsInReservations(reservationService.findAllReservationsForHost(host));
        reservations = reservationService.filterReservationsByGuest(reservations, guest);
        //Print the filtered list
        view.printReservations(reservations);

        //Return to menu if null results
        if (reservations.isEmpty()) {
            view.displayStatus(false, "No reservations found for this guest");
            return;}

        //Request id and return the target reservation
        Reservation targetReservation = reservationService.findReservationById(reservations, view.getReservationID());
        if (targetReservation == null) { //!result.isSuccess()) {
            view.displayStatus(false, "Invalid ID selected");
            return;
        }

        //Get updated dates
        Reservation updatedReservation = view.updateReservationDates(targetReservation, host);

        //Check that  new reservation is valid
        Result<Reservation> result = reservationService.checkReservation(updatedReservation);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
            return;
        }

        //Ask to confirm addition of new reservation
        result = view.confirmReservation(updatedReservation);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
            return;
        }

        //Add update the reservation in repository
        result = reservationService.updateReservation(updatedReservation);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
            return;
        }

        //Print Success
        String successMessage = String.format("Reservation %s was updated.", updatedReservation.getId());
        view.displayStatus(true, successMessage);
    }

    //Delete TODO
    private void deleteReservations() throws DataException {
        view.displayHeader("DELETE A RESERVATION");

        //Get host
        Host host = hostService.findHostByEmail(view.getHostEmail());
        host = hostService.findHostByEmail("jsuch3x@unblog.fr"); //TODO remove test line
        host = hostService.findHostByEmail("ryoselevitch78@free.fr"); //TODO remove test line

        //get guest
        Guest guest = guestService.findGuestByEmail(view.getGuestEmail());
        guest = guestService.findGuestByEmail("iganter9@privacy.gov.au"); //TODO remove test line
        guest = guestService.findGuestByEmail("coluwatoyinf@hubpages.com");//TODO remove test line

        //Return to menu if null host
        if (host == null) {
            view.displayStatus(false, "Host not found");
            return;}

        //Print Reservation List
        //If host is found, print header and results
        view.printHostResults(host);
        //Confirm guests & return list
        List<Reservation> reservations = identifyGuestsInReservations(reservationService.findAllReservationsForHost(host));
        reservations = reservationService.filterReservationsByGuest(reservations, guest);
        reservations = reservationService.filterUpcomingReservations(reservations);
        //Print the filtered list
        view.printReservations(reservations);

        //Return to menu if null results
        if (reservations.isEmpty()) {
            view.displayStatus(false, "No reservations found for this guest");
            return;}

        //Request id and return the target reservation
        Reservation targetReservation = reservationService.findReservationById(reservations, view.getReservationID());
        if (targetReservation == null) { //!result.isSuccess()) {
            view.displayStatus(false, "Invalid ID selected");
            return;
        }
        System.out.println("got: " + targetReservation.getId());

        //get reservation
        //confirm and update
        //Add update the reservation in repository
        Result<Reservation> result = reservationService.deleteReservation(targetReservation);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
            return;
        }

        //Print Success
        String successMessage = String.format("Reservation %s was deleted.", targetReservation.getId());
        view.displayStatus(true, successMessage);
    }


    //Support Methods

    //Identify any guests found in reservationList
    private List<Reservation> identifyGuestsInReservations(List<Reservation> reservations) {
        List<Reservation> result = reservations.stream()
                .map(r -> { // Update the Guests in list of Reservations
                    Guest gFromService = guestService.findGuestByGuestID(r.getGuest().getId());
                    r.setGuest(gFromService);
                    return r;
                })
                .collect(Collectors.toList());
        return result;
    }

}
