package senberg.hackathon;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

@JsonSerialize(using = EventSerializer.class)
public abstract class Event {
    String currency;
    Date date;

    public Event(String currency, Date date) {
        this.currency = currency;
        this.date = date;
    }

    public abstract BigDecimal calculateAdjustedFactor(BigDecimal lastPrice, BigDecimal adjustedFactor);

    public abstract String getDescription();

    public static int compareDates(Event event1, Event event2) {
        return event2.date.compareTo(event1.date);
    }
}
