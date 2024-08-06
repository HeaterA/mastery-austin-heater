package learn.mastery.data;

import learn.mastery.models.Host;
import learn.mastery.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    Reservation findAllById(int id, String hostID);

    List<Reservation> findAllByHost(String hostId);

    boolean update(Reservation reservation, String hostID) throws DataException;

    boolean delete(int reservationId, String hostID) throws DataException;

    Reservation add(Reservation reservation, Host host) throws DataException;

}
