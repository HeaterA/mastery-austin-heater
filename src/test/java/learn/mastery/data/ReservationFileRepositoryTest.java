package learn.mastery.data;

import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ReservationFileRepositoryTest {

    private ReservationFileRepository repository;
    //Folder Path
    private final String testDirectory = "./data/reservations_test/";

    @BeforeEach
    public void setUp() throws IOException {
        repository = new ReservationFileRepository(testDirectory);

        //Define Host
        Host testHost = new Host();
        testHost.setId("9d469342-ad0b-4f5a-8d28-e81e690ba29a");
        repository.setHost(testHost);

        //Clean File
        Files.copy(Paths.get(testDirectory + "SEEDED_" + testHost.getId() + ".csv"),
                Paths.get(testDirectory + testHost.getId() + ".csv"),
                StandardCopyOption.REPLACE_EXISTING);
    }


    @Test
    void checkThatCleanFileWasMade() {
        String thisFilePath = testDirectory + repository.getHost().getId() + ".csv";

        List<Reservation> r = repository.findAll();
        System.out.println(r.size());

        assertTrue(Files.exists(Paths.get(thisFilePath)));

        assertEquals(1, repository.findAll().size());
    }

    @Test
    public void shouldAddValidReservation() throws DataException {
        Reservation validReservation = new Reservation();
        Guest newGuest = new Guest(4, "Four", "Ford", "4Fords@email.com", "4041234567", "ny");
        validReservation.setGuest(newGuest);

        //Filler Values for reservation
        validReservation.setStartDate(LocalDate.now());
        validReservation.setEndDate(LocalDate.now().plusDays(1));
        validReservation.setcostOfStay(BigDecimal.valueOf(100.00));

        Reservation addedReservation = repository.add(validReservation);

        assertNotNull(addedReservation);
        assertEquals(2, addedReservation.getId());
        assertEquals(4, addedReservation.getGuest().getId());
        assertEquals(2, repository.findAll().size());
    }



    @Test
    public void shouldFindReservationById() throws DataException {
        Reservation validReservation = new Reservation();
        Guest newGuest = new Guest(4, "Four", "Ford", "4Fords@email.com", "4041234567", "ny");
        validReservation.setGuest(newGuest);

        //Filler Values for reservation
        validReservation.setStartDate(LocalDate.now());
        validReservation.setEndDate(LocalDate.now().plusDays(1));
        validReservation.setcostOfStay(BigDecimal.valueOf(100.00));

        Reservation addedReservation = repository.add(validReservation);

        Reservation found = repository.findById(2);
        assertNotNull(found);
        assertEquals(addedReservation.getId(), found.getId());
    }

    @Test
    public void shouldUpdateReservationOnUpdate() throws DataException {
        Reservation validReservation = new Reservation();
        Guest newGuest = new Guest(4, "Four", "Ford", "4Fords@email.com", "4041234567", "ny");
        validReservation.setGuest(newGuest);

        //Filler Values for reservation
        validReservation.setStartDate(LocalDate.now());
        validReservation.setEndDate(LocalDate.now().plusDays(1));
        validReservation.setcostOfStay(BigDecimal.valueOf(100.00));

        Reservation addedReservation = repository.add(validReservation);

        addedReservation.setcostOfStay(BigDecimal.valueOf(150.00));
        boolean updated = repository.update(addedReservation);

        assertTrue(updated);
        Reservation updatedReservation = repository.findById(addedReservation.getId());
        assertNotNull(updatedReservation);
        assertEquals(BigDecimal.valueOf(150.00), updatedReservation.getcostOfStay());
    }

    @Test
    public void shouldDeleteReservationById() throws DataException {
        Reservation validReservation = new Reservation();
        Guest newGuest = new Guest(4, "Four", "Ford", "4Fords@email.com", "4041234567", "ny");
        validReservation.setGuest(newGuest);

        //Filler Values for reservation
        validReservation.setStartDate(LocalDate.now());
        validReservation.setEndDate(LocalDate.now().plusDays(1));
        validReservation.setcostOfStay(BigDecimal.valueOf(100.00));

        Reservation addedReservation = repository.add(validReservation);

        boolean deleted = repository.delete(addedReservation.getId());
        assertTrue(deleted);
        assertNull(repository.findById(addedReservation.getId()));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    public void testFindAllByGuestId() throws DataException {
        Reservation validReservation = new Reservation();
        Guest newGuest = new Guest(4, "Four", "Ford", "4Fords@email.com", "4041234567", "ny");
        validReservation.setGuest(newGuest);

        //Filler Values for reservation
        validReservation.setStartDate(LocalDate.now());
        validReservation.setEndDate(LocalDate.now().plusDays(1));
        validReservation.setcostOfStay(BigDecimal.valueOf(100.00));

        Reservation addedReservation = repository.add(validReservation);

        assertEquals(2, repository.findAll().size());

        List<Reservation> reservationsByGuestId = repository.findAllByGuestId(4);
        assertEquals(1, reservationsByGuestId.size());
    }

}