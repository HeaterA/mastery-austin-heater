package learn.mastery.data;

import learn.mastery.models.Host;
import learn.mastery.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    void setHost(Host host);

    Host getHost();

    Reservation findById(int id);

    List<Reservation> findAllByGuestId(int id);

    List<Reservation> findAll();

    boolean update(Reservation reservation) throws DataException;

    boolean delete(int reservationId) throws DataException;

    Reservation add(Reservation reservation) throws DataException;

}
