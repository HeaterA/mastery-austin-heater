package learn.mastery.data;

import learn.mastery.models.Guest;
import learn.mastery.models.Host;

import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    @Override
    public List<Host> findAll() {
        List<Host> all = new ArrayList<>();

        all.add( new Host("3edda6bc-ab95-49a8-8962-d50b53f84b15","Yearnes","eyearnes0@sfgate.com","(806) 1783815","3 Nova Trail","Amarillo","TX","79182","340","425") );
        all.add( new Host("a0d911e7-4fde-4e4a-bdb7-f047f15615e8","Rhodes","krhodes1@posterous.com","(478) 7475991","7262 Morning Avenue","Macon","GA","31296","295","368.75") );
        all.add( new Host("b4f38829-c663-48fc-8bf3-7fca47a7ae70","Fader","mfader2@amazon.co.jp","(501) 2490895","99208 Morning Parkway","North Little Rock","AR","72118","451","563.75") );

        return all;
    }

    @Override
    public Host findById(String hostId) {
        for (Host host : findAll()) {
            if (host.getId().equalsIgnoreCase(hostId)) {
                return host;
            }
        }
        return null;
    }

    @Override
    public Host findByEmail(String email) {
        for (Host host : findAll()) {
            if (host.getEmail().equalsIgnoreCase(email)) {
                return host;
            }
        }
        return null;
    }

}
