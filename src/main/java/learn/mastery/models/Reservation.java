package learn.mastery.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation {

    private int id;
    private Guest guest;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal costOfStay;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getcostOfStay() {
        return costOfStay;
    }

    public void setcostOfStay(BigDecimal costOfStay) {
        this.costOfStay = costOfStay;
    }
}
