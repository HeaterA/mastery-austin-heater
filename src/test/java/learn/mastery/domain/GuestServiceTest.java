package learn.mastery.domain;

import learn.mastery.data.GuestRepository;
import learn.mastery.data.GuestRepositoryDouble;
import learn.mastery.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {
    GuestRepository repository = new GuestRepositoryDouble();
    GuestService service = new GuestService(repository);

    @BeforeEach
    void setUp() {
        repository = new GuestRepositoryDouble();
        service = new GuestService(repository);
    }

    @Test
    void shouldFindGuestByValidEmail() {
        Guest guest = service.findGuestByEmail("BobbingAround@email.com");

        assertNotNull(guest);
        assertEquals("BobbingAround@email.com", guest.getEmail());
        assertEquals("Bob", guest.getLastName());
    }

    @Test
    void shouldNotFindGuestByInvalidEmail() {
        Guest guest = service.findGuestByEmail("fakeEmail@email.com");
        assertNull(guest);
    }

    @Test
    void shouldFindGuestByValidId() {
        Guest guest = service.findGuestByGuestID(2);

        assertNotNull(guest);
        assertEquals("BobbingAround@email.com", guest.getEmail());
        assertEquals("Bob", guest.getLastName());
    }

    @Test
    void shouldNotFindGuestByInvalidId() {
        Guest guest = service.findGuestByGuestID(5);
        assertNull(guest);
    }

    @Test
    void shouldValidateGuest() {
        Guest guest = repository.findById(1);
        assertNotNull(guest.getLastName());
        assertNotNull(guest.getEmail());
        assertNotNull(guest.getPhone());
        assertNotNull(guest.getFirstName());
        assertNotNull(guest.getState());

        Result<Guest> result = service.validateGuest(guest);
        assertTrue(result.isSuccess());
        assertEquals(0, result.getErrorMessages().size());
    }

    @Test
    void shouldNotValidateNullGuest() {
        Guest nullGuest = new Guest();
        assertNull(nullGuest.getLastName());
        assertNull(nullGuest.getEmail());
        assertNull(nullGuest.getPhone());
        assertNull(nullGuest.getFirstName());
        assertNull(nullGuest.getState());

        Result<Guest> result = service.validateGuest(nullGuest);
        assertFalse(result.isSuccess());
        assertEquals(6, result.getErrorMessages().size());
    }

    @Test
    void shouldNotValidateGuestWithInvalidFields() {
        Guest falseGuest = (new Guest(0, "j", "j", "JJmail.com", "1700  1234567", "jc"));
        assertNotNull(falseGuest.getLastName());
        assertNotNull(falseGuest.getEmail());
        assertNotNull(falseGuest.getPhone());
        assertNotNull(falseGuest.getFirstName());
        assertNotNull(falseGuest.getState());

        Result<Guest> result = service.validateGuest(falseGuest);
        assertFalse(result.isSuccess());
        System.out.println(result.getErrorMessages());
        assertEquals(6, result.getErrorMessages().size());
    }

}