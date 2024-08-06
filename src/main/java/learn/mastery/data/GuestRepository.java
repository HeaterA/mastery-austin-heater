package learn.mastery.data;

import learn.mastery.models.Guest;

import java.util.List;

public interface GuestRepository {

    List<Guest> findAll();

    Guest findById(String id);

    Guest findByEmail(String email);
}
