package learn.mastery.ui;

import learn.mastery.data.DataException;


//@Component
public class Controller {

    private final View view;


    //Needs Constructor with all services + view other services
    public Controller() {
        //Temp Code to pacify errors
        view = new View(new ConsoleIO());
    }

    //Temp Constructor for General Outline
    public Controller(View view) {
        this.view = view;
    }


    public void run() {
        view.displayHeader("Welcome to Sustainable Foraging");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS:
                    System.out.println("ADD VIEW RESERVATIONS");
                    break;
                case MAKE_RESERVATION:
                    System.out.println("ADD MAKE RESERVATION");
                    break;
                case EDIT_RESERVATION:
                    System.out.println("ADD EDIT RESERVATION");
                    break;
                case CANCEL_RESERVATION:
                    System.out.println("ADD CANCEL RESERVATION");
                    break;

            }
        } while (option != MainMenuOption.EXIT);
    }


}
