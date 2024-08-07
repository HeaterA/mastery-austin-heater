package learn.mastery.data;

import learn.mastery.models.Host;
import learn.mastery.models.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {

    private List<Reservation> reservations = new ArrayList<>();
    private Host host;

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
        System.out.println("yeet");
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
        }

        return false;
    }

    @Override
    public boolean delete(int reservationId) {
        if (findAllByGuestId(reservationId).size() > 0) {
            reservations = reservations.stream()
                    .filter(i -> i.getId() != reservationId)
                    .collect(Collectors.toList());
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
