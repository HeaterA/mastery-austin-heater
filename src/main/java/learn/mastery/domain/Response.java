package learn.mastery.domain;

import java.util.ArrayList;
import java.util.List;

public class Response {

    /**
     * Class constructor
     */
    private ArrayList<String> messages = new ArrayList<>();

    /**
     * Returns a boolean based the size of the list
     *
     * @return true or false based on the size of list
     */
    public boolean isSuccess() {
        return messages.size() == 0;
    }

    /**
     * returns the list of string message stored in the list
     *
     * @return message to be stored
     */
    public List<String> getErrorMessages() {
        return new ArrayList<>(messages);
    }

    /**
     * Adds a new string message into the list
     *
     * @param message message to be stored
     */
    public void addErrorMessage(String message) {
        messages.add(message);
    }
}