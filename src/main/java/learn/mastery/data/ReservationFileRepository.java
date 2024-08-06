package learn.mastery.data;

import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository{

    private static final String HEADER = "id,guestId,startDate,endDate,costOfStay";
    private final String directory;

    //getHostReservations

    public ReservationFileRepository(@Value("./data/reservations")String directory){
        this.directory = directory;
    }

    //Get Folder Path
    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }

    @Override
    public Reservation findAllById(int id, String hostID) {
        return findAllByHost(hostID).stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    //@Override
    public List<Reservation> findAllByHost(String hostID) {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostID)))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    //WriteAll
    private void writeToFile(List<Reservation> reservations, String hostId) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(hostId))) {

            writer.println(HEADER);

            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    //Serialize
    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getId(),
                reservation.getGuest().getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getcostOfStay());
    }

    //Deserialize //TODO review and improve
    private Reservation deserialize(String[] fields) {
        Reservation result = new Reservation();

        result.setId( Integer.parseInt(fields[0]) );

        Guest guest = new Guest();
        guest.setId(fields[1]);
        result.setGuest(guest);

        result.setStartDate( LocalDate.parse(fields[2]) );
        result.setEndDate( LocalDate.parse(fields[3]) );
        result.setcostOfStay( BigDecimal.valueOf(Double.valueOf(fields[4])) );

        return result;
    }

    @Override //TODO review and improve
    public boolean update(Reservation reservation, String hostID) throws DataException {

        if(reservation == null) {
            return false;
        }

        List<Reservation> all = findAllByHost(hostID);
        for (int i = 0; i < all.size(); i++) {
            if (reservation.getId() == all.get(i).getId()) {
                all.set(i, reservation);
                writeToFile(all, hostID);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean delete(int reservationId, String hostId) throws DataException {
        if(String.valueOf(reservationId).isEmpty()){
            return false;
        }

        List<Reservation> all = findAllByHost(hostId);
        for(int i = 0; i < all.size(); i++){
            if(all.get(i).getId() == reservationId){
                all.remove(i);
                writeToFile(all, hostId);
                return true;
            }
        }
        return false;
    }

    @Override
    public Reservation add(Reservation reservation, Host host) throws DataException {

        List<Reservation> hostReservations = findAllByHost(host.getId());

        //Make new Id
        int nextId = hostReservations.stream()
                .mapToInt(Reservation::getId)
                .max()
                .orElse(0) + 1;

        reservation.setId(nextId);

        hostReservations.add(reservation);
        writeToFile(hostReservations, host.getId());
        return reservation;
    }

}
