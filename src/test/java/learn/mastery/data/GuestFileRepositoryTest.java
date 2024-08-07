package learn.mastery.data;

import learn.mastery.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    private GuestRepository repository = new GuestRepositoryDouble();

    @BeforeEach
    void setUp() {
        repository = new GuestRepositoryDouble();
    }

    @Test
    void confirmRepositoryIsPopulated(){
        assertFalse(repository.findAll().isEmpty());
        assertEquals(3, repository.findAll().size());

        List<Guest> guestRepo = repository.findAll();
        assertEquals("Joe", guestRepo.get(0).getFirstName());
        assertEquals("Bob", guestRepo.get(1).getLastName());
        assertEquals("RobbMoe3@email.com", guestRepo.get(2).getEmail());
    }

    @Test
    void findAllGuestsInRepository() {
        assertFalse(repository.findAll().isEmpty());
        assertEquals(3, repository.findAll().size());
    }

    @Test
    void findAllGuestsAddedToRepository() {
        Guest newGuest = new Guest(4, "Four", "Ford", "4Fords@email.com", "4041234567", "ny");

        List<Guest> guestRepo = repository.findAll();
        guestRepo.add(newGuest);

        assertEquals(4, guestRepo.size());
        assertEquals("4Fords@email.com", guestRepo.get(3).getEmail());
    }

    @Test
    void canFindGuestByValidId() {
        Guest validGuest = repository.findById(1);
        assertEquals("Joe", validGuest.getFirstName());
    }

    @Test
    void canNotFindGuestByInvalidId() {
        Guest invalidGuest = repository.findById(5);
        assertNull(invalidGuest);
    }

    @Test
    void canFindGuestByValidEmail() {
        Guest validGuest = repository.findByEmail("RobbMoe3@email.com");
        assertEquals("RobbMoe3@email.com", validGuest.getEmail());
    }

    @Test
    void canNotFindGuestByInvalidEmail() {
        Guest invalidGuest = repository.findByEmail("Fakemail@email.com");
        assertNull(invalidGuest);
    }

}