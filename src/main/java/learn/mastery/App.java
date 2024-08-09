package learn.mastery;

import learn.mastery.data.*;
import learn.mastery.domain.GuestService;
import learn.mastery.domain.HostService;
import learn.mastery.domain.ReservationService;
import learn.mastery.models.Host;
import learn.mastery.ui.ConsoleIO;
import learn.mastery.ui.Controller;
import learn.mastery.ui.View;

public class App {

    public static void main(String[] args) {
        runManualConfiguration();
    }

    private static void runManualConfiguration(){

        ConsoleIO io = new ConsoleIO();
        View view = new View(io);

        GuestRepository guestRepository = new GuestFileRepository("./data/guests.csv");
        HostRepository hostRepository = new HostFileRepository("./data/hosts.csv");
        ReservationRepository reservationRepository = new ReservationFileRepository("./data/reservations/");

        GuestService guestService = new GuestService(guestRepository);
        HostService hostService = new HostService(hostRepository);
        ReservationService reservationService = new ReservationService(guestRepository, reservationRepository);

        Controller controller = new Controller(hostService, guestService, reservationService, view);
        controller.run();
    }


}
