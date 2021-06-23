package senberg.hackathon;

import java.math.BigDecimal;
import java.util.Date;

public class Split extends Event{
    // 1:3 split has a factor of 3, a reverse split 2:1 has a factor of 0.5
    BigDecimal factor;

    public Split(String currency, Date date, BigDecimal factor) {
        super(currency, date);
        this.factor = factor;
    }

    // 1:3 split, eg for every stock you get three
    // old adjusted factor 100, new price roughly one third, eg. new adjusted factor should be 300
    @Override
    public BigDecimal calculateAdjustedFactor(BigDecimal lastPrice, BigDecimal adjustedFactor) {
        return adjustedFactor.multiply(factor);
    }

    @Override
    public String getDescription() {
        return "The stock was split giving out " + factor + " new stocks for every 1 old stock.";
    }
}
