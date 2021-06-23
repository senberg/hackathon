package senberg.hackathon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class Dividend extends Event {
    BigDecimal amount;

    public Dividend(String currency, Date date, BigDecimal amount) {
        super(currency, date);
        this.amount = amount;
    }

    // Example: 10kr dividend, eg for every stock you get 10kr to buy new stocks
    // old adjusted factor is the same as number of stocks you own.
    @Override
    public BigDecimal calculateAdjustedFactor(BigDecimal lastPrice, BigDecimal adjustedFactor) {
        BigDecimal dividendPercentage = amount.divide(lastPrice, 10, RoundingMode.HALF_UP);
        BigDecimal stockFactor = BigDecimal.ONE.add(dividendPercentage);
        return adjustedFactor.multiply(stockFactor);
    }

    @Override
    public String getDescription() {
        return "A dividend of " + amount + " " + currency + " was paid out.";
    }
}
