package learn.mastery.data;

import learn.mastery.models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestFileRepository implements GuestRepository {

    private final String filePath;
    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";

    /**
     * Class constructor.
     *
     * @param filePath absolute file path holding all guest objects
     */
    public GuestFileRepository(@Value("./data/guests.csv") String filePath) {
        this.filePath = filePath;
    }

    /**
     * Retrieves all guest objects from the file repository
     * and returns it as a list
     *
     * @return  a list of guest objects
     */
    @Override
    public List<Guest> findAll() {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    /**
     * Returns the guest with the provided id from the
     * file repository. If no guest has the ID, it returns null
     *
     * @param id  the targeted guest id
     * @return    the guest with a selected id
     */
    @Override
    public Guest findById(int id) {
        return findAll().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the guest with the email string from the
     * file repository. If no guest has the email string, it returns null
     *
     * @param email  the targeted guest id
     * @return       the guest with a selected id
     */
    @Override
    public Guest findByEmail(String email) {
        return findAll().stream()
                .filter(i -> i.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }


    //Deserialize
    /**
     * Returns a guest object created from a line
     * provided by the file repository
     *
     * @param fields  the data retrieved from the file repository
     * @return        guest object containing the field as data
     */
    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setId( Integer.parseInt( fields[0]) );
        result.setFirstName(fields[1]);
        result.setLastName(fields[2]);
        result.setEmail((fields[3]));
        result.setPhone((fields[4]));
        result.setState(fields[5]);
        return result;
    }

}
