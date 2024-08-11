package learn.mastery.data;

import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {

    private List<Reservation> reservations = new ArrayList<>();
    private Host host;

    public ReservationRepositoryDouble(){
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2024, 8, 10));
        reservation.setEndDate(LocalDate.of(2024, 8, 15));
        reservation.setGuest(new Guest(1, "Joe", "Job", "JJ@email.com", "(700) 1234567", "nc"));
        reservation.setHost(new Host("3edda6bc-ab95-49a8-8962-d50b53f84b15","Yearnes","eyearnes0@sfgate.com","(806) 1783815","3 Nova Trail","Amarillo","TX","79182","340","425") );
        reservation.setcostOfStay(reservation.determineCostOfStay());
        reservations.add(reservation);
    }

    @Override
    public void setHost(Host host) {
        this.host = host;
    }

    @Override
    public Host getHost() {
        return host;
    }

    @Override
    public Reservation findById(int id) {
        for (Reservation reservation : findAll()) {
            if (reservation.getId() == id) {
                return reservation;
            }
        }
        return null;
    }

    @Override
    public List<Reservation> findAllByGuestId(int guestId) {
        List<Reservation> guestReservations = findAll().stream()
                .filter(i -> i.getId() == guestId)
                .collect(Collectors.toList());
        return guestReservations;
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.stream().collect(Collectors.toList());
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        Reservation toUpdate = findById(reservation.getId());

        if(!toUpdate.equals(new Reservation())) {
            toUpdate.setGuest(reservation.getGuest());
            toUpdate.setStartDate(reservation.getStartDate());
            toUpdate.setEndDate(reservation.getEndDate());
            toUpdate.setcostOfStay(reservation.getcostOfStay());
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(int reservationId) {
        if (findById(reservationId) != null){
            reservations = reservations.stream()
                    .filter(i -> i.getId() != reservationId)
                    .collect(Collectors.toList());
            return true;
        }
        return false;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        reservations.add(reservation);

        int nextID = 1;
        while(!findAllByGuestId(nextID).isEmpty()){
            nextID++;
        }
        reservation.setId(reservations.size());

        return reservation;
    }
}
