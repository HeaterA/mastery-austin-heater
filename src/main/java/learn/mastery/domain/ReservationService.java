package learn.mastery.domain;

import learn.mastery.data.GuestRepository;
import learn.mastery.data.HostRepository;
import learn.mastery.data.ReservationRepository;
import learn.mastery.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservationService {

    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(GuestRepository guestRepository, ReservationRepository reservationRepository){
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }

    //Read

    //Add
    {
        
        //get fields
        //get cost
        //validate
        //return
    }

    //GetCostOfStay

    //Update

    //Delete



    //Validate
    private Result<Reservation> validate(Reservation reservation){
        Result<Reservation> result = new Result<>();

        //null
        if(reservation == null) {
            result.addErrorMessage("Reservation cannot be null");
            return result;
        }
        //null fields
        if(reservation.getGuest() == null) {
            result.addErrorMessage("Valid guest is required");
        }
        if(reservation.getStartDate() == null) {
            result.addErrorMessage("Valid start date is required");
        }
        if(reservation.getEndDate() == null) {
            result.addErrorMessage("Valid end date is required");
        }
        if(reservation.getcostOfStay() == null) {
            result.addErrorMessage("Valid cost is required");
        }
        if(!result.isSuccess()){
            return result;
        }

        //Host

        //Guest
        if(guestRepository.findByEmail(reservation.getGuest().getEmail()) == null){
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
        if(reservation.getcostOfStay().doubleValue() < 0){
            result.addErrorMessage("Cost of stay must be a positive value.");
        }


        return result;
    }


}
