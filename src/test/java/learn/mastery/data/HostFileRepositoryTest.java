package learn.mastery.data;

import learn.mastery.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    private HostRepository repository = new HostRepositoryDouble();

    @BeforeEach
    void setUp() {
        repository = new HostRepositoryDouble();
    }

    @Test
    void confirmRepositoryIsPopulated(){
        assertFalse(repository.findAll().isEmpty());
        assertEquals(3, repository.findAll().size());

        List<Host> HostRepo = repository.findAll();
        assertEquals("Yearnes", HostRepo.get(0).getLastName());
        assertEquals("a0d911e7-4fde-4e4a-bdb7-f047f15615e8", HostRepo.get(1).getId());
        assertEquals("mfader2@amazon.co.jp", HostRepo.get(2).getEmail());
    }

    @Test
    void findAllHostsInRepository() {
        assertFalse(repository.findAll().isEmpty());
        assertEquals(3, repository.findAll().size());
    }

    @Test
    void findAllHostsAddedToRepository() {
        Host newHost = new Host("d63304e3-de36-4ecc-8f8f-847431ffff64","Zuppa","ezuppa8@yale.edu","(915) 4423313","304 Aberg Trail","El Paso","TX","79905","399","498.75");

        List<Host> HostRepo = repository.findAll();
        HostRepo.add(newHost);

        assertEquals(4, HostRepo.size());
        assertEquals("ezuppa8@yale.edu", HostRepo.get(3).getEmail());
    }

    @Test
    void canFindHostByValidId() {
        Host validHost = repository.findById("a0d911e7-4fde-4e4a-bdb7-f047f15615e8");
        assertEquals("Rhodes", validHost.getLastName());
    }

    @Test
    void canNotFindHostByInvalidId() {
        Host invalidHost = repository.findById("00d011e7-4fde-4e0a-b0b7-f047f10015e8");
        assertNull(invalidHost);
    }

    @Test
    void canFindHostByValidEmail() {
        Host validHost = repository.findByEmail("mfader2@amazon.co.jp");
        assertEquals("mfader2@amazon.co.jp", validHost.getEmail());
    }

    @Test
    void canNotFindHostByInvalidEmail() {
        Host invalidHost = repository.findByEmail("Fakemail@email.com");
        assertNull(invalidHost);
    }
    
    
}