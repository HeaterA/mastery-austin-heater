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

    //getHostReservations

    public ReservationFileRepository(@Value("./data/reservations")String directory){
        this.directory = directory;
        this.host = new Host();
    }

    //Assign the Host to get ID from
    @Override
    public void setHost(Host host){
        this.host = host;
    }

    @Override
    public Host getHost(){
        return host;
    }

    //Get Folder Path
    private String getFilePath() {
        if(host.getId().isEmpty() || host.equals(new Host())) {
            return "";
        }

        return Paths.get(directory, "test_"+ host.getId() + ".csv").toString();//TODO remove test from pathstring
    }

    @Override
    public Reservation findById(int id) {
        return findAll().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Reservation> findAllByGuestId(int guestId) {
        List<Reservation> guestReservations = findAll().stream()
                .filter(i -> i.getGuest().getId() == guestId)
                .collect(Collectors.toList());
        return guestReservations;
    }

    //@Override
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
    private void writeToFile(List<Reservation> reservations) throws DataException {

        try (PrintWriter writer = new PrintWriter( getFilePath() )) {
            writer.println(HEADER);
            //TODO FORMAT OUTPUT
            for (Reservation reservation : reservations) {
                //writer.println(serialize(reservation));
                String toWrite = String.format("%s,%s,%s,%s,%s",
                        reservation.getId(),
                        reservation.getStartDate(),
                        reservation.getEndDate(),
                        reservation.getGuest().getId(),
                        reservation.getcostOfStay());

                writer.println(toWrite);
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    //Serialize
    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuest().getId(),
                reservation.getcostOfStay());
    }

    //Deserialize //TODO review and improve
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
        result.setcostOfStay( BigDecimal.valueOf(Double.valueOf(fields[4])) );

        return result;
    }

    @Override //TODO review and improve
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

    @Override
    public boolean delete(int reservationId) throws DataException {
        if(String.valueOf(reservationId).isEmpty()){
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
