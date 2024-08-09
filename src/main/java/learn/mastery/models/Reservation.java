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

    public BigDecimal determineCostOfStay(){int days = (int) (endDate.toEpochDay() - startDate.toEpochDay());
        double totalAmount = 0.0;
        for (int i = 0; i < days; i++) {
            LocalDate date = startDate.plusDays(i);
            if (date.getDayOfWeek().getValue() == 6 ||date.getDayOfWeek().getValue() == 7 ) { // 6 = Saturday, 7 = Sunday
                totalAmount += host.getWeekendRate().doubleValue();
            } else {
                totalAmount += host.getStandardRate().doubleValue();
            }
        }
        return BigDecimal.valueOf(totalAmount);
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

}
