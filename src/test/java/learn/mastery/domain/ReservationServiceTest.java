package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.GuestRepositoryDouble;
import learn.mastery.data.HostRepositoryDouble;
import learn.mastery.data.ReservationRepositoryDouble;
import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {
    ReservationService service = new ReservationService(
            new GuestRepositoryDouble(),
            new ReservationRepositoryDouble());
    HostService hostService = new HostService(new HostRepositoryDouble());


    @BeforeEach
    void setUp() {
        service = new ReservationService(new GuestRepositoryDouble(), new ReservationRepositoryDouble());
    }

    @Test
    void shouldFindAllReservationsForValidHost() {
        Host host = hostService.findHostByEmail("eyearnes0@sfgate.com");
        assertTrue(hostService.validateHost(host).isSuccess());

        List<Reservation> reservations = service.findAllReservationsForHost(host);
        assertEquals(1, reservations.size());
        assertEquals(host.getId(), reservations.get(0).getHost().getId());
    }

    @Test
    void shouldNotFindAllReservationsForNullHost() {
        Host host = null;
        assertFalse(hostService.validateHost(host).isSuccess());

        List<Reservation> reservations = service.findAllReservationsForHost(host);
        assertEquals(0, reservations.size());
    }

    @Test
    void shouldFindReservationsByValidID() {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());

        List<Reservation> reservations = service.findAllReservationsForHost(host);
        assertEquals(1, reservations.size());

        Reservation reservation = service.findReservationById(reservations, 1);
        assertNotNull(reservation.getId());
        assertEquals(1, reservation.getId());
    }

    @Test
    void shouldNotFindReservationsForInvalidId() {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());

        List<Reservation> reservations = service.findAllReservationsForHost(host);
        assertEquals(1, reservations.size());

        Reservation reservation = service.findReservationById(reservations, 99);
        assertNull(reservation);
    }

    @Test
    void shouldFilterUpcomingReservations() {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());
        List<Reservation> reservations = service.findAllReservationsForHost(host);
        //Set dates to future
        reservations.get(0).setStartDate(LocalDate.now().plusDays(2));
        reservations.get(0).setEndDate(LocalDate.now().plusDays(5));


        reservations = service.filterUpcomingReservations(reservations);
        assertEquals(1, reservations.size());
    }

    @Test
    void shouldNotFilterPastReservations() {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());
        List<Reservation> reservations = service.findAllReservationsForHost(host);
        //Set dates to future
        reservations.get(0).setStartDate(LocalDate.now().plusDays(-5));
        reservations.get(0).setEndDate(LocalDate.now().plusDays(-2));

        reservations = service.filterUpcomingReservations(reservations);
        assertEquals(0, reservations.size());
    }

    @Test
    void shouldFilterReservationsByGuest() {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());
        List<Reservation> reservations = service.findAllReservationsForHost(host);

        Guest guest = new Guest(1, "Joe", "Job", "JJ@email.com", "(700) 1234567", "nc");

        reservations = service.filterReservationsByGuest(reservations, guest);
        assertEquals(1, reservations.size());
        assertEquals(guest.getEmail(), reservations.get(0).getGuest().getEmail());
        assertEquals(LocalDate.of(2024, 8, 10), reservations.get(0).getStartDate());
    }

    @Test
    void shouldNotFilterReservationsByFakeGuest() {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());
        List<Reservation> reservations = service.findAllReservationsForHost(host);

        Guest guest = new Guest(3, "Moe", "Robbins", "RobbMoe3@email.com", "(504) 1234567", "nc");

        reservations = service.filterReservationsByGuest(reservations, guest);
        assertEquals(0, reservations.size());
    }

    @Test
    void shouldAddValidReservation() throws DataException {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());
        List<Reservation> reservations = service.findAllReservationsForHost(host);
        //Set dates to future
        reservations.get(0).setStartDate(LocalDate.now().plusDays(2));
        reservations.get(0).setEndDate(LocalDate.now().plusDays(5));

        Result<Reservation> result = service.addReservationToFile(reservations.get(0));
        assertTrue(result.isSuccess());
        assertEquals(0, result.getErrorMessages().size());
    }

    @Test
    void shouldNotAddInvalidReservation() throws DataException {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());
        List<Reservation> reservations = service.findAllReservationsForHost(host);

        Result<Reservation> result = service.addReservationToFile(reservations.get(0));
        assertFalse(result.isSuccess());
        assertEquals(1, result.getErrorMessages().size());
    }

    @Test
    void shouldUpdateValidReservation() throws DataException {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());
        List<Reservation> reservations = service.findAllReservationsForHost(host);

        Reservation updated = reservations.get(0);
        updated.setEndDate(LocalDate.now().plusMonths(2));
        updated.setStartDate(LocalDate.now().plusMonths(1));

        Result<Reservation> result = service.updateReservation(updated);
        assertTrue(result.isSuccess());
        assertEquals(0, result.getErrorMessages().size());
        assertEquals(LocalDate.now().plusMonths(1), reservations.get(0).getStartDate());
    }

    @Test
    void shouldNotUpdateInvalidReservation() throws DataException {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());
        List<Reservation> reservations = service.findAllReservationsForHost(host);
        LocalDate oldDate = reservations.get(0).getStartDate();

        Reservation updated = new Reservation();
        updated.setId(1);
        updated.setStartDate(LocalDate.now().plusDays(5));

        Result<Reservation> result = service.updateReservation(updated);
        assertFalse(result.isSuccess());
        assertEquals(3, result.getErrorMessages().size());
        assertEquals(oldDate, reservations.get(0).getStartDate());
    }

    @Test
    void shouldDeleteValidReservation() throws DataException {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());
        List<Reservation> reservations = service.findAllReservationsForHost(host);

        Result<Reservation> result = service.deleteReservation(reservations.get(0));
        assertTrue(result.isSuccess());
        assertEquals(0, result.getErrorMessages().size());
    }

    @Test
    void shouldNotDeleteInvalidReservation() throws DataException {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());
        List<Reservation> reservations = service.findAllReservationsForHost(host);

        Reservation fakeReservation = new Reservation();
        fakeReservation.setId(999);

        Result<Reservation> result = service.deleteReservation(fakeReservation);
        assertFalse(result.isSuccess());
        assertEquals(String.format("Reservation %s does not exist", fakeReservation.getId()), result.getErrorMessages().get(0));
    }

    @Test
    void shouldValidateValidReservationFields() {
        Host host = hostService.findHostByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertTrue(hostService.validateHost(host).isSuccess());
        List<Reservation> reservations = service.findAllReservationsForHost(host);
        reservations.get(0).setEndDate(LocalDate.now().plusMonths(2));
        reservations.get(0).setStartDate(LocalDate.now().plusMonths(1));


        Result<Reservation> result = service.checkReservation(reservations.get(0));
        assertTrue(result.isSuccess());
        assertEquals(0, result.getErrorMessages().size());
    }

    @Test
    void shouldNotValidateNullReservation() {
        Result<Reservation> result = service.checkReservation(null);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getErrorMessages().size());
    }

    @Test
    void shouldNotValidateInvalidReservation() {
        Reservation fakeReservation = new Reservation();
        fakeReservation.setStartDate(LocalDate.now().plusDays(-1));
        fakeReservation.setEndDate(LocalDate.now().plusDays(-13));
        fakeReservation.setcostOfStay(BigDecimal.valueOf(-5.0));
        fakeReservation.setHost(new Host());
        fakeReservation.setGuest(new Guest());

        Result<Reservation> result = service.checkReservation(fakeReservation);
        assertFalse(result.isSuccess());
        assertEquals(4, result.getErrorMessages().size());
    }

}