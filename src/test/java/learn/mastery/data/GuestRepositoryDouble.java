package learn.mastery.data;

import learn.mastery.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {
    @Override
    public List<Guest> findAll() {
        List<Guest> all = new ArrayList<>();

        all.add(new Guest(1, "Joe", "Job", "JJ@email.com", "(700) 1234567", "nc"));
        all.add(new Guest(2, "Billy", "Bob", "BobbingAround@email.com", "(600) 1234567", "sc"));
        all.add(new Guest(3, "Moe", "Robbins", "RobbMoe3@email.com", "(504) 1234567", "nc"));

        return all;
    }

    @Override
    public Guest findById(int id) {
        for (Guest guest : findAll()) {
            if (guest.getId()==id) {
                return guest;
            }
        }
        return null;
    }

    @Override
    public Guest findByEmail(String email) {
        for (Guest guest : findAll()) {
            if (guest.getEmail().equalsIgnoreCase(email)) {
                return guest;
            }
        }
        return null;
    }

}
