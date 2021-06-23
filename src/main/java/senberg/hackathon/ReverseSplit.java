package senberg.hackathon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class ReverseSplit extends Event{
    // 1:3 split has a factor of 3, a reverse split 2:1 has a factor of 0.5
    BigDecimal factor;

    public ReverseSplit(String currency, Date date, BigDecimal factor) {
        super(currency, date);
        this.factor = factor;
    }

    // 2:1 reverse split, eg for every new stocks you get to retain one
    // current adjusted factor 100, new price roughly double, eg. new adjusted factor should be 50
    @Override
    public BigDecimal calculateAdjustedFactor(BigDecimal lastPrice, BigDecimal adjustedFactor) {
        return adjustedFactor.multiply(factor);
    }

    @Override
    public String getDescription() {
        return "The stock was reverse splitted giving out 1 new stock for every " + BigDecimal.ONE.divide(factor, 0, RoundingMode.HALF_UP) + " old stocks.";
    }
}
