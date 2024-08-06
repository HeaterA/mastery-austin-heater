package learn.mastery.data;

import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostFileRepository implements HostRepository{

    private final String filePath;
    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";

    public HostFileRepository(@Value("./data/hosts.csv") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Host> findAll() {
        ArrayList<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Host findById(String hostId) {
        return findAll().stream()
                .filter(i -> i.getId().equals(hostId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host findByEmail(String email) {
        return findAll().stream()
                .filter(i -> i.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    private Host deserialize(String[] fields) {
        Host result = new Host();

        result.setId(fields[0]);
        result.setLastName(fields[1]);
        result.setEmail(fields[2]);
        result.setPhone(fields[3]);
        result.setAddress(fields[4]);
        result.setCity(fields[5]);
        result.setState(fields[6]);
        result.setPostalCode(fields[7]);
        result.setStandardRate( BigDecimal.valueOf(Double.valueOf( fields[8])) );
        result.setWeekendRate( BigDecimal.valueOf(Double.valueOf( fields[9])) );

        return result;
    }

}
