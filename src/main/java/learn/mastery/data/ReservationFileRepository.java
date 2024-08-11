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
import java.util.stream.Collectors;

@Repository
public class ReservationFileRepository implements ReservationRepository{

    private static final String HEADER = "id,guestId,startDate,endDate,costOfStay";
    private final String directory;
    private Host host; //Need host to get ID


    /**
     * Class constructor.
     */
    public ReservationFileRepository(@Value("./data/reservations")String directory){
        this.directory = directory;
        this.host = new Host();
    }

    //Assign the Host to get ID from

    /**
     * Assigns a host object to the reservation.
     * the host id will be used to determine the
     * file repository path
     *
     * @param host  the host object that determines the file path
     */
    @Override
    public void setHost(Host host){
        this.host = host;
    }

    /**
     * Retrieves the host object from the reservation.
     * the host id will be used to determine the
     * file repository path
     *
     * @return the host object that determines the file path
     */
    @Override
    public Host getHost(){
        return host;
    }

    /**
     * Returns a file path constructed from the host object
     * stored inside of the reservation object. If no host
     * object is found, returns null
     *
     * @return the file path of the file repository
     */
    //Get Folder Path
    private String getFilePath() {
        if(host.getId().isEmpty() || host.equals(new Host())) {
            return "";
        }

        return Paths.get(directory, host.getId() + ".csv").toString();
    }

    //getHostReservations
    /**
     * Returns the reservation with the id integer from the
     * file repository. If no reservation has the id integer, it returns null
     *
     * @param id    the targeted host id
     * @return      the reservation with a selected id
     */
    @Override
    public Reservation findById(int id) {
        return findAll().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a list of reservation objects that contain
     * the provided guest. Provides a empty list if none
     * are found
     *
     * @param guestId       the id of the guest to find
     * @return              a list of filtered reservation objects
     */
    @Override
    public List<Reservation> findAllByGuestId(int guestId) {
        return findAll().stream()
                .filter(i -> i.getGuest().getId() == guestId)
                .collect(Collectors.toList());
    }

    /**
     * Returns the file repository as a list of
     * reservation objects
     *
     * @return list of reservation objects
     */
    @Override
    public List<Reservation> findAll() {
        List<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader( getFilePath() ))) {
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

    /**
     * Prints the repository to file
     *
     * @param reservations      list of reservation objects to write to file
     * @throws DataException
     */
    private void writeToFile(List<Reservation> reservations) throws DataException {

        try (PrintWriter writer = new PrintWriter( getFilePath() )) {
            writer.println(HEADER);
            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    //Serialize
    /**
     * Returns a formatted string created from the values  of a reservation object
     *
     * @param reservation  reservation object containing populated data
     * @return             a formated string created from a reservation object
     */
    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuest().getId(),
                reservation.getcostOfStay());
    }

    //Deserialize
    /**
     * Returns a reservation object created from a line
     * provided by the file repository
     *
     * @param fields  the data retrieved from the file repository
     * @return        reservation object containing the field as data
     */
    private Reservation deserialize(String[] fields) {
        Reservation result = new Reservation();

        result.setId( Integer.parseInt(fields[0]) );
        //Stay Duration
        result.setStartDate( LocalDate.parse(fields[1]) );
        result.setEndDate( LocalDate.parse(fields[2]) );
        //Guest
        Guest guest = new Guest();
        guest.setId(Integer.parseInt (fields[3]));
        result.setGuest(guest);
        //Cost
        result.setcostOfStay( BigDecimal.valueOf(Double.parseDouble(fields[4])) );

        return result;
    }

    /**
     * Updates a reservation object in the repository and returns
     * a boolean based on the update succeeding
     *
     * @param reservation  the reservation object to update
     * @return             whether the reservation updated
     * @throws DataException
     */
    @Override
    public boolean update(Reservation reservation) throws DataException {
        if(reservation == null) {
            return false;
        }

        List<Reservation> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (reservation.getId() == all.get(i).getId()) {
                all.set(i, reservation);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a reservation object with a selected id from
     * the repository and writes the file repository without it
     *
     * @param reservationId  the id of the reservation to remove
     * @return               whether the reservation was removed
     * @throws DataException
     */
    @Override
    public boolean delete(int reservationId) throws DataException {
        if(String.valueOf(reservationId) == null){
            return false;
        }

        List<Reservation> all = findAll();
        for(int i = 0; i < all.size(); i++){
            if(all.get(i).getId() == reservationId){
                all.remove(i);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a reservation object to the repository and writes the
     * file repository with the new object
     *
     * @param reservation       the reservation object to add
     * @return                  the reservation object added
     * @throws DataException
     */
    @Override
    public Reservation add(Reservation reservation) throws DataException {

        List<Reservation> hostReservations = findAll();

        //Make new id
        int nextId = hostReservations.stream()
                .mapToInt(Reservation::getId)
                .max()
                .orElse(0) + 1;

        reservation.setId(nextId);
        hostReservations.add(reservation);
        writeToFile(hostReservations);
        return reservation;
    }

}
