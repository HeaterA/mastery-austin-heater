package learn.mastery;

import learn.mastery.data.*;
import learn.mastery.domain.GuestService;
import learn.mastery.domain.HostService;
import learn.mastery.domain.ReservationService;
import learn.mastery.ui.ConsoleIO;
import learn.mastery.ui.Controller;
import learn.mastery.ui.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@PropertySource("classpath:data.properties")
public class App {

    public static void main(String[] args) {
        runSpringAnnotationConfiguration();
    }

    private static void runSpringAnnotationConfiguration() {
        //Annotation Setup
        ApplicationContext container = new AnnotationConfigApplicationContext(App.class);
        //stays same as XML
        Controller controller = container.getBean(Controller.class);
        controller.run();
    }

    private static void runManualConfiguration() {

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
