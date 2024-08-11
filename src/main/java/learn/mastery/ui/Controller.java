package learn.mastery.ui;

import learn.mastery.data.DataException;
import learn.mastery.domain.GuestService;
import learn.mastery.domain.HostService;
import learn.mastery.domain.ReservationService;
import learn.mastery.domain.Result;
import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class Controller {

    private final View view;
    private final HostService hostService;
    private final GuestService guestService;
    private final ReservationService reservationService;


    /**
     * Class constructor.
     */
    public Controller(HostService hostService, GuestService guestService, ReservationService reservationService, View view) {
        this.hostService = hostService;
        this.guestService = guestService;
        this.reservationService = reservationService;
        this.view = view;
    }

    /**
     * Prints out a welcome message and starts the main loop.
     * Prints out an exit message once the loop ends
     */
    public void run() {
        view.displayHeader("Welcome to Don't Wreck My House");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    /**
     * Prints out the main menu of the program and prompts
     * the console for input. Based on the enum value selected
     * the program then prompts for the appropriate method call
     *
     * @throws DataException
     */
    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS:
                    viewReservations();
                    break;
                case MAKE_RESERVATION:
                    addReservation();
                    break;
                case EDIT_RESERVATION:
                    editReservations();
                    break;
                case CANCEL_RESERVATION:
                    deleteReservations();
                    break;

            }
        } while (option != MainMenuOption.EXIT);
    }


    //View
    /**
     * Prints out a list of reservation objects
     * based on a provided host email
     */
    private void viewReservations() {
        view.displayHeader("VIEW RESERVATIONS FOR HOST");
        //Get host
        Host host = hostService.findHostByEmail(view.getHostEmail());

        //Print Reservation List
        //If host is found, print header and results
        view.printHostResults(host);
        if(host == null){return;}

        //Confirm guests & return list
        List<Reservation> reservations = identifyGuestsInReservations(reservationService.findAllReservationsForHost(host));
        //print List
        view.printReservations(reservations);
    }


    //Add
    /**
     * Prints out a list of upcoming reservation objects
     * based on a provided host email and guest email strings.
     * Prompts a new reservation object for creation. The
     * reservation is then validated and prompted for confirmation.
     * once confirmed object is added to the repository and
     * written to the file repository based on the host
     *
     * @throws DataException
     */
    private void addReservation() throws DataException {
        view.displayHeader("ADD A RESERVATION");

        //Get host
        Host host = hostService.findHostByEmail(view.getHostEmail());
        //get guest
        Guest guest = guestService.findGuestByEmail(view.getGuestEmail());

        //Print Reservation List
        //If host is found, print header and results
        view.printHostResults(host);
        if(host == null){return;}

        //Confirm guests & return list
        List<Reservation> reservations = identifyGuestsInReservations(reservationService.findAllReservationsForHost(host));
        reservations = reservationService.filterUpcomingReservations(reservations);
        //Print the filtered list
        view.printReservations(reservations);

        //Return to menu if null guest
        if (guest == null) {
            return;
        }

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


    //Edit
    /**
     * Prints out a list of reservation objects filtered by a
     * guest, based on a provided host email and guest email strings.
     * The targeted reservation object is then located in
     * the file repository after receiving an id input.
     * If the targeted reservation is found, prompts for new LocalDates
     * are prompted to be updated and then validated along with the new cost.
     * The reservation is then updated in the file repository after
     * receiving confirmation from the console.
     *
     * @throws DataException
     */
    private void editReservations() throws DataException {
        view.displayHeader("UPDATE A RESERVATION");

        //Get host
        Host host = hostService.findHostByEmail(view.getHostEmail());
        //get guest
        Guest guest = guestService.findGuestByEmail(view.getGuestEmail());

        //Print Reservation List
        //If host is found, print header and results
        view.printHostResults(host);
        if(host == null){return;}

        //Confirm guests & return list
        List<Reservation> reservations = identifyGuestsInReservations(reservationService.findAllReservationsForHost(host));
        reservations = reservationService.filterReservationsByGuest(reservations, guest);
        //Print the filtered list
        view.printReservations(reservations);

        //Return to menu if null results
        if (reservations.isEmpty()) {
            view.displayStatus(false, "No reservations found for this guest");
            return;
        }

        //Request id and return the target reservation
        Reservation targetReservation = reservationService.findReservationById(reservations, view.getReservationID());
        if (targetReservation == null) {
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


    //Delete
    /**
     * Prints out a list of upcoming reservation objects filtered
     * by guest, based on a provided host email and guest email strings.
     * The targeted reservation object is then removed from
     * the file repository after receiving an id input and
     * confirmation from the console
     *
     * @throws DataException
     */
    private void deleteReservations() throws DataException {
        view.displayHeader("DELETE A RESERVATION");

        //Get host
        Host host = hostService.findHostByEmail(view.getHostEmail());
        //get guest
        Guest guest = guestService.findGuestByEmail(view.getGuestEmail());

        //Print Reservation List
        //If host is found, print header and results
        view.printHostResults(host);
        if(host == null){return;}

        //Confirm guests & return list
        List<Reservation> reservations = identifyGuestsInReservations(reservationService.findAllReservationsForHost(host));
        reservations = reservationService.filterReservationsByGuest(reservations, guest);
        reservations = reservationService.filterUpcomingReservations(reservations);
        //Print the filtered list
        view.printReservations(reservations);

        //Return to menu if null results
        if (reservations.isEmpty()) {
            view.displayStatus(false, "No reservations found for this guest");
            return;
        }

        //Request id and return the target reservation
        Reservation targetReservation = reservationService.findReservationById(reservations, view.getReservationID());
        if (targetReservation == null) {
            view.displayStatus(false, "Invalid ID selected");
            return;
        }

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
    /**
     * Returns a list of reservation objects that
     * populates each reservation's guest with
     * the guest that matches the stored id
     *
     * @param reservations a list of reservation objects with guest ids
     * @return a list of reservation objects with populated guests
     */
    private List<Reservation> identifyGuestsInReservations(List<Reservation> reservations) {
        List<Reservation> result = reservations.stream()
                .map(r -> {
                    // Update the Guests in list of Reservations
                    Guest guests = guestService.findGuestByGuestID(r.getGuest().getId());
                    r.setGuest(guests);
                    return r;
                })
                .collect(Collectors.toList());
        return result;
    }

}
