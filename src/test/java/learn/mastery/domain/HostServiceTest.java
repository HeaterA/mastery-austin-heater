package learn.mastery.domain;

import learn.mastery.data.HostRepository;
import learn.mastery.data.HostRepositoryDouble;
import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {
    HostRepository repository = new HostRepositoryDouble();
    HostService service = new HostService(repository);

    @BeforeEach
    void setUp() {
        repository = new HostRepositoryDouble();
        service = new HostService(repository);
    }

    @Test
    void shouldFindHostByValidId() {
        Host host = service.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");

        assertNotNull(host);
        assertEquals("3edda6bc-ab95-49a8-8962-d50b53f84b15", host.getId());
        assertEquals("Yearnes", host.getLastName());
    }

    @Test
    void shouldNotFindHostByInvalidId() {
        Host host = service.findHostByHostId("00000000-0000-0000-0000-000000000000");
        assertNull(host);
    }

    @Test
    void shouldFindHostByValidEmail() {
        Host host = service.findHostByEmail("mfader2@amazon.co.jp");

        assertNotNull(host);
        assertEquals("mfader2@amazon.co.jp", host.getEmail());
        assertEquals("Fader", host.getLastName());
    }

    @Test
    void shouldNotFindHostByInvalidEmail() {
        Host host = service.findHostByEmail("fake@email.com");
        assertNull(host);
    }

    @Test
    void shouldNotValidateNullHost() {
        Host nullHost = new Host();
        assertNull(nullHost.getLastName());
        assertNull(nullHost.getEmail());
        assertNull(nullHost.getPhone());
        assertNull(nullHost.getCity());
        assertNull(nullHost.getState());
        assertNull(nullHost.getAddress());
        assertNull(nullHost.getStandardRate());
        assertNull(nullHost.getWeekendRate());
        assertNull(nullHost.getPostalCode());

        Result<Host> result = service.validateHost(nullHost);
        assertFalse(result.isSuccess());
        assertEquals(10, result.getErrorMessages().size());
    }

    @Test
    void shouldNotValidateHostWithInvalidFields() {
        Host nullHost = new Host("","j","badEmail@mail@mail.com","(5010)2490895","1 jr st","lo","ZA","00asd","-1","-5");

        assertNotNull(nullHost.getLastName());
        assertNotNull(nullHost.getEmail());
        assertNotNull(nullHost.getPhone());
        assertNotNull(nullHost.getCity());
        assertNotNull(nullHost.getState());
        assertNotNull(nullHost.getAddress());
        assertNotNull(nullHost.getStandardRate());
        assertNotNull(nullHost.getWeekendRate());
        assertNotNull(nullHost.getPostalCode());

        Result<Host> result = service.validateHost(nullHost);
        assertFalse(result.isSuccess());
        assertEquals(9, result.getErrorMessages().size());
    }

    @Test
    void shouldValidateValidHost() {
        Host host = repository.findById("b4f38829-c663-48fc-8bf3-7fca47a7ae70");

        assertNotNull(host.getLastName());
        assertNotNull(host.getEmail());
        assertNotNull(host.getPhone());
        assertNotNull(host.getCity());
        assertNotNull(host.getState());
        assertNotNull(host.getAddress());
        assertNotNull(host.getStandardRate());
        assertNotNull(host.getWeekendRate());
        assertNotNull(host.getPostalCode());

        Result<Host> result = service.validateHost(host);
        assertTrue(result.isSuccess());
        assertEquals("b4f38829-c663-48fc-8bf3-7fca47a7ae70", host.getId());
        assertEquals("Fader", host.getLastName());
        assertEquals(BigDecimal.valueOf(451.00), host.getStandardRate());
    }

}