package learn.mastery.ui;

public enum MainMenuOption {
    EXIT(0, "Exit"),
    VIEW_RESERVATIONS(1, "View Reservations"),
    MAKE_RESERVATION(2, "Make a Reservation"),
    EDIT_RESERVATION(3, "Edit a Reservation"),
    CANCEL_RESERVATION(4, "Cancel a Reservation");

    private int value;
    private String message;

    /**
     * Enum constructor specifying the enum
     */
    MainMenuOption(int value, String message) {
        this.value = value;
        this.message = message;
    }

    /**
     * @param value the int value of the target enum
     * @return the enum value found in a valid enum
     */
    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }

    /**
     * Return method for the value of the enum
     *
     * @return the value of the enum
     */
    public int getValue() {
        return value;
    }

    /**
     * Return method for the message stored
     * in the value of the enum
     *
     * @return the message stored in the enum value
     */
    public String getMessage() {
        return message;
    }


}
