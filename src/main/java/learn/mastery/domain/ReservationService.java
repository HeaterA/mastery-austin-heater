package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.GuestRepository;
import learn.mastery.data.ReservationRepository;
import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReservationService {

    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;

    /**
     * Class constructor.
     */
    public ReservationService(GuestRepository guestRepository, ReservationRepository reservationRepository) {
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }

    //Read

    /**
     * Returns a list of reservation objects found stored under
     * the ID of the provided host object
     *
     * @param   host the host object that provides the ID path
     * @return  a list of reservation objects
     */
    //Creates a list of reservations under a specific host
    public List<Reservation> findAllReservationsForHost(Host host) {
        List<Reservation> hostReservations = new ArrayList<>();
        //Check for Host
        if (host != null) {
            //check for reservations
            reservationRepository.setHost(host);
            hostReservations = reservationRepository.findAll().stream()
                    .sorted(Comparator.comparing(Reservation::getStartDate))
                    .toList();
        }
        return hostReservations;
    }

    /**
     * Returns a reservation object with a specific ID
     * found in a list of reservation objects
     *
     * @param reservations  a list of reservation objects
     * @param targetId      the desired reservation id to find
     * @return              a reservation object with the same id as targetId
     */
    //Returns a single reservation from a list of reservations based on a target id
    public Reservation findReservationById(List<Reservation> reservations, int targetId) {
        Reservation targetReservations = new Reservation();
        if (reservations != null) {
            //check for reservations after no
            targetReservations = reservations.stream()
                    .filter(reservation -> reservation.getId() == targetId)
                    .findFirst()
                    .orElse(null);
        }
        return targetReservations;
    }

    //Filters a list of reservations that occur after the current date
    /**
     * Returns a list of reservation objects that contain
     * startDates whose values after the localDate of today
     * Provides an empty list if none are found
     *
     * @param reservations  a list of reservation objects
     * @return              a list of filtered reservation objects
     */
    public List<Reservation> filterUpcomingReservations(List<Reservation> reservations) {
        List<Reservation> upcomingReservations = new ArrayList<>();
        if (reservations != null) {
            //check for reservations after no
            upcomingReservations = reservations.stream()
                    .filter(reservation -> reservation.getStartDate().isAfter(LocalDate.now()))
                    .toList();
        }
        return upcomingReservations;
    }

    //Filters a list of reservations that were made by a specific guest
    /**
     * Returns a list of reservation objects that contain
     * the provided guest. Provides an empty list if none
     * are found
     *
     * @param reservations  a list of reservation objects
     * @param guest         the desired reservation id to find
     * @return              a list of filtered reservation objects
     */
    public List<Reservation> filterReservationsByGuest(List<Reservation> reservations, Guest guest) {
        List<Reservation> reservationsMadeByGuest = new ArrayList<>();
        if (reservations != null && guest != null) {
            //check for reservations after no
            reservationsMadeByGuest = reservations.stream()
                    .filter(reservation -> reservation.getGuest().getEmail().equalsIgnoreCase(guest.getEmail()))
                    .toList();
        }
        return reservationsMadeByGuest;
    }


    //Add
    /**
     * Adds a reservation to the repository and passes it to the data layer
     * to be written to file.
     *
     * @param reservation       the reservation object to add to the repository
     * @return                  the list of error messages
     * @throws DataException
     */
    public Result<Reservation> addReservationToFile(Reservation reservation) throws DataException {
        //Validate again to be safe
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        //set path
        result.setPayload((reservationRepository.add(reservation)));
        return result;
    }


    //Update
    /**
     * finds a reservation in the repository and passes it to the data layer
     * to be updated in the file.
     *
     * @param reservation       the reservation object to be updated
     * @return                  the list of error messages
     * @throws DataException
     */
    public Result<Reservation> updateReservation(Reservation reservation) throws DataException {
        //Validate again to be safe
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        //set path
        boolean canUpdate = (reservationRepository.update(reservation));
        if(!canUpdate){
            result.addErrorMessage(String.format("Reservation %s does not exist", reservation.getId()));
        }
        return result;
    }


    //Delete
    /**
     * Removes a reservation from the repository and passes it to the data layer
     * to be removed from file.
     *
     * @param reservation       the reservation object to remove from the repository
     * @return                  the list of error messages
     * @throws DataException
     */
    public Result<Reservation> deleteReservation(Reservation reservation) throws DataException {
        //Validate again to be safe
        Result<Reservation> result = new Result<>();
        if (reservation == null) {
            result.addErrorMessage("Reservation does not exist");
            return result;
        }

        //set path
        boolean canDelete = (reservationRepository.delete(reservation.getId()));
        if(!canDelete){
            result.addErrorMessage(String.format("Reservation %s does not exist", reservation.getId()));
        }
        return result;
    }


    //Validate
    /**
     * Validates if a reservation object's fields all pass
     * the desired checks
     *
     * @param reservation   the reservation object to check
     * @return              a list of potential error messages
     */
    //Public method to pass a  new reservation to validate()
    public Result<Reservation> checkReservation(Reservation reservation) {
        Result<Reservation> result = validate(reservation);
        return result;
    }

    /**
     * A helper validation method that checks if a reservation's
     * dates overlap with any preexisting reservation. This method
     * is used by the main validate method
     *
     * @param newReservation    the reservation object to check
     * @return                  a list of potential error messages
     */
    private Result<Reservation> conflictingDateRange(Reservation newReservation) {
        Result<Reservation> result = new Result<>();
        reservationRepository.setHost(newReservation.getHost());

        //Check if there is any overlap between the new reservation and repository selected
        for (Reservation confirmedReservation : reservationRepository.findAll()) {
            boolean overlap = areDatesOverlapping(newReservation, confirmedReservation);
            if (overlap && (confirmedReservation.getId() != newReservation.getId())) {
                result.addErrorMessage("Dates cannot Overlap");
                return result;
            }
        }
        return result;
    }


    /**
     * Returns a boolean that shows if two reservations have overlapping dates.
     * a helper method used by checkReservation
     *
     * @param newReservation        a reservation object
     * @param confirmedReservation  a preexisting reservation object
     * @return                      Whether the reservation dates overlap
     */
    private boolean areDatesOverlapping(Reservation newReservation, Reservation confirmedReservation) {
        //Do the dates overlap
        return ((!newReservation.getStartDate().isBefore(confirmedReservation.getStartDate())) &&
                (!newReservation.getStartDate().isAfter(confirmedReservation.getEndDate()))
                ||
                (!newReservation.getEndDate().isBefore(confirmedReservation.getStartDate())) &&
                        (!newReservation.getEndDate().isAfter(confirmedReservation.getEndDate()))
        );
    }

    //Main validation method. checks if a method is valid

    /**
     * The main validation check for reservation objects.
     * checks that a reservation's fields return appropriate
     * values and provides a list of error messages if any
     * issues are found
     *
     * @param reservation  a reservation object
     * @return             the list of error messages
     */
    private Result<Reservation> validate(Reservation reservation) {
        Result<Reservation> result = new Result<>();

        //null
        if (reservation == null) {
            result.addErrorMessage("Reservation cannot be null");
            return result;
        }
        //null fields
        if (reservation.getGuest() == null) {
            result.addErrorMessage("Valid guest is required");
        }
        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Valid start date is required");
        }
        if (reservation.getEndDate() == null) {
            result.addErrorMessage("Valid end date is required");
        }
        if (reservation.getcostOfStay() == null) {
            result.addErrorMessage("Valid cost is required");
        }
        if (!result.isSuccess()) {
            return result;
        }

        //Host

        //Guest
        if (guestRepository.findByEmail(reservation.getGuest().getEmail()) == null) {
            result.addErrorMessage("Guest not found");
        }

        //Start
        if (reservation.getStartDate().isBefore(LocalDate.now().plusDays(1))) {
            result.addErrorMessage("Start date must not have occurred yet.");
        }

        //End
        // No future dates.
        if (!reservation.getStartDate().isBefore(reservation.getEndDate())) {
            result.addErrorMessage("Start date must come before the end date.");
        }

        //GetCost
        if (reservation.getcostOfStay().doubleValue() < 0) {
            result.addErrorMessage("Cost of stay must be a positive value.");
        }

        if (!result.isSuccess()) {
            return result;
        }

        //Check Overlapping Dates
        result = conflictingDateRange(reservation);

        return result;
    }


}
