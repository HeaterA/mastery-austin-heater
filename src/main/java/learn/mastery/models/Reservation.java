package learn.mastery.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation {

    private int id;
    private Guest guest;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal costOfStay;
    private Host host; //TODO


    /**
     * Gets the integer id stored inside the
     * reservation object used as a unique identifier
     *
     * @return the integer used to identify the object
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the integer id stored inside the
     * reservation object used as a unique identifier
     *
     * @param id the integer used to identify the object
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the guest object stored inside the
     * reservation object
     *
     * @return the guest object
     */
    public Guest getGuest() {
        return guest;
    }

    /**
     * Sets the guest object stored inside the
     * reservation object
     *
     * @param guest the guest object
     */
    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    /**
     * Gets the value of startDate inside
     * the reservation object
     *
     * @return the LocalDate value
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of startDate inside
     * the reservation object
     *
     * @param startDate the LocalDate value to be stored
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the value of endDate inside
     * the reservation object
     *
     * @return the LocalDate value
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of endDate inside
     * the reservation object
     *
     * @param endDate the LocalDate value to be stored
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the value of costOfStay inside
     * the reservation object
     *
     * @return the BigDecimal value
     */
    public BigDecimal getcostOfStay() {
        return costOfStay;
    }

    /**
     * Sets the value of costOfStay inside
     * the reservation object
     *
     * @param costOfStay the BigDecimal value to be stored
     */
    public void setcostOfStay(BigDecimal costOfStay) {
        this.costOfStay = costOfStay;
    }

    /**
     * Returns the calculated value of CostOfStay based
     * on the difference of the startDate and endDates
     * by summing the values of each day,based of if it used the
     * standardRate or WeekendRate found in the stored host object
     *
     * @return the calculated cost of stay
     */
    public BigDecimal determineCostOfStay() {
        double totalAmount = 0.0;
        for (int i = 0; i < (int) (endDate.toEpochDay() - startDate.toEpochDay()); i++) {
            LocalDate date = startDate.plusDays(i);
            if (date.getDayOfWeek().getValue() == 5 || date.getDayOfWeek().getValue() == 6) { // 5 = Fri, 6 = Sat
                totalAmount += host.getWeekendRate().doubleValue();
            } else {
                totalAmount += host.getStandardRate().doubleValue();
            }
        }
        return BigDecimal.valueOf(totalAmount);
    }

    /**
     * Gets the host object stored inside the
     * reservation object
     *
     * @return the host object
     */
    public Host getHost() {
        return host;
    }

    /**
     * Sets the host object stored inside the
     * reservation object
     *
     * @param host the host object to be stored
     */
    public void setHost(Host host) {
        this.host = host;
    }

}
